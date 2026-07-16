# The miniFlask Compiler — Full Project & Solution Documentation

## 1. What this project is

This project is an **educational compiler / translator** written in **Java** on top of
**ANTLR 4** (v4.13.2). It compiles a source language called **miniFlask** into a real,
runnable **Flask web project**.

A miniFlask program is a small full-stack web application consisting of:

| Part | Example file | Language |
|---|---|---|
| Backend logic | `tests/app.py` | a *subset* of Python + Flask idioms |
| Frontend views | `tests/templates/index.html` | HTML with embedded Jinja2 |
| Styling | `tests/static/styles.css` | CSS |

The compiler parses these files, builds **Abstract Syntax Trees (ASTs)** — the
intermediate representation — runs **semantic analysis** (symbol tables, scope
checking, type checking, structural rules), reports **all errors** in a unified
format, and finally **generates executable target code**:

```
generated/
├── app.py               ← executable Python/Flask backend
├── templates/*.html     ← Jinja2/HTML templates for the browser UI
└── static/*             ← static assets, copied through
```

The output runs directly on real Python and real Flask:

```bash
python generated/app.py       # → http://127.0.0.1:5000
```

### Why "miniFlask" and not "Flask"?

**Flask** is the real web framework running on full Python. **miniFlask** is the name
of the *source language* this compiler understands: a teaching-scale **subset** of
Python + Flask + Jinja2, defined precisely by the grammar files in `grammars/`.
The subset covers functions, decorators (`@app.route`), conditionals, loops,
imports, `global`, expressions, lists/dicts/sets, and the common Jinja2 template
constructs — but deliberately excludes classes, `try/except`, `lambda`, f-strings,
comprehensions, etc. The *input* is miniFlask; the *output* runs on real Flask.
The compiler is the bridge between the two.

---

## 2. Architecture: the compilation pipeline

```
  tests/app.py ────► PythonLexer/Parser ───► Python AST ───► SymbolTableBuilder ──┐
                      (SyntaxErrorListener)   (PythonVisitor)  (semantic checks)   │
                                                                                   ├──► FlaskProjectGenerator
  tests/templates/*.html ► HTMLLexer/Parser ► Template AST ► SymbolTableBuilder ───┘        │
                      (SyntaxErrorListener)   (AntlrToTemplate  (Flask-aware seeding,       ▼
                                               AstVisitor)      type checking, rules)  generated/app.py
                                                                                        generated/templates/*.html
                                                                                        generated/static/*
```

The five classic stages:

1. **Lexical analysis** — ANTLR-generated lexers turn raw text into token streams.
2. **Syntactic analysis** — ANTLR-generated parsers build parse trees according to
   the `.g4` grammars. Custom error listeners collect syntax errors instead of
   printing them and continuing with a broken tree.
3. **AST construction** — hand-written visitor classes convert the noisy parse
   trees into clean, typed AST node classes (the intermediate representation).
4. **Semantic analysis** — symbol-table builders walk the ASTs, build nested
   scopes, define/resolve symbols, run type checks and pluggable semantic rules,
   and *collect* (never throw) semantic errors.
5. **Code generation** — generator classes walk the ASTs again and emit target
   code: Python for the backend, HTML/Jinja2 for the frontend.

Every stage runs behind a single **error boundary** (`FlaskProjectGenerator.generate()`):
the outcome is always either *generated files + empty report* or *no files + a
grouped error report*. The compiler never crashes on bad input.

**Entry point:** `Main.compile()` → `codegen.FlaskProjectGenerator.generate()`.

---

## 3. Project structure, file by file

### 3.1 `grammars/` — the language definitions (ANTLR)

| File | Defines |
|---|---|
| `python/PythonLexer.g4`, `PythonParser.g4` | The miniFlask Python subset: `prog → stmt*`; simple statements (expressions, assignments, augmented assignments, `pass/break/continue/return`, imports, `global`), compound statements (`if/elif/else`, `while/else`, `for/else`, decorated function definitions), full expression precedence (`**`, unary, `* / % // @`, `+ -`, shifts, `& ^ \|`), comparisons (`== != < > <= >= in, not in, is, is not`), boolean conditions (`and/or/not`), atoms (identifiers, ints, floats, strings, booleans, `None`, lists, dicts, sets, parenthesised expressions), trailers (`.attr`, `(call args)`, `[subscript]`), and INDENT/DEDENT-based blocks. |
| `html/HTMLLexer.g4`, `HTMLParser.g4` | HTML documents with Jinja2 embedded: normal & void elements, attributes (whose values may contain `{{ expr }}`), raw text, `{{ output }}`, and `{% ... %}` statements (`if/elif/else`, `for`, `set` inline & block, `macro`, `block`, `extends`, `include`), Jinja expressions with filters. |
| `jinja2/Jinja2Lexer.g4`, `Jinja2Parser.g4` | Standalone Jinja2 expression grammar. |
| `css/CSSLexer.g4`, `CSSParser.g4` | CSS: selector groups, combinators, filters (class/id/attribute/pseudo), declarations and value parts. |

### 3.2 `src/antlr/` — generated recognizers

The lexers, parsers, listeners and base visitors that ANTLR generates from the
grammars (packages `antlr.python`, `antlr.html`, `antlr.jinja2`, `antlr.css`).
Never edited by hand.

### 3.3 `src/python/` — the Python pipeline

**`models/` — the Python AST.** Every node extends `ASTNode` (stores `nodeName`,
source `line`, and exposes `getChildren()` + `describe()`):

* Roots: `Program`, `Statement`, `SimpleStatement` (list of small statements),
  `CompoundStatement`.
* Small statements: `ExpressionStatement` (condlist `=` condlist),
  `AugAssignStatement` (`x += e`, all 12 operators), `ReturnStatement`,
  `PassStatement`, `BreakStatement`, `ContinueStatement`, `GlobalStatement`,
  `SimpleImportStatement` (`import a.b`), `FromImportStatement`
  (`from a import x, y` / `*`).
* Compound statements: `IfStatement` (parallel condition/body lists + optional
  else), `WhileStatement`, `ForStatement` (iterator IDs + iterable + body +
  optional else), `DecoratorStatement` (decorators + `FunctionDef`), `Body`,
  `Decorator`.
* Functions: `FunctionDef` (name, `Parameter` list with optional type annotation
  and default value, optional return type, body).
* Conditions: `CompoundCondition` (`and/or/not`), `RelationalComparison`,
  `Comparison`, `Condition`.
* Expressions: `BinaryExpression`, `UnaryExpression`, `IDTrailer` (an identifier
  followed by a chain of `Trailer`s — `.attr`, `(args)`, `[index]`).
* Trailers: `Trailer`, `CallArguments`/`Argument` (positional & keyword),
  `SubscriptArguments`.
* Atoms: `ID`, `IntegerAtom`, `FloatAtom`, `BoolAtom`, `StringAtom` (raw token,
  quotes preserved — including triple-quoted docstrings), `None`, `ParenAtom`
  (explicit source parentheses survive in the AST), `List`, `Set`, `Dictionary`.
* `enums/Operation` — the shared operator enum.

**`visitor/PythonVisitor.java`** — extends the generated
`PythonParserBaseVisitor`; one `visit...` method per grammar rule converts the
parse tree into the AST above.

**`symbol_table/`** — semantic analysis:

* `Scope` — a named scope with a symbol map, a parent pointer, and the set of
  names declared `global` inside it.
* `Symbol` / `SymbolKind` (`VARIABLE`, `FUNCTION`, `PARAMETER`).
* `SymbolTable` — a scope stack (global / function / for / while / if / else...).
  `define()` redirects to the module scope when the name was declared `global`.
* `SymbolTableBuilder` — walks the AST, opens/closes scopes, defines functions,
  parameters, loop iterators, assignment targets and import names, **and collects
  semantic errors** (see §5).
* `CompilerError` *(new)* — Python-side semantic error record with its own `Kind`
  enum, mirroring the Jinja2 one so both pipelines report identically.

**`printer/ASTPrinter.java`** — pretty tree printing for teaching/debugging.

### 3.4 `src/jinja2/` — the template pipeline

**`models/` — the template AST.** Every node extends `TemplateNode` (line number
+ `getChildren()` + `describe()`):

* `file/TemplateFile` — the root; a list of content nodes.
* Content: `HtmlTextNode` (verbatim text), `OutputNode` (`{{ expr }}`),
  `html/HTMLNormalElementNode` (tag + attributes + children),
  `html/HTMLVoidElementNode` (tag + attributes).
* Attributes: `HtmlAttributeNode` (name + value parts), value parts being
  `AttributeTextNode` or `AttributeExpressionNode` (a `{{ expr }}` inside an
  attribute value, sub-parsed into a real expression tree).
* Statements: `ForStatementNode`, `IfStatementNode`/`IfBranchNode`,
  `SetStatementNode` (inline `{% set x = e %}` and block
  `{% set x %}...{% endset %}`), `MacroStatementNode`/`ParameterNode`,
  `BlockStatementNode`, `ExtendsStatementNode`, `IncludeStatementNode`.
* Expressions: `IdentifierNode`, literals (`StringLiteralNode`,
  `NumberLiteralNode`, `BooleanLiteralNode`, `NoneLiteralNode`),
  `BinaryExpressionNode`/`UnaryExpressionNode` (+ `Operation` enum),
  `PropertyAccessNode` (`product.name`), `IndexAccessNode` (`items[0]`),
  `CallExpressionNode`/`ArgumentNode` (`url_for('index', id=1)`),
  `FilterExpressionNode` (`products|length`, chained filters supported),
  `ListExpressionNode`, `DictionaryExpressionNode`.

**`visitor/AntlrToTemplateAstVisitor.java`** — converts the HTML/Jinja parse tree
into the template AST, including sub-parsing `{{ ... }}` found inside attribute
values.

**`symbol_table/`** — semantic analysis:

* `Scope`/`ScopeKind` (template / for / macro / block), `Symbol`/`SymbolKind`
  (`VARIABLE`, `LOOP_VAR`, `MACRO`, `PARAMETER`, `BLOCK`), `Type` and
  `TypeChecker` (infers static types of literals/lists/dicts, checks binary and
  unary operations, assignment compatibility, and iterability of `for` targets).
* `SymbolTableBuilder` — builds scopes exactly like Jinja2 does (e.g. `{% if %}`
  does *not* open a scope, `{% for %}` does and seeds the magic `loop` variable;
  macro defaults are evaluated in the caller's scope; blocks are template-level
  names), resolves identifiers, and collects `CompilerError`s.
* `CompilerError` — the existing error record (`UNDEFINED_VARIABLE`, `SCOPE`,
  duplicates, `TYPE_MISMATCH`, `TYPE_ERROR`, `INVALID_HTML_STRUCTURE`).
* `semantic_rules/ISemanticRule` — a pluggable rule interface (Strategy pattern);
  `UlLiRule` validates that `<li>` only appears inside `<ul>`/`<ol>`.

### 3.5 `src/css/` and `src/html/` — auxiliary pipelines

CSS: `AntlrToStyleSheet` builds a stylesheet AST (`Ruleset`, selector model,
declaration/value-part model) plus its own symbol table. HTML: an earlier
standalone HTML document pipeline with symbol table and semantic rules
(`UlLiRule`, `BrokenReferenceRule`). Both are kept as teaching material; the main
compilation path uses the jinja2 pipeline for templates.

### 3.6 `src/errors/` — the unified error-handling framework *(new)*

| Class | Role |
|---|---|
| `CompilerStage` | `PARSING`, `SEMANTIC_ANALYSIS`, `CODE_GENERATION`, `IO`. |
| `CompilerException` | Abstract base of all recoverable pipeline failures; carries stage + file + line; converts itself to a reportable problem. |
| `ParseError`, `SemanticError`, `CodeGenError` | Stage-specific exceptions. |
| `CompilerProblem` | One normalized, printable error entry: `[KIND] file line N: message`. |
| `ErrorReporter` | The central collector. Adopts native error types from both sub-pipelines (`jinja2.symbol_table.CompilerError`, `python.symbol_table.CompilerError`), adopts exceptions, wraps unexpected `RuntimeException`s as `INTERNAL`, and prints **one report grouped by stage** (Syntax Errors / Semantic Errors / Code Generation Errors / I-O Errors). |
| `SyntaxErrorListener` | An ANTLR `BaseErrorListener` attached to every lexer *and* parser (defaults removed). Collects each syntax error with file, line and column. When any exist, parsing returns `null` — no AST is ever built from a broken parse tree. |

### 3.7 `src/codegen/` — the code generator *(new)*

**`PythonCodeGenerator.java`** — walks the Python AST and emits executable
Python source:

* instanceof-dispatch in the same style as the existing symbol-table builders
  (consistent architecture, no new visitor infrastructure);
* 4-space indentation tracked by nesting level; empty bodies become `pass`;
* **meaning preservation:** explicit source parentheses survive as `ParenAtom`;
  nested `and/or/not` operands are re-parenthesised so the AST's grouping stays
  correct under real Python precedence; strings are emitted as their raw tokens
  (original quoting, docstrings intact);
* complete operator maps for binary (`+ - * / // % ** @ & | ^ << >>`), unary
  (`+ - ~`), comparison (`== != < > <= >= in, not in, is, is not`), boolean
  (`and or not`) and all 12 augmented assignments;
* **Flask detection:** while emitting decorators it notices `@app....`; if a
  Flask app is present, it injects a runtime error-handling block (see §5.4)
  immediately *before* the `if __name__ == "__main__":` guard (located by
  searching the top-level `if` conditions for `__name__`);
* any unknown node or unmapped operator raises `CodeGenError` with the node's
  `describe()` and source line.

**`TemplateCodeGenerator.java`** — walks the template AST and emits HTML/Jinja2:

* text nodes verbatim (original formatting preserved wherever the parser kept it);
* elements: `<tag attrs>children</tag>`; true void elements (`img`, `input`,
  `link`, `meta`, `br`, ...) self-close as `<tag …/>`, while empty non-void
  elements (e.g. `<textarea>`) correctly regain their closing tag;
* attributes: `name="text{{ expr }}text"`; attributes with no value parts are
  emitted as boolean attributes (`required`);
* statements: `{% for %}…{% endfor %}`, `{% if %}{% elif %}{% else %}{% endif %}`,
  `{% set %}` (both forms), `{% macro %}`, `{% block %}`, `{% extends %}`,
  `{% include %}`;
* expressions: identifiers, literals, binary/unary (nested binaries
  parenthesised), property/index access, calls with keyword arguments, filters
  with arguments, lists and dictionaries;
* unknown nodes/operators raise `CodeGenError` with file + line.

**`FlaskProjectGenerator.java`** — the end-to-end driver and error boundary:

1. **Parse the backend** (`app.py`) with syntax-error collection → Python AST.
2. **Analyze the backend** — Python `SymbolTableBuilder` with error collection.
3. **Discover the frontend** — walk the Python AST for
   `render_template('name.html', kw=...)` calls; the result is a map
   *template name → set of context variable names*. Only templates the backend
   actually renders get compiled.
4. **Parse + analyze each template** — before analysis the template's symbol
   table is **seeded** with (a) names Flask injects into every render
   (`url_for`, `get_flashed_messages`, `request`, `session`, `config`, `g`,
   `range`, `dict`, `namespace`) and (b) the discovered context variables. This
   makes "undefined variable" checking work **across the Python↔HTML boundary**.
   A missing template file is itself a reported semantic error.
5. **Gate** — if *any* stage reported errors, generation is skipped entirely.
6. **Generate** — write `generated/app.py` and `generated/templates/*.html`,
   copy `generated/static/*`. All I/O failures become `CodeGenError`s; any
   unexpected `RuntimeException` is caught last-resort and reported as
   `INTERNAL`. The method returns `true` (files written) or `false`
   (report available) — it never throws.

### 3.8 `src/Main.java` — entry point

Keeps the original per-pipeline demo methods (`python()`, `jinja()`, `types()`,
`css()`) and adds **`compile()`**, which builds a `FlaskProjectGenerator` for
`tests/app.py` + `tests/templates` + `tests/static` → `generated/`, runs it, and
on failure prints the grouped `ErrorReporter` report.

### 3.9 `tests/` — the miniFlask source program (compiler *input*)

* **`app.py`** — a complete "Product Store" Flask app: a products list (list of
  dicts), helper functions (`find_product_by_id`, `get_max_product_id`,
  `remove_product_by_id` using `global`), and routes `/`, `/products`,
  `/product/<int:id>`, `/add` (GET+POST with form handling and `flash`),
  `/delete/<int:id>`, plus the `__main__` guard.
* **`templates/index.html`** — one multi-page template (home / product list /
  details / add form) driven by a `page` context variable; uses `{% if/elif %}`,
  `{% for %}`, `{% set %}`, filters (`|length`, `"%.2f"|format(...)`),
  `url_for(...)` with keyword args, and flash-message display.
* **`templates/variables.html`, `types.html`, `scopes.html`, `ulli.html`,
  `attr_value.html`** — focused fixtures for the symbol-table, type-checking and
  semantic-rule demos.
* **`static/styles.css`** — the stylesheet (also used by the CSS pipeline demo).

### 3.10 `generated/` — compiler *output* (git-ignored)

Created by `Main.compile()`; see §6 for content and behavior guarantees.

### 3.11 `docs/`

* `CODEGEN.md` — the code-generation & error-handling design document with full
  AST-node → target-language mapping tables.
* `PROJECT_OVERVIEW_AR.md` — the full project explanation in Arabic.
* `PROJECT_OVERVIEW.md` — this document.

---

## 4. AST → target-language mapping (summary)

The complete node-by-node tables live in `docs/CODEGEN.md`. In brief:

* **Python AST → Python text.** Statements map 1:1 (`IfStatement` →
  `if/elif/else` blocks, `DecoratorStatement` → `@decorator(...)` lines + `def`,
  `SimpleStatement` → one line joined with `;`, ...). Expressions rebuild
  operator text from the `Operation` enum; `IDTrailer` chains rebuild
  `obj.attr(args)[index]`; atoms print their stored values (strings keep raw
  quoting). Structure (indentation) encodes block nesting.
* **Template AST → HTML text.** Content nodes concatenate in order; elements
  wrap their children; statements re-wrap their bodies in `{% ... %}` /
  `{% end... %}` pairs; `OutputNode` becomes `{{ expr }}`; attribute value parts
  interleave text and `{{ expr }}` inside one quoted value.
* A node generates **Python**, **HTML**, or (for the orchestrator) **both**:
  the Python generator additionally emits the Flask error-handler block (Python
  *support code*), and the project generator decides file placement
  (`app.py` vs `templates/…` vs copied `static/…`).

---

## 5. Error handling — the full catalog (~26 handled situations)

### 5.1 Syntax errors (stage: PARSING) — 1 kind, unlimited instances

`[SYNTAX]` from `SyntaxErrorListener` on all four recognizers (Python
lexer/parser, HTML lexer/parser), each with file, line and column. On any syntax
error the parse result is discarded (`null`) so later stages never see a broken
tree. An unreadable source file is a `ParseError` as well.

### 5.2 Python semantic errors — 6 kinds *(new)*

| Kind | Detects |
|---|---|
| `DUPLICATE_FUNCTION` | the same function name defined twice in one scope |
| `DUPLICATE_PARAMETER` | a repeated parameter name in a `def` |
| `RETURN_OUTSIDE_FUNCTION` | `return` at module level |
| `BREAK_OUTSIDE_LOOP` | `break` outside any `for`/`while` (loop depth resets across function boundaries) |
| `CONTINUE_OUTSIDE_LOOP` | `continue` outside any loop |
| `GLOBAL_AT_MODULE_LEVEL` | a `global` declaration that has no effect |

### 5.3 Template semantic errors — 9 kinds (pre-existing, now unified)

`UNDEFINED_VARIABLE`, `SCOPE` (declared but not visible here),
`DUPLICATE_VARIABLE`, `DUPLICATE_MACRO`, `DUPLICATE_PARAMETER`,
`DUPLICATE_BLOCK`, `INVALID_HTML_STRUCTURE` (ul/li rule), `TYPE_MISMATCH`,
`TYPE_ERROR`. Because of Flask-aware seeding (§3.7 step 4), undefined-variable
checking is now **cross-file**: a variable passed by `render_template` counts as
defined in that template — and only in that template.

### 5.4 Cross-file, generation and I/O errors — 7 situations *(new)*

missing template referenced by `render_template`; unreadable source file;
unsupported AST node during Python generation; unmapped operator (binary /
unary / aug-assign); unsupported template node or Jinja operator; failure
writing a generated file; failure copying static assets.

### 5.5 Last-resort guard — 1

Any unexpected `RuntimeException` anywhere in the pipeline is caught at the
boundary and reported as `[INTERNAL]` — the compiler never shows a raw crash.

### 5.6 Runtime error handling in the *generated app* — 2 handlers

When the source uses a Flask app, the generated `app.py` contains:

```python
@app.errorhandler(404)
def _mf_not_found(error): ...          # friendly not-found page

@app.errorhandler(Exception)
def _mf_internal_error(error): ...     # passes HTTPExceptions through,
                                       # renders a 500 page for real crashes
```

so the compiled web app shows friendly error pages in the browser instead of
tracebacks.

**Design principles:** errors are *collected, not thrown one-at-a-time* (one
compile run shows every problem at once); every entry carries stage + kind +
file + line; the printed format `[KIND] line N: message` matches the display the
project already used, so old and new reporting look identical.

---

## 6. Testing & verification (what was actually executed)

### 6.1 Build

The entire source tree (223+ Java files including the new packages) compiles
cleanly with `javac` against `antlr-4.13.2-complete.jar`.

### 6.2 Happy path — full end-to-end run

* `Main.compile()` translated `tests/app.py` + `tests/templates/index.html` with
  **zero errors**, producing `generated/app.py`, `generated/templates/index.html`
  and copying `generated/static/styles.css`.
* `generated/app.py` **passes real Python's syntax check** (`python -m py_compile`).
* The generated app was executed on real Flask (3.1.2) through Flask's test
  client and behaves identically to the source program:
  * `GET /`, `/products`, `/product/1`, `/product/999`, `/add` → **200**
  * `POST /add` with form data → **302** redirect; the new product appears on
    the products page ✔
  * `GET /delete/2` → **302**; the product is really removed (the `global
    products` reassignment works) and the flash message shows once ✔
  * `GET /nonexistent` → **404** served by the *generated* error handler with
    the friendly HTML body ✔

### 6.3 Error paths — four purpose-built bad projects

| Case | Input | Result |
|---|---|---|
| Syntax error | `def broken(:` | `success = false`; `[SYNTAX] … line 1: mismatched input ':' … (column 11)`; no crash, no output files |
| Semantic errors | module-level `global`, `break` at top level, `def f(a, a)`, a second `def f()`, top-level `return` | **all five** errors reported in one run, each with the correct kind and line |
| Template error | `{{ missing_variable }}` in a rendered template | `[UNDEFINED_VARIABLE] page.html line 1` (while the legitimately passed `title` variable was accepted) |
| Missing template | `render_template('nope.html')` | semantic error: *render_template refers to missing template 'nope.html'* |

In every failure case generation was skipped entirely — no partial or corrupt
output is ever produced.

---

## 7. How to build and run

```bash
# 1) compile the compiler (from the project root)
javac -encoding UTF-8 -cp antlr-4.13.2-complete.jar -d out $(find src -name "*.java")

# 2) run the compiler (executes Main.compile())
java -cp "out;antlr-4.13.2-complete.jar" Main
#   → Compiling miniFlask project (tests/app.py)...
#     generated generated\app.py
#     generated generated\templates\index.html
#     copied    generated\static\styles.css

# 3) run the compiled application
python generated/app.py            # requires Python 3 + Flask installed
#   → open http://127.0.0.1:5000
```

(In IntelliJ the ANTLR jar is already configured as a module library —
just run `Main`.)

---

## 8. Design decisions & constraints honored

* **No rewrite.** All pre-existing classes (ASTs, visitors, symbol tables, type
  checker, semantic rules, printers) are untouched or minimally extended; the
  Python `SymbolTableBuilder` gained an *optional* error-list constructor — the
  old one still works.
* **Consistent architecture.** New generators use the same instanceof-dispatch
  visitor style as the existing builders; the new Python `CompilerError` mirrors
  the existing Jinja2 one; the report format matches the existing display.
* **Modularity.** Error handling lives in its own `errors` package, generation
  in its own `codegen` package; each generator handles exactly one target
  language and the orchestrator handles files/IO/sequencing.
* **Meaning preservation over pretty-printing.** The generated code is
  guaranteed to *behave* like the source (verified at runtime); parentheses and
  string quoting are preserved where the AST keeps them.

### Known limitations (inherent, documented)

* Comments are discarded by the lexers, so generated files contain no original
  comments (the generator adds its own marker comments).
* Multi-line literals (e.g. the products dict list) are re-emitted on one line;
  some inter-tag whitespace in HTML is lost where the parser dropped it. Output
  is 100 % functionally correct, just formatted more compactly than the source.
* Only the miniFlask subset is accepted (no classes, `try/except`, f-strings,
  comprehensions, `{% with %}`, template inheritance chains beyond
  `extends`/`include` reconstruction, etc.).
* Templates never referenced by a `render_template` call are not compiled
  (by design — they are not part of the program).

---

## 9. One-paragraph summary

The miniFlask compiler is a complete educational compiler that takes a small
full-stack web program (a Python/Flask backend plus Jinja2/HTML templates),
runs it through the classic pipeline — ANTLR lexing/parsing, AST construction
via visitors, symbol tables with nested scopes, type checking and pluggable
semantic rules — and, uniquely, performs **Flask-aware cross-file analysis**
(template variables are validated against what the backend actually passes to
`render_template`). A three-layer error-handling framework guarantees the
compiler never crashes: syntax, semantic, generation and I/O problems are all
collected into one grouped report. When the program is clean, two code
generators translate the ASTs into an executable target project — real Python
with injected 404/500 error handlers, real Jinja2/HTML templates, and copied
static assets — that runs in the browser with behavior identical to the source,
verified end-to-end against real Python and real Flask.
