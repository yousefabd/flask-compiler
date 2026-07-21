# The miniFlask Compiler — Complete Program Explanation

This is the current, complete picture of the project: what it does, exactly
how a program moves through it end to end, the full file structure, and a
direct mapping from every requirement given so far to where it was
implemented. It supersedes earlier partial explanations — this reflects the
program **after**: the Resolver and Symbol Table extensions; the merge with
`origin/main`'s independently-developed `TypeCheckerRule` type-checking
design; and the pure static-HTML generation stage (`output/` + `compiler_output/`)
added on top of the original code generator and error-handling framework.

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
**Resolver** phase on each, and — only if everything checks out — produces
**two independent kinds of output** from the same front end:

1. A real, runnable **executable Flask project** (`generated/`):
   ```
   python generated/app.py       # → http://127.0.0.1:5000, behaves like tests/app.py
   ```
2. A fully pre-rendered, **Jinja-free static site** (`output/`) plus the
   compiler's own analysis artifacts (`compiler_output/`) — one final HTML
   page per route, with every `{% if/for %}` and `{{ expr }}` evaluated and
   substituted at compile time, not left for a template engine to run later.

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
                                                           the discovered context vars,
                                                           type-checked by TypeCheckerRule)
                              │
                              ▼
                     TemplateResolver (report facade over
                     the builder's already-captured bindings)
                              │
        ── if ANY stage produced an error, STOP HERE and print the report ──
                              │
              ┌───────────────┴────────────────────────────┐
              ▼                                              ▼
   EXECUTABLE APP (generated/)                    PURE STATIC SITE (output/, compiler_output/)
   PythonCodeGenerator.generate(program)           ModuleContextExtractor + RouteTable
     ──► generated/app.py                            (Python AST → initial literal values,
   TemplateCodeGenerator.generate(ast)                 @app.route → URL patterns)
     ──► generated/templates/*.html   (live)         StaticPageGenerator.generate(ast, context)
   TemplateCodeGenerator.generate(ast, literalArgs)     ──► output/<route>.html   (Jinja fully
     ──► generated/rendered/*.html  (folded preview)       evaluated — zero {% %}/{{ }} left)
   static assets copied                              app.py/CSS/JS copied unmodified
     ──► generated/static/*                             ──► output/app.py, output/static/*
   AST + Symbol Table + Resolver dump                 AST-as-JSON + semantic report + log
     ──► generated/compiler_report.txt                   ──► compiler_output/*
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
4. **Resolve (`PythonResolver`).** A second pass, with its own fresh
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
   arguments whose *value* is a provable literal (e.g. `page='home'`), used
   by both the folded-preview feature (step 9) and the static-site stage (§2.5).
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
   checking work *across* the Python/HTML boundary. It also records, for
   every resolved identifier, *which* declaration it bound to and the line
   it was used on, and tracks a literal value for `{% set x = <literal> %}`
   the same way the Python resolver does. A separate, pluggable
   `TypeCheckerRule` (from `origin/main`, folded in by a later merge — see
   §3's note) then makes a second pass over the same AST checking binary/
   unary/call/index/property operations for type mismatches, using its own
   `SymbolType` inference.
8. **Resolve (`TemplateResolver`).** A thin facade — not a second AST walk —
   over the state step 7 already captured. Its only real job is turning that
   state into the same kind of readable report the Python resolver produces.
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

### 2.4 Finishing the executable-app path

10. **Gate.** If any stage (1, 3, 4, 7) reported a problem, generation is
    skipped entirely — no partial or incorrect output is ever written.
11. **Write output.** `app.py`, the live templates, the folded previews, and
    the copied static assets are all written under `generated/`.
12. **Write the report.** `generated/compiler_report.txt` is assembled from
    `python.printer.ASTPrinter`/`jinja2.printer.ASTPrinter` (`treeToString(...)`
    overloads, so the AST can be captured as text instead of only printed to
    `System.out`) plus both resolvers' `report()` output — one file showing,
    for every variable in every file: its declaration site, its scope, its
    kind/type, its resolved value (or "unknown" when it isn't provable), and
    every line it was used on.

### 2.5 The pure static-HTML path (a separate goal, same front end)

The Python and template ASTs above are also fed into a second, independent
generation stage — `FlaskProjectGenerator.generateStaticSite()` — that
produces genuinely final HTML, not another Flask template:

13. **Extract module context (`ModuleContextExtractor`).** Looks *only* at
    top-level Python assignment statements (never descending into function
    bodies) and evaluates each literal right-hand side into a `ConstantValue`
    — e.g. `products = [...]`. This is deliberately **not** the same thing as
    `PythonResolver`'s tracked value for `products`: that resolver correctly
    invalidates it the moment it sees `.append(...)` in a route handler
    (right for proving safety everywhere in the program); a static-site
    generator isn't simulating requests, so it needs the value exactly as
    written at the top of the file instead.
14. **Build the route table (`RouteTable`).** Reads every `@app.route(...)`
    decorator already sitting in the Python AST and maps each view function's
    name to its URL pattern (plus Flask's built-in `/static/<filename>`), so
    `url_for(...)` calls can be resolved to real paths.
15. **Evaluate each page (`StaticPageGenerator`).** For every discovered
    `render_template` call site, the module context is merged with that call's
    literal keyword arguments and handed to a dedicated AST walker that
    *evaluates* rather than re-emits: for-loops are unrolled, if/elif/else is
    collapsed to the one branch that runs, `{{ expr }}` is substituted
    (HTML-escaped, matching Jinja2's default autoescape), common filters and
    `url_for(...)` are resolved, and anything genuinely request-time-only
    (a looked-up `product`, flash messages) or unsupported (`extends`/
    `include`) becomes a visible, logged HTML comment — never a guess, never
    a crash. One page's failure doesn't abort the batch.
16. **Copy support files + write analysis artifacts.** `app.py` and every
    file under `tests/static/` are copied through byte-for-byte into
    `output/` (they are support files, not part of the transformation).
    `compiler_output/` gets both ASTs as JSON, a semantic-analysis report,
    and a generation log explaining what became what and what couldn't be
    statically resolved.

See `docs/STATIC_HTML_GENERATION.md` for the full design rationale and the
per-node evaluation rules.

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
│   │   ├── resolver/               #   PythonResolver: binds reads, detects undefined
│   │   │                         #   variables, tracks compile-time constant values
│   │   └── printer/                #   ASTPrinter (tree dump; also to String)
│   │
│   ├── jinja2/                   # TEMPLATE PIPELINE
│   │   ├── models/                #   Template AST (TemplateFile, HTML element nodes,
│   │   │                         #   statement nodes for if/for/set/macro/block/extends/
│   │   │                         #   include, expression nodes, literals)
│   │   ├── visitor/                #   AntlrToTemplateAstVisitor: parse tree → AST
│   │   ├── symbol_table/           #   Symbol (declared-value ExpressionNode + shallow
│   │   │                         #   SymbolType, PLUS the Resolver's own additive
│   │   │                         #   resolvedValue/usageLines — see §3 note below),
│   │   │                         #   Scope, SymbolTable (binding map), SymbolTableBuilder
│   │   │                         #   (declares + resolves reads), CompilerError,
│   │   │                         #   semantic_rules/ (UlLiRule, TypeCheckerRule,
│   │   │                         #   SemanticContext, ISemanticRule)
│   │   ├── resolver/                #   TemplateResolver: report facade over the
│   │   │                         #   builder's already-captured resolution state
│   │   └── printer/                #   ASTPrinter (tree dump; also to String)
│   │
│   ├── css/                       # auxiliary CSS AST/symbol-table pipeline — teaching
│   │                              # material, not part of the main compile() path
│   │
│   ├── errors/                   # UNIFIED ERROR-HANDLING FRAMEWORK
│   │   ├── CompilerStage.java     #   PARSING | SEMANTIC_ANALYSIS | CODE_GENERATION | IO
│   │   ├── CompilerException.java #   base of ParseError / SemanticError / CodeGenError
│   │   ├── CompilerProblem.java   #   one normalized, printable error entry
│   │   ├── ErrorReporter.java     #   central collector; adopts both pipelines' native
│   │   │                         #   error types; formats one report grouped by stage
│   │   │                         #   (printed to console AND reused verbatim inside
│   │   │                         #   compiler_output/semantic_report.txt)
│   │   └── SyntaxErrorListener.java # ANTLR listener: collects syntax errors, never lets
│   │                              #   a broken parse tree reach the next stage
│   │
│   ├── resolver/                 # SHARED VALUE MODEL
│   │   ├── ConstantValue.java     #   INT|FLOAT|STRING|BOOL|NONE|LIST|DICT|UNKNOWN — a
│   │   │                         #   compile-time-provable value, used by both resolvers,
│   │   │                         #   the template folding feature, and the static generator
│   │   └── PythonLiteralEvaluator.java # shared literal-matching logic (Python AST node →
│   │                              #   ConstantValue), used by PythonResolver AND by
│   │                              #   codegen.ModuleContextExtractor
│   │
│   ├── codegen/                  # CODE GENERATION + ORCHESTRATION
│   │   ├── PythonCodeGenerator.java     # Python AST → executable Python text
│   │   ├── TemplateCodeGenerator.java   # Template AST → HTML/Jinja2 text — LIVE templates
│   │   │                               # (regenerates Jinja, for generated/), plus an
│   │   │                               # opt-in folding mode for the preview files
│   │   ├── ModuleContextExtractor.java  # Python AST → initial top-level literal values
│   │   ├── RouteTable.java              # Python AST → {view function name → URL pattern}
│   │   ├── StaticPageGenerator.java     # Template AST → PURE, final HTML — every
│   │   │                               # {% %}/{{ }} evaluated and substituted, none left
│   │   ├── AstJsonWriter.java           # dependency-free AST → JSON, for compiler_output/
│   │   └── FlaskProjectGenerator.java   # the end-to-end driver described in §2
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
├── generated/                    # EXECUTABLE Flask project (git-ignored, rebuilt every run)
│   ├── app.py                    #   executable Flask backend
│   ├── templates/*.html          #   live templates Flask renders per request
│   ├── rendered/*.html           #   compile-time-folded previews (see §2.3 step 9)
│   ├── static/*                  #   copied through unchanged
│   └── compiler_report.txt       #   AST + Symbol Table + Resolver, for every file
│
├── output/                       # PURE STATIC SITE (git-ignored, rebuilt every run)
│   ├── <route-name>.html         #   one final, Jinja-free page per render_template call
│   ├── app.py                    #   support file, copied through unmodified
│   └── static/*                  #   CSS/JS, copied through unmodified
│
├── compiler_output/               # ANALYSIS ARTIFACTS (git-ignored, rebuilt every run)
│   ├── ast_python.json            #   the Python AST, as JSON
│   ├── ast_jinja.json             #   every analyzed template's AST, as JSON, keyed by name
│   ├── semantic_report.txt        #   files analyzed + every semantic problem found
│   └── generation_log.txt         #   what became what + every value the static generator
│                                   #   could not prove and therefore left visibly unresolved
│
└── docs/
    ├── CODEGEN.md                    # AST-node → target-language mapping tables for the
    │                                 # executable-app path (PythonCodeGenerator/TemplateCodeGenerator)
    ├── RESOLVER_AND_BYTECODE.md      # architecture review, Resolver design rationale,
    │                                 # Symbol Table extensions, the bytecode question in full
    ├── STATIC_HTML_GENERATION.md     # the static-HTML path: architecture review, resolver/
    │                                 # generator walkthrough, output structure, bytecode restated
    ├── PROJECT_OVERVIEW.md           # this document
    └── PROJECT_OVERVIEW_AR.md        # Arabic explanation
```

**Note on the Jinja2 `Symbol` class:** it now carries fields from two
independently-developed features that were later reconciled by a merge:
`origin/main`'s own type-checking work added `ExpressionNode value` (the raw
declared initializer) and a shallowly-inferred `SymbolType`; this branch's
Resolver work added `ConstantValue resolvedValue`/`getResolvedValue()` (a
*different* thing — an evaluated compile-time constant) and `usageLines`,
under different names specifically so the two coexist without colliding. See
`docs/RESOLVER_AND_BYTECODE.md` §3 for the full before/after.

---

## 4. Objectives → implementation map

Every requirement given across all rounds of work, matched to exactly where
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
| Symbol Table: extend with name/type/value/scope/declaration location/usages, reusing the existing implementation | Both `Symbol` classes gained `value`/`resolvedValue` (`ConstantValue`) and `usageLines`, additively | `python/symbol_table/Symbol.java`, `jinja2/symbol_table/Symbol.java`, `resolver/ConstantValue.java` |
| Code Generator: produce executable Python | unchanged generator, now fed a resolved AST | `codegen/PythonCodeGenerator.java` |
| Code Generator: "fully rendered HTML using resolved values" | Round 2: safe, scoped compile-time branch folding per render-call-site literal (`generated/rendered/*.html`). **Superseded/completed in Round 4**: full evaluation with zero Jinja left (`output/*.html`) | `codegen/TemplateCodeGenerator.java`, `codegen/StaticPageGenerator.java` |
| Output structure resembling `app.py` + template files + CSS/JS | `generated/app.py`, `generated/templates/*.html`, `generated/rendered/*.html`, `generated/static/*` | §3 above |
| Readable AST / Symbol Table / Resolver output, showing declaration/scope/type/value per variable | `generated/compiler_report.txt` | `FlaskProjectGenerator.writeReport`, `ASTPrinter.treeToString(...)` on both sides |
| Full bytecode explanation (what it is, who generates it, CPython's process, the PVM, whether it's in scope, where it belongs in the architecture, the AST/SymbolTable/Resolver/CodeGen/Bytecode/PVM relationship) | complete, dedicated section | `docs/RESOLVER_AND_BYTECODE.md` §6 |
| Recommendation: stop at Python/HTML, or continue to bytecode? | **Stop** — justified against the project's own educational objectives, restated and strengthened in Round 4 | `docs/RESOLVER_AND_BYTECODE.md` §6, `docs/STATIC_HTML_GENERATION.md` §6 |
| Constraints: keep the architecture, don't rewrite unnecessarily, reuse AST/Visitor, explain decisions first | Every existing constructor/API kept working; new capability added via additive fields, an opt-in constructor parameter, and one small edit to `visitIdentifier` — not a rewrite anywhere | throughout; itemized in `docs/RESOLVER_AND_BYTECODE.md` |

### Round 3 — catching up with `origin/main`'s type-checking work

| Objective | Implementation | Where |
|---|---|---|
| Reconcile two independently-developed Jinja2 type-checking designs after merging `origin/main` | Adopted main's `TypeCheckerRule`/`SymbolType`/`SemanticContext` (a separate, pluggable, richer pass); retired this branch's own `TypeChecker.java`/`Type.java` (confirmed unused elsewhere first) | `jinja2/symbol_table/semantic_rules/TypeCheckerRule.java`, merge history |
| Retrofit the Resolver's own value/usage tracking onto the new `Symbol` shape without colliding with main's own `value`/`type` fields | New, distinctly-named `resolvedValue`/`getResolvedValue()` fields, additive | `jinja2/symbol_table/Symbol.java` |
| Fix a pre-existing break on `origin/main`'s tip (`HTMLApp.java` importing a deleted package) | Deleted the now-fully-dead `HTMLApp.java` and the `src/html/*` package it referenced | merge history |

### Round 4 — pure static HTML generation, no Jinja at runtime

| Objective | Implementation | Where |
|---|---|---|
| Two cooperating stages: Python/Flask data processing + Jinja→HTML processing | `ModuleContextExtractor`/`RouteTable` (Python side) + `StaticPageGenerator` (Jinja side), orchestrated by `FlaskProjectGenerator.generateStaticSite()` | `src/codegen/*` |
| A "Context Data Extraction" step distinct from the Resolver | `ModuleContextExtractor` looks only at top-level literals, ignoring what a route handler does at request time — explicitly justified against `PythonResolver`'s different, whole-program-safe invalidation behavior | `codegen/ModuleContextExtractor.java`, `docs/STATIC_HTML_GENERATION.md` §1 |
| Fully evaluate Jinja: if/elif/else, for-loops, variable substitution | for-loops unrolled with a synthetic `loop` dict, if/elif/else collapsed to the one true branch, `{{ expr }}` substituted and HTML-escaped | `codegen/StaticPageGenerator.java` |
| One Jinja template → one generated HTML file | one `output/<route-name>.html` per `render_template` call site | `FlaskProjectGenerator.generateStaticSite` |
| `compiler_output/`: `ast_python.json`, `ast_jinja.json`, `semantic_report.txt`, `generation_log.txt` | all four, JSON-validated | `codegen/AstJsonWriter.java`, `FlaskProjectGenerator.writeCompilerOutputArtifacts` |
| Support files (`app.py`, `style.css`, `script.js`) copied unmodified into the output, not transformed | `copySupportAssets()` copies `app.py` → `output/app.py` and everything under `tests/static/` → `output/static/` | `FlaskProjectGenerator.copySupportAssets` |
| Symbol Table not used during generation | `StaticPageGenerator` never touches `jinja2.symbol_table.Symbol`; consumes only a plain `Map<String, ConstantValue>` | `codegen/StaticPageGenerator.java`, `docs/STATIC_HTML_GENERATION.md` §1 |
| Data prepared in Python first, then passed to Jinja | context built (`ModuleContextExtractor` + literal render args) before the template evaluator ever runs | `FlaskProjectGenerator.generateStaticSite` |
| Error handling for this stage: resolver/generation failures must not crash | per-page try/catch (one page's failure logs and moves on) + per-expression "unknown + logged comment" fallback, never a thrown exception for an unresolvable value | `codegen/StaticPageGenerator.java`, `docs/STATIC_HTML_GENERATION.md` §5 |
| Bytecode explanation, restated for an explicitly "pure HTML/CSS/JS" target | full section, conclusion strengthened (bytecode has even less of a role once the target is static files served with no Python process running) | `docs/STATIC_HTML_GENERATION.md` §6 |

---

## 5. Verified, not assumed

- **Live app unaffected.** `generated/app.py` and `generated/templates/index.html`
  are unaffected by every later round of work (the Resolver, the merge, and
  the static-HTML stage). Confirmed with a full Flask test-client run every
  round: `GET /`, `/products`, `/product/<id>`, `/add`, a `POST /add` that
  shows up on the products page, a `GET /delete/<id>` that actually removes
  the item and shows its flash message once, and a 404 served by the
  generated handler.
- **The Resolver catches a real class of bug the old pipeline could not.**
  `if totally_unknown_name > 0: ...` now correctly fails with
  `[UNDEFINED_VARIABLE] ... Undefined variable 'totally_unknown_name'` —
  previously silent, because nothing walked the condition.
- **`TypeCheckerRule` (from the merge) is genuinely wired and firing**, not
  just present: `{% set x=5 %}{% set y="hi" %}{{ x+y }}` produces
  `[TYPE_MISMATCH] ... Operator PLUS cannot be applied to NUMBER and STRING`.
- **The static-HTML stage was exercised on the real project, not just unit
  logic**: `output/view_products.html` shows the product grid genuinely
  unrolled (three `<div class="product-card">` blocks, not a loop), with
  `url_for(...)`-resolved links (`/product/1`, `/delete/2`, ...) and
  `"%.2f"|format(...)`-formatted prices, and **zero** `{% %}`/`{{ }}` left
  in any of the four generated pages (checked with a grep pass). The one
  page with genuinely request-time data (`product_details`, where `product`
  comes from a runtime lookup) honestly renders a visible, logged HTML
  comment instead of fabricating a product.
- **The spec's own worked example was reproduced exactly**: a from-scratch
  test project with separate `index.jinja`/`add_product.jinja`/`edit_product.jinja`
  files and `{% for product in products %}<h3>{{ product.name }}</h3>{% endfor %}`
  produced exactly `<h3>Phone</h3><h3>Laptop</h3>` in `output/index.html`,
  with `output/app.py` copied byte-identical (`diff` confirmed) and both
  `style.css`/`script.js` copied into `output/static/`.
- **Graceful degradation was tested, not just claimed**: an unsupported
  filter (`{{ title|weirdfilter }}`) resolves to an empty string with a
  logged line in `generation_log.txt`, and the whole compile still succeeds
  — one bad expression never aborts the run.

---

## 6. Known limitations (stated honestly, not hidden)

- **Constant propagation is a single forward pass, not dataflow analysis.**
  A value is "the most recently observed literal assignment in program
  text order" — sound (never wrongly claims a value), but not complete
  (can miss values a real dataflow pass would catch).
- **Inherited block-scoping model.** `SymbolTableBuilder` (and, by design,
  `PythonResolver` mirroring it for consistency) opens a new scope per
  `if`/`for`/`while`, even though real Python has no block scoping. A nested
  reassignment can therefore be recorded against a new inner-scope symbol
  instead of updating the enclosing one — e.g. `get_max_product_id`'s
  `max_id` looks more "constant" in the report than it truly is at runtime.
  Deliberately left alone rather than changed silently (fixing it means
  changing the builder's scoping, a larger, separate change).
- **Attribute-expression sub-parsing loses line numbers.** Identifiers inside
  an HTML attribute value (`href="{{ url_for(...) }}"`) are re-parsed from an
  isolated one-line string, so their usage lines all report as `1` in the
  resolver output. Pre-existing in `AntlrToTemplateAstVisitor`, surfaced —
  not introduced — by the new usage tracking.
- **`{% extends %}`/`{% include %}` are not evaluated by the static generator**
  — logged and left as a visible comment. Supporting them needs a second,
  cross-template resolution pass, which the real project doesn't exercise.
- **Static-site filters are bounded to the common ones** (`length`, `format`,
  `upper`/`lower`/`title`/`capitalize`/`trim`/`default`); anything else
  degrades to a logged empty value rather than a crash.
- **A pre-existing, unrelated float-precision artifact** shows up in
  `generation_log.txt`'s raw value dump (e.g. `999.989990234375` instead of
  `999.99`): `python.models.atom_statement.FloatAtom` stores its value as a
  Java `float`, not a `double`. This does not affect the actual generated
  HTML (`"%.2f"|format(...)` still prints `999.99` correctly), only the raw
  log display.
- **miniFlask is a subset.** No classes, `try/except`, `lambda`, f-strings,
  comprehensions, `{% with %}`. Comments are discarded by the lexers.
- **No bytecode generation**, by deliberate recommendation — see
  `docs/RESOLVER_AND_BYTECODE.md` §6 and `docs/STATIC_HTML_GENERATION.md` §6.

---

## 7. How to build and run

```bash
# compile
javac -encoding UTF-8 -cp antlr-4.13.2-complete.jar -d out $(find src -name "*.java")

# run the compiler (Main.compile())
java -cp "out;antlr-4.13.2-complete.jar" Main

# run the compiled executable app
python generated/app.py            # → http://127.0.0.1:5000

# or just open the pure static pages directly — no Python process needed
#   output/index.html, output/view_products.html, ...
```

Inspect `generated/compiler_report.txt` for the full AST/Symbol
Table/Resolver dump of the executable-app path, `generated/rendered/*.html`
for the compile-time template-evaluation previews, and
`compiler_output/generation_log.txt` for exactly what the static-HTML stage
resolved versus what it honestly couldn't.
