# Python Error Handling

This document describes the Python-side error handling: what it detects, where the
errors appear in the compiler output, how to run the tests, and how to trigger each
error by hand.

It is the Python counterpart of the Jinja2 error handling and deliberately mirrors
it — same error record shape, same collection strategy, same rule pipeline, same
`ErrorReporter` output. Nothing about the existing Jinja2 system was replaced.

---

## 1. Where the pieces live

| Concern | Python | Jinja2 (mirrored) |
|---|---|---|
| Error record | `python/symbol_table/CompilerError.java` | `jinja2/symbol_table/CompilerError.java` |
| Static types | `python/symbol_table/SymbolType.java` | `jinja2/symbol_table/SymbolType.java` |
| Declarations + name resolution | `python/symbol_table/SymbolTableBuilder.java` | `jinja2/symbol_table/SymbolTableBuilder.java` |
| Rule interface / context | `python/symbol_table/semantic_rules/` | `jinja2/symbol_table/semantic_rules/` |
| Type checking | `semantic_rules/TypeCheckerRule.java` | `semantic_rules/TypeCheckerRule.java` |
| Flask route rule | `semantic_rules/FlaskRouteRule.java` | `semantic_rules/UlLiRule.java` |
| Frontend wiring | `python/PythonFrontend.java` | `jinja2/TemplateFrontend.java` |
| Backend/template contract | `compiler/template/TemplateContextChecker.java` | — |
| Reporting | `errors/ErrorReporter.java` (shared) | same |

Nothing new was introduced at the architecture level: the Python analysis now runs
the same three phases the Jinja2 analysis already ran.

---

## 2. How analysis runs

`SymbolTableBuilder.build(Program)` runs three phases:

1. **declare** — walk the AST, open and close scopes, declare every name, and
   record every name that is *read* together with the scope it was read in;
2. **resolve** — with the whole module now known, bind each recorded read to its
   declaration and report `UndefinedError` / `ScopeError` / `NameError`;
3. **rules** — run each `ISemanticRule` (`TypeCheckerRule`, `FlaskRouteRule`)
   against the finished table.

Reads are resolved in phase 2 rather than inline because *"used before it was
declared"* can only be told apart from *"declared in an enclosing scope further
down the file"* once the entire module has been seen. A function body that refers
to a module-level name defined later in the file is legal Python and is **not**
reported; a module-level statement that does the same thing is.

Every read that resolves is also recorded as a **binding** (`SymbolTable.recordBinding`),
which is what lets `TypeCheckerRule` ask "what type is this identifier?" without
redoing scope resolution — exactly the mechanism the Jinja2 type checker uses.

---

## 3. Errors detected

Each error carries **kind**, **message**, **line**, **scope/context** and the
**variable/function/template name** when one applies.

| Error name | Kind | Triggered by |
|---|---|---|
| `UndefinedError` | `UNDEFINED_VARIABLE` | a name that exists in no scope at all |
| `NameError` | `USE_BEFORE_DECLARATION` | a name declared in *this* scope, further down |
| `ScopeError` | `SCOPE` | a name that exists, but not on this scope chain |
| `TypeError` | `TYPE_ERROR` | an operation the value's type does not support |
| `TypeMismatchError` | `TYPE_MISMATCH` | a value whose type contradicts a declaration or annotation |
| `MissingFlaskVariableError` | `MISSING_FLASK_VARIABLE` | a template variable no `render_template` call supplies |
| `DuplicateDeclarationError` | `DUPLICATE_VARIABLE` | a name declared twice in the same scope |
| `DuplicateFunctionError` | `DUPLICATE_FUNCTION` | two `def`s with the same name in one scope |
| `DuplicateParameterError` | `DUPLICATE_PARAMETER` | a repeated parameter name |
| `DuplicateRouteError` | `DUPLICATE_ROUTE` | two `@app.route` decorators on the same URL |
| `ArgumentCountError` | `ARGUMENT_COUNT` | a call with too few or too many arguments |
| `ReturnOutsideFunctionError` | `RETURN_OUTSIDE_FUNCTION` | `return` at module level |
| `BreakOutsideLoopError` | `BREAK_OUTSIDE_LOOP` | `break` outside a loop |
| `ContinueOutsideLoopError` | `CONTINUE_OUTSIDE_LOOP` | `continue` outside a loop |
| `GlobalScopeError` | `GLOBAL_AT_MODULE_LEVEL` | `global` where it has no effect |

### Two rules worth knowing

**Duplicate declaration vs. type mismatch.** Both are reported at a second
assignment to the same name in the same scope; which one you get depends on the
types:

```python
x = 1
x = 2          # DuplicateDeclarationError — same type
```
```python
x = 10
x = "text"     # TypeMismatchError — int then str
```

Assignments Python semantics make legitimate *rebindings* rather than new
declarations are exempt: a name pulled in with `global`, a parameter reassigned
inside its own function, and a shadowed builtin. Without those exemptions
`tests/app.py` (which reassigns the module-level `products` under `global`) would
not compile.

**Everything unknown is silent.** Any type that resolves to `Any` — a call result,
an attribute, a subscript — disables the check that needed it. The checkers only
report what they can prove, so incomplete inference never becomes a false error.

---

## 4. Output

Python errors go through the existing `ErrorReporter`, so they land in the same
report, in the same format, in the **Semantic Errors** section:

```
Compilation failed:
Semantic Errors:
  [TypeMismatchError] tests\errors\broken_app.py line 9: Expected str, got int for 'title' (declared at line 8) (in global > function index)
  [UndefinedError] tests\errors\broken_app.py line 10: Variable 'missing_name' is not defined (in global > function index)
  [UNDEFINED_VARIABLE] tests\templates\index.html line 10: Undefined variable 'product'
  [MissingFlaskVariableError] tests\errors\broken_app.py line 10: 'product' was not passed to render_template('index.html') (in function 'index')
```

The format is `[Kind] file line N: message (in scope)`. Python kinds print under
their **error name** (`UndefinedError`, `ScopeError`, ...) so the report reads the
way Python names these failures; Jinja2 kinds keep printing under their enum name
(`UNDEFINED_VARIABLE`), unchanged.

Syntax errors land in **Syntax Errors**, generation errors in **Code Generation
Errors**, as before.

---

## 5. Flask integration

Two things connect the error handling to the Flask side.

**`MissingFlaskVariableError`** (`compiler/template/TemplateContextChecker.java`)
compares what a template reads against what the backend passes. The same gap is
visible from both ends of the pipeline, and both are reported:

- the Jinja analysis reports it as an undefined variable **at the template line**;
- this checker reports it **at the `render_template(...)` call in `app.py`** — which
  is where the fix has to be made.

A name counts as supplied when **any** `render_template` call for that template
passes it, matching how `CompilationPipeline` unions the context of all calls before
analyzing a template. A template rendered from several routes is therefore only
flagged for names no route supplies at all.

**Nothing crashes the pipeline.** `PythonFrontend.analyzePython` wraps the analysis:
if a check itself fails, the failure is reported as an internal problem and every
error found so far is still reported. Above it, `CompilationPipeline.compileSnapshot`
already caught `CompilerException` and `RuntimeException` and funnelled them into the
reporter. Code generation is skipped whenever the reporter holds any error, so a
program with Python errors produces a report instead of broken output.

---

## 6. Running the tests

The suite lives in `src/tests/PythonErrorTests.java`. Every case feeds a small
program from `tests/errors/` through the **real** front end and checks the three
promises the error handling makes:

1. the compiler **does not crash** — any escaping exception fails the case;
2. the expected error **appears in the report**, with its error name and message;
3. it appears in the **correct section** (`Semantic Errors`).

```bat
build.bat
run.bat tests.PythonErrorTests
```

Add `--show` to print the full compiler report each case produced — the exact text
that appears in the output/report area:

```bat
run.bat tests.PythonErrorTests --show
```

See [BUILD_AND_RUN.md](BUILD_AND_RUN.md) for what the scripts do.

The runner prints `PASS`/`FAIL` per case, a summary line, and exits `0` only when
everything passes, so it can be wired into a build.

Two cases guard against over-reporting rather than under-reporting:

- **A correct program reports nothing** — `tests/errors/clean.py` uses the same
  patterns as `tests/app.py` (module-level state, `global`, loops, route handlers)
  and must produce `No errors.`
- **The whole pipeline survives a broken app** — runs the real
  `CompilationPipeline` over `tests/errors/broken_app.py` and asserts it returns
  normally, prints `Compilation failed:` and lists both Python errors.

---

## 7. Triggering each error by hand

Every sample below is a file in `tests/errors/`. To run one through the compiler
outside the suite, point `utils.CompilerSettings.appSource` at it and run `Main`,
or call `PythonFrontend.parsePython()` / `analyzePython()` directly the way
`PythonErrorTests.analyze()` does.

| File | Source | Expected report line |
|---|---|---|
| `undefined.py` | `print(x)` | `[UndefinedError] line 1: Variable 'x' is not defined` |
| `type_error.py` | `x = 5 + "hello"` | `[TypeError] line 1: Unsupported operand types for +: int and str` |
| `scope.py` | `if True:` / `    x = 10` / `print(x)` | `[ScopeError] line 3: Variable 'x' is out of scope here (declared at line 2)` |
| `type_mismatch.py` | `x = 10` / `x = "text"` | `[TypeMismatchError] line 2: Expected int, got str for 'x'` |
| `name_error.py` | `print(value)` / `value = 10` | `[NameError] line 1: Variable 'value' is used before it is declared at line 2` |
| `missing_flask_variable.py` | route passes `name`, template also reads `products` | `[MissingFlaskVariableError] line 8: 'products' was not passed to render_template('missing_variable.html')` |
| `duplicate_declaration.py` | `x = 1` / `x = 2` | `[DuplicateDeclarationError] line 2: Variable 'x' is already declared in this scope at line 1` |
| `duplicate_function.py` | two `def handler()` | `[DuplicateFunctionError] line 4: Function 'handler' is already defined in this scope` |
| `duplicate_parameter.py` | `def f(a, a):` | `[DuplicateParameterError] line 1: Duplicate parameter 'a' in function 'f'` |
| `duplicate_route.py` | two `@app.route('/products')` | `[DuplicateRouteError] line 11: Route '/products' is already handled by 'view_products'` |
| `argument_count.py` | `def add(a, b)` called as `add(1)` | `[ArgumentCountError] line 4: Function 'add' expects 2 argument(s), but 1 were given` |
| `not_callable.py` | `total = 5` / `total(3)` | `[TypeError] line 2: 'total' of type int is not callable` |
| `argument_type.py` | `def greet(name: str)` called as `greet(42)` | `[TypeMismatchError] line 4: Expected str, got int for parameter 'name' of 'greet'` |
| `index_access.py` | `count = 5` / `count[0]` | `[TypeError] line 2: Type int does not support index access for 'count'` |
| `augmented_undefined.py` | `counter += 1` | `[UndefinedError] line 1: Variable 'counter' is not defined` |

### Checking that an error was handled correctly

1. run the suite (`tests.PythonErrorTests`) — or `Main` against your own `app.py`;
2. read the report printed after `Compilation failed:`;
3. confirm the line appears under **Semantic Errors** with the right error name,
   line number and scope;
4. confirm the process exited normally and printed a report — no stack trace, no
   partially generated output.

---

## 8. Notes and limitations

- **`if` opens a scope.** This front end has always given `if` / `for` / `while`
  bodies their own scope, which is why `if True: x = 10` followed by `print(x)` is a
  `ScopeError` here. Real CPython has no block scope; the behaviour is kept as the
  project already defined it.
- **Type inference is shallow, by design.** Literals, parenthesized expressions,
  unary and binary operations, and plain names are inferred; call results, attributes
  and subscripts are `Any`. This keeps the checks provable and the report free of
  guesses.
- **A missing template variable is reported twice** — once by the Jinja analysis at
  the template line, once by the Python checker at the `render_template` call. That
  is intentional: they point at the two different places a reader may be looking.
