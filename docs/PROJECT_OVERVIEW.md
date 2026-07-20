# The miniFlask Compiler — Complete Program Explanation

This is the current, complete picture of the project: what it does, exactly
how a program moves through it end to end, the full file structure, and a
direct mapping from every requirement given so far to where it was
implemented. It supersedes earlier partial explanations — this reflects the
program **after** the Resolver, Symbol Table extensions, and compile-time
template evaluation were added on top of the original code generator and
error-handling framework.

---

## 1. What this program does

It is an **educational compiler**, written in **Java** on **ANTLR 4**, for a
source language called **miniFlask**: a small full-stack web program made of

| Input | Example | Language |
|---|---|---|
| Backend logic | `tests/app.py` | a subset of Python + Flask idioms |
| Frontend views | `tests/templates/index.html` | HTML with embedded Jinja2 |
| Styling | `tests/static/styles.css` | CSS |

Running it (`Main.compile()`) parses these files, builds an **AST** for each
(the intermediate representation), runs **semantic analysis** and a
**Resolver** phase on each, and — only if everything checks out — **generates
a real, runnable Flask project**:

```
python generated/app.py       # → http://127.0.0.1:5000, behaves like tests/app.py
```

If anything is wrong at any stage — a syntax error, an undefined variable, a
missing template, an unmapped AST node — nothing is generated and a single,
grouped, human-readable error report is produced instead. The compiler never
crashes on bad input.

---

## 2. End-to-end flow

### 2.1 The big picture

```
tests/app.py ──Lexer/Parser──► Python AST ──SymbolTableBuilder──► declarations + 6 checks
                                     │                                       │
                                     └──────────────► PythonResolver ◄───────┘
                                                    (binds every read, detects
                                                     undefined vars, tracks
                                                     compile-time values)
                                                            │
                              ┌─────────────────────────────┘
                              ▼
                    render_template(...) call sites discovered
                    (template name + argument names + LITERAL argument values)
                              │
                              ▼
tests/templates/*.html ──Lexer/Parser──► Template AST ──SymbolTableBuilder──►
        (only templates actually rendered are parsed)   (declares + resolves + binds,
                                                           seeded with Flask builtins +
                                                           the discovered context vars)
                              │
                              ▼
                     TemplateResolver (report facade over
                     the builder's already-captured bindings)
                              │
        ── if ANY stage produced an error, STOP HERE and print the report ──
                              │
                              ▼
              PythonCodeGenerator.generate(program)  ──► generated/app.py
              TemplateCodeGenerator.generate(ast)     ──► generated/templates/*.html   (live)
              TemplateCodeGenerator.generate(ast, literalArgs) ──► generated/rendered/*.html  (folded preview)
              static assets copied                    ──► generated/static/*
              AST + Symbol Table + Resolver dump       ──► generated/compiler_report.txt
```

Everything above runs inside one method, `FlaskProjectGenerator.generate()`,
which is the single error boundary for the whole compiler: every stage
reports into a shared `ErrorReporter` instead of throwing past it, and the
method always returns cleanly (`true` = files were generated, `false` = check
the report) — it never lets an exception escape to the caller.

### 2.2 The Python side, step by step

1. **Lex + parse.** `PythonLexer`/`PythonParser` (ANTLR-generated from
   `grammars/python/*.g4`) turn `tests/app.py` into a parse tree. A
   `SyntaxErrorListener` is attached to both, so any syntax error is
   collected — parsing then returns `null` rather than handing a broken tree
   to the next stage.
2. **Build the AST.** `PythonVisitor` walks the parse tree and builds the
   real AST out of `python.models.*` classes (`Program`, `FunctionDef`,
   `IfStatement`, `BinaryExpression`, `IDTrailer`, `StringAtom`, ...) — this
   is the tree every later stage actually operates on.
3. **Declare (`SymbolTableBuilder`).** Walks the AST and: opens a scope per
   function/if/elif/else/for/while, defines symbols for functions,
   parameters, loop variables, import targets and assignment targets, and
   raises six semantic checks (duplicate function, duplicate parameter,
   `return`/`break`/`continue` misuse, stray `global`). It does **not** look
   at *reads* — a condition, an iterable, or a plain expression's right-hand
   side is never visited here.
4. **Resolve (`PythonResolver`, new).** A second pass, with its own fresh
   `SymbolTable`, that does what step 3 structurally cannot: it visits every
   expression, binding each identifier *read* to the `Symbol` it refers to,
   flagging genuinely undefined names, and tracking best-effort compile-time
   constant values (a value survives until a non-literal reassignment or a
   mutating call like `.append(...)` invalidates it). Top-level function
   names are hoisted first, so one route can call a helper defined later in
   the file without a false "undefined" — module-level variables are
   deliberately *not* hoisted, matching real Python's own `NameError` rule.
5. **Discover templates.** `FlaskProjectGenerator` walks the same AST looking
   for `render_template('name.html', kw=value, ...)` calls. For each call
   site it records: which template, every keyword argument's *name* (for
   step 7's undefined-variable seeding), and — separately — only the keyword
   arguments whose *value* is a provable literal (e.g. `page='home'`), for
   the folded-preview feature in step 9.
6. **Generate Python.** `PythonCodeGenerator` walks the AST one more time and
   emits real Python text: every statement/expression/operator has a direct
   mapping (see `docs/CODEGEN.md` for the full table), explicit source
   parentheses and string quoting survive, and — if a Flask app was detected
   — a runtime `@app.errorhandler(404)`/`@app.errorhandler(Exception)` block
   is injected before the `if __name__ == "__main__":` guard.

### 2.3 The template side, step by step

7. **Lex + parse + analyze (only templates actually referenced).** Each
   template discovered in step 5 is parsed by `HTMLLexer`/`HTMLParser` (which
   embed the Jinja2 grammar) into a Template AST (`jinja2.models.*`), the
   same way as step 1–2. Its `SymbolTableBuilder` — unlike the Python one —
   **already** visits every expression (conditions, iterables, filter
   arguments, attribute expressions...) and resolves every identifier read
   against a symbol table seeded with (a) names Flask injects into every
   render (`url_for`, `request`, `get_flashed_messages`, ...) and (b) the
   argument names from step 5. This is what makes "undefined variable"
   checking work *across* the Python/HTML boundary. It also now records,
   for every resolved identifier, *which* declaration it bound to and the
   line it was used on (a small, additive change to `visitIdentifier`), and
   tracks a literal value for `{% set x = <literal> %}` the same way the
   Python resolver does.
8. **Resolve (`TemplateResolver`, new).** A thin facade — not a second
   AST walk — over the state step 7 already captured. Its only real job is
   turning that state into the same kind of readable report the Python
   resolver produces.
9. **Generate HTML.** `TemplateCodeGenerator` walks the Template AST and
   emits HTML/Jinja2 text (see `docs/CODEGEN.md` for the full mapping). It
   runs twice per template:
   - **Live mode** (no known values) → `generated/templates/<name>.html`,
     the file Flask actually serves. `{% if %}`/`{% for %}`/filters/etc. are
     all preserved exactly, because the data behind them (`products`, a
     looked-up `product`, form input) is only known once a real request
     arrives.
   - **Folded-preview mode**, once per render-call site with at least one
     literal keyword argument → `generated/rendered/<route-name>.html`.
     Here, `{% if/elif/else %}` chains whose condition depends only on that
     call site's known literals are collapsed to just the taken branch's
     body — dropping the `{% if %}` wrapper and every branch that can be
     proven not to run — while anything not provable (loops over runtime
     data, filters, calls) is left exactly as in live mode. This file is
     **never read by the generated app** — it exists purely to demonstrate
     the resolver's values driving real template evaluation without risking
     the live app's correctness.

### 2.4 Finishing up

10. **Gate.** If any stage (1, 3, 4, 7) reported a problem, generation is
    skipped entirely — no partial or incorrect output is ever written.
11. **Write output.** `app.py`, the live templates, the folded previews, and
    the copied static assets are all written under `generated/`.
12. **Write the report.** `generated/compiler_report.txt` is assembled from
    `python.printer.ASTPrinter`/`jinja2.printer.ASTPrinter` (new
    `treeToString(...)` overloads, so the AST can be captured as text instead
    of only printed to `System.out`) plus both resolvers' `report()` output —
    one file showing, for every variable in every file: its declaration site,
    its scope, its kind/type, its resolved value (or "unknown" when it isn't
    provable), and every line it was used on.

---

## 3. Full project structure

```
flask-compiler-1/
├── grammars/                     # ANTLR .g4 grammar sources (hand-written)
│   ├── python/                   #   miniFlask's Python subset
│   ├── html/                     #   HTML + embedded Jinja2
│   ├── jinja2/                   #   standalone Jinja2 expression grammar
│   └── css/                      #   CSS selectors/declarations
│
├── src/
│   ├── antlr/{python,html,jinja2,css}/   # ANTLR-generated lexers/parsers (do not edit)
│   │
│   ├── python/                   # PYTHON PIPELINE
│   │   ├── models/               #   AST node classes (Program, FunctionDef, IfStatement,
│   │   │                         #   BinaryExpression, IDTrailer, atoms, trailers, ...)
│   │   ├── visitor/               #   PythonVisitor: parse tree → AST
│   │   ├── symbol_table/          #   Symbol, Scope, SymbolTable, SymbolTableBuilder
│   │   │                         #   (declarations + 6 semantic checks), CompilerError
│   │   ├── resolver/               #   PythonResolver (NEW): binds reads, detects undefined
│   │   │                         #   variables, tracks compile-time constant values
│   │   └── printer/                #   ASTPrinter (tree dump; now also to String)
│   │
│   ├── jinja2/                   # TEMPLATE PIPELINE
│   │   ├── models/                #   Template AST (TemplateFile, HTML element nodes,
│   │   │                         #   statement nodes for if/for/set/macro/block/extends/
│   │   │                         #   include, expression nodes, literals)
│   │   ├── visitor/                #   AntlrToTemplateAstVisitor: parse tree → AST
│   │   ├── symbol_table/           #   Symbol (now with value+usages), Scope, SymbolTable
│   │   │                         #   (now with a binding map), SymbolTableBuilder
│   │   │                         #   (declares AND resolves reads + type-checks),
│   │   │                         #   TypeChecker, CompilerError, semantic_rules/ (UlLiRule)
│   │   ├── resolver/                #   TemplateResolver (NEW): report facade over the
│   │   │                         #   builder's already-captured resolution state
│   │   └── printer/                #   ASTPrinter (tree dump; now also to String)
│   │
│   ├── css/, html/                # auxiliary pipelines (CSS AST; an earlier standalone
│   │                              # HTML-only AST/symbol-table pipeline) — teaching
│   │                              # material, not part of the main compile() path
│   │
│   ├── errors/                   # UNIFIED ERROR-HANDLING FRAMEWORK
│   │   ├── CompilerStage.java     #   PARSING | SEMANTIC_ANALYSIS | CODE_GENERATION | IO
│   │   ├── CompilerException.java #   base of ParseError / SemanticError / CodeGenError
│   │   ├── CompilerProblem.java   #   one normalized, printable error entry
│   │   ├── ErrorReporter.java     #   central collector; adopts both pipelines' native
│   │   │                         #   error types; prints one report grouped by stage
│   │   └── SyntaxErrorListener.java # ANTLR listener: collects syntax errors, never lets
│   │                              #   a broken parse tree reach the next stage
│   │
│   ├── resolver/                 # SHARED VALUE MODEL
│   │   └── ConstantValue.java     #   INT|FLOAT|STRING|BOOL|NONE|LIST|DICT|UNKNOWN — a
│   │                              #   compile-time-provable value, used by both resolvers
│   │                              #   and by the template folding feature
│   │
│   ├── codegen/                  # CODE GENERATION + ORCHESTRATION
│   │   ├── PythonCodeGenerator.java   # Python AST → executable Python text
│   │   ├── TemplateCodeGenerator.java # Template AST → HTML/Jinja2 text (live mode, and
│   │   │                             # an opt-in constant-folding mode for previews)
│   │   └── FlaskProjectGenerator.java # the end-to-end driver described in §2
│   │
│   └── Main.java                 # entry point; Main.compile() runs the whole pipeline
│
├── tests/                        # the miniFlask SOURCE PROGRAM (compiler input)
│   ├── app.py                    #   a "Product Store" Flask app: list/find/add/delete
│   │                              #   routes, helper functions, global state, flash msgs
│   ├── templates/                #   index.html (the real multi-page template) +
│   │                              #   smaller fixtures for symbol-table/type-check demos
│   └── static/styles.css
│
├── generated/                    # compiler OUTPUT (git-ignored, rebuilt every run)
│   ├── app.py                    #   executable Flask backend
│   ├── templates/*.html          #   live templates Flask renders per request
│   ├── rendered/*.html           #   compile-time-folded previews (see §2.3 step 9)
│   ├── static/*                  #   copied through unchanged
│   └── compiler_report.txt       #   AST + Symbol Table + Resolver, for every file
│
└── docs/
    ├── CODEGEN.md                 # full AST-node → target-language mapping tables
    ├── RESOLVER_AND_BYTECODE.md   # architecture review, Resolver design rationale,
    │                              # the bytecode question answered in full
    ├── PROJECT_OVERVIEW.md        # this document
    └── PROJECT_OVERVIEW_AR.md     # Arabic explanation (pre-Resolver state)
```

---

## 4. Objectives → implementation map

Every requirement given across both rounds of work, matched to exactly where
it lives.

### Round 1 — error handling + code generation

| Objective | Implementation | Where |
|---|---|---|
| Flask/Python backend error handling (invalid input, parse/semantic/generation failures, unexpected exceptions never crash the app) | `ErrorReporter` central collector + `SyntaxErrorListener` + the try/catch boundary in `generate()` | `src/errors/*`, `codegen/FlaskProjectGenerator.java` |
| Python-side exception/error classes, cleanly organized | `CompilerException` → `ParseError`/`SemanticError`/`CodeGenError`; `python.symbol_table.CompilerError` (6 kinds, +`UNDEFINED_VARIABLE` in round 2) | `src/errors/*`, `python/symbol_table/CompilerError.java` |
| Code generator walking the AST, producing Python + HTML | `PythonCodeGenerator`, `TemplateCodeGenerator` | `src/codegen/*` |
| AST-node → target-language mapping, documented | full tables, one row per node class | `docs/CODEGEN.md` |
| Real output files (Python source, HTML templates, supporting files) | `generated/app.py`, `generated/templates/*.html`, `generated/static/*` | written by `FlaskProjectGenerator.writeOutput` |
| Flask route updates (runtime error handling in the generated app) | injected `@app.errorhandler(404)`/`@app.errorhandler(Exception)` block | `PythonCodeGenerator.flaskErrorHandlerBlock()` |
| Integration with existing Jinja2/HTML error display | same `[KIND] file line N: message` format reused throughout | `errors/CompilerProblem.java` |
| End-to-end explanation of how the generator works | | `docs/CODEGEN.md` §4, and §2 of this document |

### Round 2 — architecture review, Resolver, Symbol Table, evaluation, bytecode

| Objective | Implementation | Where |
|---|---|---|
| Review the architecture; identify and justify any missing phase | Found: Python side never resolved reads (no Resolver possible without it); Jinja2 side already resolved reads but discarded the binding. Both explained before implementing. | `docs/RESOLVER_AND_BYTECODE.md` §1 |
| Resolver: walk the AST, use the Symbol Table, resolve identifiers/scopes, determine value/type where possible, connect identifier nodes to declarations, prepare the AST for codegen | `PythonResolver` (full second pass) + `TemplateResolver` (facade over the builder's now-recorded bindings) | `python/resolver/PythonResolver.java`, `jinja2/resolver/TemplateResolver.java` |
| "If another approach fits better than a separate Resolver, explain why" | Done explicitly for the Jinja2 side (extending the builder beats a duplicate walk) | `docs/RESOLVER_AND_BYTECODE.md` §1 |
| Symbol Table: extend with name/type/value/scope/declaration location/usages, reusing the existing implementation | Both `Symbol` classes gained `value` (`ConstantValue`) and `usageLines`, additively — every pre-existing constructor and call site still compiles unchanged | `python/symbol_table/Symbol.java`, `jinja2/symbol_table/Symbol.java`, `resolver/ConstantValue.java` |
| Code Generator: produce executable Python | unchanged generator, now fed a resolved AST | `codegen/PythonCodeGenerator.java` |
| Code Generator: "fully rendered HTML using resolved values" | Implemented as *safe, scoped* compile-time branch folding per render-call-site literal — full paragraph of justification for why unconditional freezing was rejected (would break `products` add/delete) | `codegen/TemplateCodeGenerator.java` (`tryFold`/`evalCondition`), `docs/RESOLVER_AND_BYTECODE.md` §4 |
| Output structure resembling `app.py` + template files + CSS/JS | `generated/app.py`, `generated/templates/*.html`, `generated/rendered/*.html`, `generated/static/*` | §3 above |
| Readable AST / Symbol Table / Resolver output, showing declaration/scope/type/value per variable | `generated/compiler_report.txt` | `FlaskProjectGenerator.writeReport`, new `ASTPrinter.treeToString(...)` on both sides |
| Full bytecode explanation (what it is, who generates it, CPython's process, the PVM, whether it's in scope, where it belongs in the architecture, the AST/SymbolTable/Resolver/CodeGen/Bytecode/PVM relationship) | complete, dedicated section | `docs/RESOLVER_AND_BYTECODE.md` §6 |
| Recommendation: stop at Python/HTML, or continue to bytecode? | **Stop** — justified against the project's own educational objectives | `docs/RESOLVER_AND_BYTECODE.md` §6, final subsection |
| Constraints: keep the architecture, don't rewrite unnecessarily, reuse AST/Visitor, explain decisions first | Every existing constructor/API kept working; new capability added via additive fields, an opt-in constructor parameter, and one small 4-line edit to `visitIdentifier` — not a rewrite anywhere | throughout; itemized in `docs/RESOLVER_AND_BYTECODE.md` |

---

## 5. Verified, not assumed

- **Live app unaffected.** `generated/app.py` and `generated/templates/index.html`
  are byte-identical before and after adding the Resolver and the folding
  feature (folding only ever engages when a caller explicitly opts in with
  known values — the live-template call site never does). Confirmed with a
  diff, and with a full Flask test-client run: `GET /`, `/products`,
  `/product/<id>`, `/add`, a `POST /add` that shows up on the products page, a
  `GET /delete/<id>` that actually removes the item and shows its flash
  message once, and a 404 served by the generated handler.
- **The Resolver catches a real class of bug the old pipeline could not.**
  `if totally_unknown_name > 0: ...` now correctly fails with
  `[UNDEFINED_VARIABLE] ... Undefined variable 'totally_unknown_name'` —
  previously silent, because nothing walked the condition.
- **Two real false positives were found and fixed while testing the
  Resolver itself:** keyword-argument names (`page=`, `methods=`) were
  briefly mis-resolved as variable reads instead of parameter labels, and
  `True`/`False` — which this grammar's lexer only recognizes in lowercase —
  lex as plain identifiers, so they were added to the resolver's builtin
  allowlist rather than silently misreported as undefined.
- **The folding feature was exercised on real output**, not just unit logic:
  the `page='home'` preview correctly collapses the `<title>` if/elif chain
  and all four page-section blocks to just the home content, while leaving
  the flash-message loop (which depends on runtime state, not `page`) as
  live Jinja.

---

## 6. Known limitations (stated honestly, not hidden)

- **Constant propagation is a single forward pass, not dataflow analysis.**
  A value is "the most recently observed literal assignment in program
  text order" — sound (never wrongly claims a value), but not complete
  (can miss values a real dataflow pass would catch).
- **Inherited block-scoping model.** `SymbolTableBuilder` (both the original
  and, by design, `PythonResolver` mirroring it for consistency) opens a new
  scope per `if`/`for`/`while`, even though real Python has no block scoping.
  A nested reassignment can therefore be recorded against a new inner-scope
  symbol instead of updating the enclosing one — e.g. `get_max_product_id`'s
  `max_id` looks more "constant" in the report than it truly is at runtime.
  This predates the Resolver and was deliberately left alone rather than
  changed silently (fixing it means changing the builder's scoping, a larger,
  separate change).
- **Attribute-expression sub-parsing loses line numbers.** Identifiers inside
  an HTML attribute value (`href="{{ url_for(...) }}"`) are re-parsed from an
  isolated one-line string, so their usage lines all report as `1` in the
  resolver output. Pre-existing in `AntlrToTemplateAstVisitor`, surfaced —
  not introduced — by the new usage tracking.
- **miniFlask is a subset.** No classes, `try/except`, `lambda`, f-strings,
  comprehensions, `{% with %}`, or template-inheritance chains beyond
  `extends`/`include` reconstruction. Comments are discarded by the lexers.
- **No bytecode generation**, by deliberate recommendation — see
  `docs/RESOLVER_AND_BYTECODE.md` §6.

---

## 7. How to build and run

```bash
# compile
javac -encoding UTF-8 -cp antlr-4.13.2-complete.jar -d out $(find src -name "*.java")

# run the compiler (Main.compile())
java -cp "out;antlr-4.13.2-complete.jar" Main

# run the compiled app
python generated/app.py            # → http://127.0.0.1:5000
```

Inspect `generated/compiler_report.txt` for the full AST/Symbol
Table/Resolver dump, and `generated/rendered/*.html` for the compile-time
template-evaluation previews.
