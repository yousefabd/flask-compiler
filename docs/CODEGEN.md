# miniFlask Code Generation & Backend Error Handling

The compiler translates a **miniFlask** source program (a Flask backend
`app.py` + the Jinja2/HTML templates it renders) into an executable target
project. The AST built by the existing visitors is the intermediate
representation; the code generator walks it and emits **Python** (backend) and
**HTML/Jinja2** (frontend) files.

```
  tests/app.py ────► PythonLexer/Parser ───► Python AST ───► SymbolTableBuilder ──┐
                       (SyntaxErrorListener)                  (semantic checks)   │
                                                                                  ├──► FlaskProjectGenerator
  tests/templates/*.html ► HTMLLexer/Parser ► Template AST ► SymbolTableBuilder ──┘          │
                       (SyntaxErrorListener)                  (Flask-aware seeding)          ▼
                                                                            generated/app.py
                                                                            generated/templates/*.html
                                                                            generated/static/*  (copied)
```

Entry point: `Main.compile()` → `codegen.FlaskProjectGenerator.generate()`.

Run it, then run the output: `python generated/app.py`.

---

## 1. Backend error-handling design (`errors` package)

| Class | Role |
|---|---|
| `CompilerStage` | Which stage produced an error: `PARSING`, `SEMANTIC_ANALYSIS`, `CODE_GENERATION`, `IO`. |
| `CompilerException` | Abstract base for all recoverable pipeline failures (stage + file + line). |
| `ParseError` / `SemanticError` / `CodeGenError` | Stage-specific exceptions thrown inside the pipeline. |
| `CompilerProblem` | One normalized, printable error entry (`[KIND] file line N: message`). |
| `ErrorReporter` | Central collector. Adopts native error types from both sub-pipelines (`jinja2.symbol_table.CompilerError`, `python.symbol_table.CompilerError`) plus exceptions, prints one report grouped by stage. |
| `SyntaxErrorListener` | ANTLR listener attached to every lexer/parser; collects syntax errors instead of printing to stderr and continuing with a broken tree. |

**The error boundary** is `FlaskProjectGenerator.generate()`:

* syntax errors → collected by `SyntaxErrorListener`; parsing returns `null`, no AST is built from a broken tree;
* semantic errors → collected by the two `SymbolTableBuilder`s (never thrown);
* generation/I-O failures → thrown as `CodeGenError`, caught at the boundary;
* any unexpected `RuntimeException` → caught last-resort and reported as `INTERNAL`.

The outcome is always either *generated files + no errors* or *no files + a
report*. The compiler never crashes on bad input, and the report format
matches the existing Jinja2 semantic-error display (`[KIND] line N: message`).

### New Python-side semantic checks

`python.symbol_table.CompilerError` mirrors the Jinja2 one. The Python
`SymbolTableBuilder` now collects (old constructor still works):

* `DUPLICATE_FUNCTION` — function name defined twice in one scope
* `DUPLICATE_PARAMETER` — repeated parameter name
* `RETURN_OUTSIDE_FUNCTION`, `BREAK_OUTSIDE_LOOP`, `CONTINUE_OUTSIDE_LOOP`
* `GLOBAL_AT_MODULE_LEVEL` — a `global` declaration that has no effect

### Flask-aware template analysis

Before analyzing a template, the generator seeds its symbol table with:

1. names Flask injects into every render (`url_for`, `request`,
   `get_flashed_messages`, `session`, `config`, `g`, …);
2. the keyword arguments the backend actually passes:
   `render_template('index.html', page='home', products=products)` defines
   `page` and `products` for `index.html`.

So "undefined variable" checking works *across* the Python/HTML boundary, and
only templates actually referenced by a `render_template` call are compiled.

### Runtime error handling in the generated app

When the source program uses a Flask app (`@app.route` seen), the Python
generator inserts a support block before the `if __name__ == "__main__"`
guard:

* `@app.errorhandler(404)` — friendly not-found page;
* `@app.errorhandler(Exception)` — passes `HTTPException`s through, renders a
  500 page for real crashes instead of a bare traceback.

---

## 2. AST-node → target-language mapping

### Python AST (`python.models`) → Python (`codegen.PythonCodeGenerator`)

| AST node | Stores | Generated Python |
|---|---|---|
| `Program` | top-level statements | whole module, statements in order (+ error-handler block) |
| `SimpleStatement` | list of small statements | one line, joined with `; ` |
| `ExpressionStatement` | conditions, assigns, `=` flag | `a, b = x, y` or bare expression |
| `AugAssignStatement` | id, op, expr | `x += expr` (all 12 augmented operators) |
| `ReturnStatement` | conditions | `return a, b` |
| `Pass/Break/ContinueStatement` | — | `pass` / `break` / `continue` |
| `GlobalStatement` | names | `global a, b` |
| `SimpleImportStatement` | dotted name | `import a.b` |
| `FromImportStatement` | dotted name, targets, `*` | `from a.b import x, y` / `import *` |
| `IfStatement` | conditions, bodies, else | `if/elif/else` blocks, 4-space indent |
| `WhileStatement` | condition, body, else | `while cond:` (+ `else:`) |
| `ForStatement` | iterators, iterable, body, else | `for i, j in expr:` (+ `else:`) |
| `DecoratorStatement` | decorators + funcdef | `@app.route('/…')` lines + `def` (detects Flask usage) |
| `FunctionDef` / `Parameter` | id, params, return type, body | `def f(a: T = d) -> R:` |
| `Body` | statements | indented block; empty body → `pass` |
| `CompoundCondition` | and/or/not | `x and y`, `not x` — nested operands parenthesized to preserve AST grouping |
| `RelationalComparison` | op, left, right | `==  !=  <  >  <=  >=  in  not in  is  is not` |
| `BinaryExpression` | op, left, right | `+ - * / // % ** @ & \| ^ << >>` |
| `UnaryExpression` | op, expr | `+x  -x  ~x` |
| `IDTrailer` + `Trailer` | id + call/subscript/attr chain | `obj.attr(args)[index]` |
| `CallArguments` / `Argument` | args, keyword flag | `f(a, k=v)` |
| `SubscriptArguments` | conditions | `x[i, j]` |
| `ID`, `IntegerAtom`, `FloatAtom`, `BoolAtom`, `StringAtom`, `None` | literal value | `name`, `42`, `3.14`, `True/False`, raw string token (quotes preserved), `None` |
| `ParenAtom` | inner expr | `( expr )` — explicit source parentheses survive |
| `List` / `Set` / `Dictionary` | elements / key-value pairs | `[a, b]` / `{a, b}` / `{k: v}` |

### Template AST (`jinja2.models`) → HTML/Jinja2 (`codegen.TemplateCodeGenerator`)

| AST node | Stores | Generated HTML |
|---|---|---|
| `TemplateFile` | content nodes | whole `.html` file |
| `HtmlTextNode` | raw text | emitted verbatim (formatting preserved) |
| `OutputNode` | expression | `{{ expr }}` |
| `HTMLNormalElementNode` | tag, attributes, children | `<div …>children</div>` |
| `HTMLVoidElementNode` | tag, attributes | `<img …/>` (real void tags); `<textarea …></textarea>` otherwise |
| `HtmlAttributeNode` | name, value parts | `name="text{{ expr }}text"`; no parts → boolean attribute (`required`) |
| `ForStatementNode` | variable, iterable, body | `{% for x in items %} … {% endfor %}` |
| `IfStatementNode` / `IfBranchNode` | branches | `{% if %} {% elif %} {% else %} {% endif %}` |
| `SetStatementNode` | variable, value/body | `{% set x = expr %}` or `{% set x %}…{% endset %}` |
| `MacroStatementNode` / `ParameterNode` | name, params, body | `{% macro m(a, b=1) %}…{% endmacro %}` |
| `BlockStatementNode` | name, body | `{% block name %}…{% endblock %}` |
| `ExtendsStatementNode` / `IncludeStatementNode` | path | `{% extends "base.html" %}` / `{% include "x.html" %}` |
| `IdentifierNode` / literals | name / value | `name`, `'str'` (quotes preserved), `42`, `true/false`, `none` |
| `BinaryExpressionNode` / `UnaryExpressionNode` | op, operands | `a == b`, `not x` — nested binaries parenthesized |
| `PropertyAccessNode` / `IndexAccessNode` | target, member/index | `product.name` / `items[0]` |
| `CallExpressionNode` / `ArgumentNode` | callee, args | `url_for('index', id=1)` |
| `FilterExpressionNode` | target, filter, args | `products\|length`, `"%.2f"\|format(p.price)` |
| `ListExpressionNode` / `DictionaryExpressionNode` | elements / pairs | `[1, 2]` / `{'k': v}` |

Any node or operator without a mapping raises `CodeGenError` with the node
name and source line — reported, never a crash.

---

## 3. Output structure

```
generated/
├── app.py               ← executable Flask backend (+ generated error handlers)
├── templates/
│   └── index.html       ← one file per template referenced by render_template
└── static/
    └── styles.css       ← static assets copied through unchanged
```

---

## 4. How the generator works end to end

1. **Parse the backend.** `tests/app.py` goes through `PythonLexer/Parser`
   with a `SyntaxErrorListener`; `PythonVisitor` builds the Python AST.
2. **Check the backend.** The Python `SymbolTableBuilder` builds scopes and
   collects semantic errors.
3. **Discover the frontend.** The generator walks the Python AST for
   `render_template(...)` calls — that yields the set of templates to compile
   and the context variables each one receives.
4. **Parse + check each template.** Each referenced `.html` is parsed to a
   Template AST and analyzed with the existing Jinja2 symbol
   table/type-checker, seeded with Flask builtins + the discovered context.
5. **Gate.** If *any* stage reported errors, generation is skipped and the
   `ErrorReporter` prints one grouped report.
6. **Generate.** `PythonCodeGenerator` re-emits the module as executable
   Python (injecting the Flask runtime error handlers), and
   `TemplateCodeGenerator` re-emits each template as HTML/Jinja2. Static
   assets are copied. The result runs directly: `python generated/app.py`.
