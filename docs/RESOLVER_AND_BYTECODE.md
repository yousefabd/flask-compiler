# Architecture Review, Resolver, and the Bytecode Question

This document covers the second round of work on the miniFlask compiler: a
review of the compiler architecture, the **Resolver** phase added for both
pipelines, the resulting **Symbol Table** extensions, the (deliberately
scoped) **template evaluation** feature, the new output structure, and a full
answer to "should this compiler generate Python bytecode?"

---

## 1. Architecture review

### Verdict: the phase list was incomplete, but only in one specific, provable way

The proposed architecture —

```
Lexer → Parser → AST → Semantic Analysis → Symbol Table → Resolver → Code Generator
```

— names the right phases. Before touching any code, the existing pipeline was
read in full (`SymbolTableBuilder`, `Symbol`, `SymbolTable` for both Python and
Jinja2) to check whether a Resolver was genuinely missing or just relabeled
work the builders already did. The two pipelines turned out to be in very
different states:

**Python side — the Resolver was genuinely, provably missing.**
`python.symbol_table.SymbolTableBuilder` only visits *declaration* sites. Proof,
read directly from the code before any change:

```java
private void visitIfStatement(IfStatement is) {
    symbolTable.enterScope("if");
    visitBody(is.bodies.get(0));   // the CONDITION itself is never visited
    ...
}
private void visitForStatement(ForStatement fs) {
    symbolTable.enterScope("for");
    fs.iterators.forEach(...);     // the ITERABLE expression is never visited
    ...
}
```

No `if`/`while`/`for` condition, no function's default value or return-type
annotation, and no plain expression statement's right-hand side was ever
walked for identifier *reads*. Concretely: `SymbolTableBuilder` would happily
accept `if totally_unknown_name > 0: ...` without a single error, because it
never looked at `totally_unknown_name` — it isn't a declaration, so the
declaration-only builder had no code path that would ever see it. This is
verified in §3 below with an actual test case that now fails correctly.

**Jinja2 side — no separate Resolver phase was needed.**
`jinja2.symbol_table.SymbolTableBuilder.visitExpression`/`visitIdentifier`
*already* walks every expression (conditions, iterables, filter arguments,
attribute expressions, ...) and already calls `symbolTable.resolve(name)` to
do undefined-variable/type checking. The only thing missing was that the
successful resolution was thrown away — `visitIdentifier` found the `Symbol`,
read its `Type`, and returned only the `Type`, discarding *which* symbol it
was and the fact that a read had happened at all.

**Both sides — the Symbol Table stored declarations, not resolution state.**
Neither `Symbol` class had a value, a usage list, or (Python's case) even a
declaration line. There was also no AST-node → Symbol binding map anywhere —
nothing connected a specific identifier node back to the declaration it
refers to, which is the literal definition of "the AST isn't resolved yet."

### What this means for the two Resolvers

Because the gap was different on each side, the fix was different on each
side — and each is explained/justified inline, per the instructions, rather
than assumed:

- **Python gets a real, separate `PythonResolver` pass.** It re-walks the AST
  with its own fresh `SymbolTable`, this time visiting every expression, so it
  can bind every identifier read to its declaration and — as a direct,
  demonstrable consequence — detect undefined variables for the first time.
  A second pass (rather than folding this into `SymbolTableBuilder`) was
  chosen deliberately: the builder already owns six tested semantic checks
  (duplicate function/parameter, stray `return`/`break`/`continue`, stray
  `global`); mixing "declare and validate structure" with "resolve every
  expression and track values" into one method set would blur two
  responsibilities and risk regressing tested code for no benefit. A second
  pass over a small AST is cheap, and keeping it separate is the standard way
  compilers structure a binder/resolver distinct from a declaration pass.

- **Jinja2 gets a thin `TemplateResolver` facade, not a duplicate walk.**
  Re-implementing a whole second traversal of the template AST just to redo
  work `SymbolTableBuilder.visitExpression` already does correctly would be
  pure waste and a maintenance hazard (two copies of the same walk that could
  drift apart). Instead, `visitIdentifier` was extended by four lines to
  record the binding and a usage line on the `Symbol` it already found:

  ```java
  if (visible != null) {
      symbolTable.recordBinding(id, visible);
      visible.addUsage(id.getLineNumber());
      return visible.getType();
  }
  ```

  `TemplateResolver` then just turns that captured state into the same kind of
  report the Python resolver produces. This is the "if another approach fits
  the existing architecture better than a separate Resolver, explain why
  before implementing it" case the task called out directly — here it is.

### Nothing else was missing

Lexer → Parser → AST → Semantic Analysis → Code Generator were all present,
tested, and (from the first round of work) already wired end-to-end with error
handling. No other phase (a separate "linker", a dedicated "type inference"
pass beyond the existing `TypeChecker`, an IR lowering step, etc.) is
justified for a program this size — adding one would be exactly the
"unnecessary complexity" the task says to avoid.

---

## 2. The Resolver

### 2.1 `python.resolver.PythonResolver`

Constructed fresh, called once with `resolve(Program)`, then queried:

```java
PythonResolver resolver = new PythonResolver();
resolver.resolve(program);
resolver.getErrors();      // List<python.symbol_table.CompilerError>
resolver.getBindings();    // Map<ASTNode, Symbol>  — every identifier's declaration
resolver.getSymbolTable(); // the resolver's own SymbolTable
resolver.report();         // readable text dump
```

What it does, walking in the same shape as `SymbolTableBuilder` (same
scope-opening structure) but now visiting **every** expression:

- **Binds every identifier read** to the `Symbol` it resolves to, in an
  `IdentityHashMap<ASTNode, Symbol>` — literally "connect every identifier
  node with its declaration."
- **Detects undefined variables** — the capability that was entirely absent
  before, because nothing walked reads.
- **Tracks best-effort constant values.** A symbol's value is the most
  recently observed literal assignment in a single forward pass over the
  source text (see §2.3 for exactly what this can and can't prove).
- **Hoists top-level function names** before the main walk, so one route can
  call a helper defined later in the same file without a false "undefined"
  (real Python allows this — the call only happens after the whole module has
  loaded). Module-level *variables* are deliberately **not** hoisted, because
  `x = y` before `y` exists is a genuine `NameError` in real Python too;
  hiding that class of bug would be wrong.
- **Recognizes builtins** (`len`, `str`, `range`, `__name__`, ...) so ordinary
  Python doesn't get flagged as undefined just for using the standard library.

### 2.2 `jinja2.resolver.TemplateResolver`

A thin, honestly-scoped facade (see §1) over a `SymbolTable` that
`SymbolTableBuilder` already resolved. Its only job is `report()` — turning
the bindings/usages/values the builder already captured into the same kind of
readable output the Python resolver produces.

### 2.3 What "constant value" honestly means here

This is **not** a fixed-point dataflow analysis — it is a single forward
textual pass, and that limitation is stated up front rather than glossed
over. A symbol's value is invalidated the moment:

- it's reassigned to something that isn't a literal,
- it's reassigned via `+=`/`-=`/etc. (depends on the prior runtime value),
- a call is made through it that could mutate it (`list.append(...)`,
  `.method(...)`, ...), or
- it's used as an attribute/subscript assignment target (`obj.attr = ...`).

This is demonstrated directly by the test program (`tests/app.py`):
`products` starts as a literal list of dicts — the resolver proves this and
records the full value — but `add_product()`'s `products.append(new_product)`
and `remove_product_by_id()`'s `global products; products = new_products`
both invalidate it. The generated report shows exactly that:

```
VARIABLE products (line 8) = [{'id': 1, ...}, ...]     <- right after declaration
...
VARIABLE products (line 8) = unknown | used at lines [...]   <- final state, after mutation is seen
```

That the value goes from known to unknown, not the other way around, is the
correctness property that matters: the resolver never claims a value is
constant unless the source text proves it, and it errs toward *not* knowing
rather than guessing.

**Inherited limitation, stated honestly:** `SymbolTableBuilder` opens a new
scope for every `if`/`elif`/`else`/`for`/`while` block, even though real
Python has no block scoping (only module/function scope). `PythonResolver`
mirrors that same per-block scope model for consistency with the existing,
tested symbol table — but it means a reassignment inside a nested `if` block
(e.g. `get_max_product_id`'s `if p['id'] > max_id: max_id = p['id']`) is
recorded against a **new symbol in that nested scope**, not the enclosing
function's `max_id`, so the outer `max_id`'s reported value can look more
"constant" than it truly is at runtime. This is a pre-existing modeling choice
in the codebase (not something this change introduced), and fixing it
properly means changing how the builder scopes `if`/`for`/`while` — a larger,
separate change than "add a resolver," left out deliberately rather than
touched silently.

---

## 3. Symbol Table extensions

Both `Symbol` classes gained the same three things, **additively** (existing
constructors and call sites are untouched):

| Field | Python `Symbol` | Jinja2 `Symbol` |
|---|---|---|
| declaration line | added (`declarationLine`, `-1` if unknown) | already had `lineNumber` |
| type / kind | `SymbolKind` (already existed) | `SymbolType` |
| **value** *(new)* | `ConstantValue value` | `ConstantValue resolvedValue` |
| **usages** *(new)* | `List<Integer> usageLines` | `List<Integer> usageLines` |
| **binding map** *(new)* | `PythonResolver`'s own `IdentityHashMap<ASTNode, Symbol>` | `SymbolTable.bindings` (`IdentityHashMap<TemplateNode, Symbol>`) |

`resolver.ConstantValue` (new, shared by both pipelines) is a small tagged
value — `INT | FLOAT | STRING | BOOL | NONE | LIST | DICT | UNKNOWN` — used
only when the source text *proves* a value; everything else is `UNKNOWN`.

**Update (post-merge with `origin/main`):** the Jinja2 side of this table
changed shape after this branch was later merged with `origin/main`'s own,
independently-developed type-checking work (`TypeCheckerRule`/`SymbolType`/
`SemanticContext` — see the merge history for the full reconciliation). Main
had *also* added a `type`/`value` pair to `jinja2.symbol_table.Symbol`, but
with different meaning: `SymbolType` is a type shallowly inferred at
declaration time, and `getValue()` returns the raw declared `ExpressionNode`
(the initializer itself, not an evaluated constant). The Resolver's own
value-tracking, described above, therefore uses a differently-named field —
`resolvedValue`, via `getResolvedValue()`/`setResolvedValue(ConstantValue)`
— so the two independently-added features coexist on the same class without
colliding. `python.symbol_table.Symbol` was untouched by that merge (the
Python pipeline has no counterpart to `TypeCheckerRule`), so its column above
is still accurate as originally written.

`Symbol.toString()` on both sides prints its value/usages when known, e.g.
(Python side):

```
VARIABLE max_id (line 46) = 0 | used at lines [48, 50]
FUNCTION find_product_by_id (line 33) = unknown | used at lines [75, 107]
```

`python.symbol_table.SymbolTable` also gained `defineOrGet(Symbol)` — an
upsert used only by the resolver, because re-assigning an existing name
(`x = 1` then `x = 2`) is completely normal in Python and must return the
*same* `Symbol` object so usages/values accumulate on it, unlike
`define()`'s "reject duplicates" behavior, which exists for the builder's
*declaration* checks (duplicate function/parameter) and must keep rejecting.

A new `UNDEFINED_VARIABLE` kind was added to `python.symbol_table.CompilerError.Kind`,
mirroring the Jinja2 error taxonomy so both pipelines report through the same
`ErrorReporter` in the same format.

---

## 4. Code generation: what changed, and — importantly — what didn't

### The live Flask app is untouched, and that was verified, not assumed

`generated/app.py` and `generated/templates/index.html` were byte-diffed
against the pre-resolver output: identical. The reason is structural, not
incidental: `TemplateCodeGenerator`'s new folding logic is gated behind an
**opt-in constructor parameter** (`Map<String, ConstantValue> knownValues`)
that defaults to empty; `FlaskProjectGenerator` still constructs the live
templates with the original no-argument constructor. Folding physically
cannot engage unless a caller passes known values explicitly.

This was a deliberate design boundary, not an oversight: the data a route
passes to a template — the products list, a looked-up product, form input —
is **only known once a real HTTP request arrives**. Freezing `products` at
compile time into the live template would silently break the add/delete
functionality that was verified end-to-end in the first round of work (adding
a product and having it show up, deleting one and having it disappear). No
version of "template evaluation" was implemented that could put that at risk.

### What "Generate fully rendered HTML from the Jinja AST" became, and why

The literal request — bake every value into the HTML — is unsafe in general,
for the reason above. The compiler instead implements the part of it that
*is* sound: **compile-time branch folding scoped to what a specific
`render_template(...)` call site provably passes as a literal.**

`FlaskProjectGenerator` now discovers, per call site (not just per template):

```python
render_template('index.html', page='home')                       # page: literal
render_template('index.html', page='products', products=products) # page: literal, products: not
```

For every call site with at least one literal keyword argument, it generates
one extra file — `generated/rendered/<route-function-name>.html` — using
`TemplateCodeGenerator`'s folding mode with that call site's literal values.
Inside `ifStatement()`, folding is **all-or-nothing per chain**: every branch
condition in an `{% if %}...{% elif %}...{% endif %}` must be provably
true/false using only the known values, or the whole chain falls back to the
exact same live-Jinja output as before. A branch that resolves to `true` is
inlined as plain body content (no `{% if %}` left in the output); a branch
that resolves to `false` is dropped entirely; an unresolvable branch aborts
folding for that chain. Verified output for the `page='home'` call site:

```html
<!-- live generated/templates/index.html -->
<title>{% if page == 'home' %}Home - Product Store
{% elif page == 'products' %}All Products
...{% endif %}</title>
...
{% if page == 'home' %}<h1>Welcome to Product Store</h1>...{% endif %}
{% if page == 'products' %}...{% endif %}   <!-- entire products section -->
{% if page == 'details' %}...{% endif %}    <!-- entire details section -->
{% if page == 'add' %}...{% endif %}        <!-- entire add-form section -->
```

```html
<!-- generated/rendered/index.html (page='home' folded) -->
<title>Home - Product Store</title>
...
<h1>Welcome to Product Store</h1>...   <!-- only the home section survives -->
<!-- products/details/add sections: gone entirely -->
{% if messages %}...{% endfor %}{% endif %}   <!-- untouched: depends on runtime flash state -->
```

Note the flash-message loop stayed as live Jinja — `messages` isn't a
call-site literal, so folding correctly refuses to touch it. This is the
"whenever possible" qualifier from the task taken literally: fold exactly what
is provable, leave everything else exactly as before.

These preview files are **never read by `generated/app.py`** — they exist
purely to demonstrate the resolver's values driving real template evaluation,
completely decoupled from the live, request-driven app.

---

## 5. Output structure (updated)

```
generated/
├── app.py                         ← executable Flask backend (unchanged from before)
├── templates/
│   └── index.html                 ← the LIVE template Flask renders per request (unchanged)
├── rendered/                      ← NEW: compile-time template-evaluation previews
│   ├── index.html                 ←   render_template(..., page='home')      folded
│   ├── view_products.html         ←   render_template(..., page='products')  folded
│   ├── product_details.html       ←   render_template(..., page='details')   folded
│   └── add_product.html           ←   render_template(..., page='add')       folded
├── static/
│   └── styles.css                 ← copied through unchanged
└── compiler_report.txt            ← NEW: AST + Symbol Table + Resolver, for every file
```

### `compiler_report.txt`

One combined, readable text file (not just console output) containing, per
the task's requirement to show "declaration, scope, inferred type, resolved
value" for every variable:

```
--- Python AST (tests/app.py) ---
Program (line 1) Statements: 
|    Statement.CompoundStatement.DecoratorStatement (line 63) FunctionDef: 
...

--- Python Resolver (declarations, scopes, values, usages) ---
Scope: global
  - VARIABLE products (line 8) = unknown | used at lines [35, 43, 47, 57, 70, 95]
  - FUNCTION find_product_by_id (line 33) = unknown | used at lines [75, 107]
  ...
Scope: function get_max_product_id
  - VARIABLE max_id (line 46) = 0 | used at lines [48, 50]

--- Template AST (index.html) ---
...
--- Template Resolver (index.html) ---
Scope: TEMPLATE "template"
  - VARIABLE page (line 0) | used at lines [8, 9, 10, 11, 34, 44, 71, 106]
  - VARIABLE messages (line 26) = unknown | used at lines [27, 28]
  ...
```

(A pre-existing, unrelated artifact surfaced by this report, worth flagging
honestly: identifiers that appear *inside HTML attribute values*, like
`url_for` in `href="{{ url_for(...) }}"`, are re-parsed by
`AntlrToTemplateAstVisitor.parseAttributeExpression` from an isolated
one-line string, so their usage lines all show as `1`. This is a limitation
in the existing attribute sub-parser predating this work, not something the
resolver introduced — the resolver just made it newly visible.)

Both `python.printer.ASTPrinter` and `jinja2.printer.ASTPrinter` gained a
`treeToString(...)` overload (their existing `printTree(...)`/`printNode(...)`
methods, which print straight to `System.out`, are untouched) so the report
can embed the AST as text without disturbing console output.

---

## 6. The Bytecode question

### What Python bytecode is

Python bytecode is the low-level, stack-based instruction set that CPython's
*own* compiler produces internally from your source file — instructions like
`LOAD_FAST`, `BINARY_ADD`, `CALL_FUNCTION`, `RETURN_VALUE`, packaged into
`code` objects (one per module/function/class body). It is not part of the
Python *language* — it's an implementation detail of one specific Python
implementation, and the exact instruction set has changed in nearly every
recent CPython release (3.10 added "zero-cost" exceptions, 3.11 substantially
restructured the instruction layout for speed, etc.). You can inspect it
yourself with the standard `dis` module (`python -m dis app.py`).

### Who generates it

CPython's own compiler (`Python/compile.c` and friends in the CPython source
tree) — automatically, invisibly, every time you run a `.py` file. It is not
something an application developer, or a course project, is expected to hand-
generate; it's purely internal machinery of the reference interpreter.

### How CPython converts source into bytecode

1. **Tokenize** the source.
2. **Parse** it into CPython's own AST (the same one exposed to Python code
   via the `ast` module — a completely different, unrelated AST from the one
   this project builds).
3. Run CPython's **own symbol-table pass** (for scope/closure resolution —
   again, internal and separate from anything in this project).
4. **Compile** the AST into a control-flow graph of pseudo-instructions.
5. Run **peephole/optimization passes** over that graph.
6. **Assemble** the final bytecode into `code` objects.
7. Optionally **cache** the result to a `.pyc` file (via `marshal`), so
   re-running the same unmodified file skips steps 1–6.

### What the Python Virtual Machine (PVM) is, and how it executes bytecode

The PVM is CPython's bytecode interpreter (`Python/ceval.c`) — a big loop that
reads one instruction at a time from a `code` object and executes it against:
a per-call **frame** (holding local variables, the exception/block stack, and
a link to the caller's frame) and an **operand stack** (`LOAD_FAST x` pushes
`x`'s value; `BINARY_ADD` pops two values, adds them, pushes the result;
`CALL_FUNCTION` pushes a new frame and starts interpreting the callee's own
bytecode). This loop, dispatching one opcode after another, is what actually
*runs* a Python program.

### Should this compiler generate bytecode directly, or generate Python source?

**Generate Python source — which is exactly what it already does.**
Justification:

1. **Replicating CPython's bytecode correctly is a moving target unrelated to
   compiler-construction fundamentals.** The instruction set is
   version-specific and has changed substantially across recent releases;
   matching it exactly would mean re-implementing CPython's own compiler
   internals (including its optimizer), not teaching lexing/parsing/semantic
   analysis/code generation.
2. **Generating source is strictly more portable.** A `.py` file runs
   correctly on any CPython version, and on non-CPython implementations
   (PyPy, MicroPython, ...) that don't share CPython's bytecode format at all.
   Hand-rolled bytecode would only ever run on one specific interpreter
   version.
3. **This compiler's actual job is source-to-source translation**
   (miniFlask → Python + HTML). Python source *is* the natural target — real
   Python then compiles that source to bytecode itself, for free, using
   correct, battle-tested code that this project has no reason to reinvent.

### Where bytecode and the PVM belong in the picture — and where they don't

They are **outside the scope of this project**, and outside its architecture
diagram. They are not a phase of this compiler; they are what happens
*afterward*, inside a completely different program (`python.exe`/CPython),
when someone runs `python generated/app.py`. The relationship, drawn
correctly:

```
 ┌─────────────────────── this compiler's job ───────────────────────┐
 │  miniFlask AST → Symbol Table → Resolver → Code Generator          │
 │                                                    │                │
 │                                                    ▼                │
 │                                          generated/app.py (text)    │
 └──────────────────────────────┬──────────────────────────────────────┘
                                 │  (you run: python generated/app.py)
                                 ▼
 ┌──────────────── CPython's own job — not this project ─────────────┐
 │  Python source → CPython's own AST → CPython's own symtab pass →   │
 │  CPython's own compiler → Bytecode → PVM executes it               │
 └──────────────────────────────────────────────────────────────────┘
```

The AST, Symbol Table, Resolver and Code Generator built in this project are
*analogous in role* to CPython's own AST/symtable/compiler — but they are a
**separate, higher-level translation** (miniFlask → Python), not a
continuation of CPython's pipeline. This project hands off one complete,
valid Python text file and stops; CPython restarts its own entire pipeline
from scratch the moment it reads that file.

### Recommendation: stop at Python + HTML — do not add bytecode generation

Justified against the project's own stated educational objectives:

- The project's teaching arc — lexing, parsing, AST construction, semantic
  analysis, symbol tables, resolution, and source-level code generation — is
  already a complete, coherent, appropriately-scoped story for an educational
  compiler. It ends at exactly the point a real source-to-source translator
  should end.
- Adding real bytecode generation would mean reimplementing a large,
  version-fragile piece of CPython's internals that has nothing to do with
  miniFlask's own semantics — swapping "teach compiler construction" for
  "reverse-engineer one specific interpreter's private instruction format."
- It would provide **zero behavioral benefit**: real Python already produces
  correct bytecode from the generated `app.py` for free, the instant it runs.

If the *concept* of bytecode/virtual machines is independently interesting to
explore, the appropriate, much more common compiler-course exercise is to
design a **small, original bytecode format and virtual machine for
miniFlask itself** (not an attempt to reproduce CPython's) — a genuinely new,
separate project, not a "next phase" of this one.
