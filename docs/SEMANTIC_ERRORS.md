# Python semantic analysis

This mirrors the existing Jinja2 semantic-analysis design for the Python side
of the compiler. It reports name, scope and type errors in `app.py`, plus one
cross-language error that compares what a template needs against what the
routes actually pass to `render_template(...)`.

---

## 1. How to run the tests

From the project root:

```bat
run_semantic_tests.bat
```

or manually:

```bat
dir /s /b src\*.java > build\sources.txt
javac --release 21 -d build\classes -cp "lib\antlr4-runtime-4.13.2.jar;lib\gson-2.14.0.jar" @build\sources.txt
java -cp "build\classes;lib\antlr4-runtime-4.13.2.jar;lib\gson-2.14.0.jar" semantic_tests.SemanticTestRunner
```

The suite exits with status `1` when anything fails, so it can gate a build.
It must be run from the project root, because two cases read the real
`tests/app.py` and `tests/templates`.

Expected output:

```
Undefined variable
  PASS  an unresolvable name is reported
  ...
────────────────────────────────────────────────────────────
34 passed, 0 failed
```

There is no test framework on the classpath, so the suite is a plain `main`
(`src/semantic_tests/`). Every case asserts the **complete** set of reported
error kinds, not just the presence of the expected one. That is what makes an
extra false positive — or an unexpected `INTERNAL` failure — fail the run
instead of slipping through. Exceptions escaping a case are caught and
reported as `INTERNAL`.

### Seeing one error by hand

The compiler only prints; it has no other output channel. To watch a single
error, edit `tests/app.py`, add a bad line, and run the compiler:

```bat
java -cp "build\classes;lib\antlr4-runtime-4.13.2.jar;lib\gson-2.14.0.jar" Main url_test
```

For example adding `print(missing_name)` at module level prints:

```
Compilation failed:
Semantic Errors:
  [UNDEFINED_VARIABLE] tests\app.py line 5: Undefined variable 'missing_name'
```

and compilation stops — the rendered HTML is never produced.

---

## 2. The errors

### 2.1 Required

| Kind | Meaning |
|---|---|
| `UNDEFINED_VARIABLE` | The name cannot be resolved through the legal visible Python scope chain. |
| `SCOPE` | The name exists, but only in a scope that is not reachable from here. |
| `TYPE_ERROR` | Operand types are statically known and the operation is provably invalid. |
| `TYPE_MISMATCH` | A value contradicts an explicit type expectation (a parameter annotation). |
| `MISSING_FLASK_VARIABLE` | A template reads a variable that no `render_template()` call supplies. |

**Undefined variable** — builtins are never reported:

```python
print(missing_name)      # UNDEFINED_VARIABLE
print(len([1, 2]))       # fine — print and len are builtins
```

**Scope** — functions create scopes; `if`, `for` and `while` do not:

```python
def create_value():
    secret = 10

print(secret)            # SCOPE: declared in function 'create_value'
```

```python
def compute(flag, items):
    if flag:
        chosen = 1
    for item in items:
        last = item
    print(chosen, last, item)   # all fine — no block scope in Python
```

The old project treated `if`/`for`/`while` as scopes. That behaviour was not
preserved.

**Type error** — only when both operand types are proven:

```python
value = 5 + "hello"      # TYPE_ERROR: unsupported operand types for +: 'int' and 'str'
number = 10
first = number[0]        # TYPE_ERROR: 'int' object is not subscriptable
total = 5
total()                  # TYPE_ERROR: 'int' object is not callable
```

When a type is unknown it becomes `ANY` and the check is skipped. Nothing is
guessed in order to produce an error:

```python
def handle(payload, count):
    combined = payload + "text"   # payload is a parameter — ANY, no error
    item = payload[0]             # no error
```

**Type mismatch** — explicit expectations only, i.e. parameter annotations:

```python
def set_age(age: int):
    pass

set_age("twenty")        # TYPE_MISMATCH: parameter 'age' expects 'int' but received 'str'
```

Ordinary Python rebinding is **not** a mismatch:

```python
x = 1
x = "text"               # legal Python — accepted
```

A variable assigned more than once resolves to `ANY` rather than to the type
of its first assignment, so the first assignment never fixes the type.

**Missing Flask variable** — see section 4.

### 2.2 Bonus errors

Three beyond the required five, all Python-specific:

| Kind | Why it is a distinct error |
|---|---|
| `USE_BEFORE_ASSIGNMENT` | Python's `NameError` / `UnboundLocalError`. Different from undefined: the name *does* exist in this scope, it just has not been assigned yet at that point. |
| `DUPLICATE_FUNCTION` | Two `def`s with the same name in one scope — the second silently replaces the first. |
| `DUPLICATE_PARAMETER` | `def f(value, value)` is a hard `SyntaxError` in CPython. |

```python
print(declared)          # USE_BEFORE_ASSIGNMENT — exists, not assigned yet
print(never_declared)    # UNDEFINED_VARIABLE   — does not exist at all
declared = 1
```

A loop body is exempt, because a name assigned near its end is already bound
on the next iteration:

```python
for item in items:
    print(carry)         # accepted
    carry = item
```

`DUPLICATE_FUNCTION` and `DUPLICATE_PARAMETER` **already existed** in
`python/symbol_table/SymbolTableBuilder`. Nothing new was added for them — they
were previously untested and now have coverage. See the note in section 6.

---

## 3. Jinja errors deliberately *not* ported

The Jinja analyzer reports nine kinds. These were not carried over, because
they are not errors in Python:

| Jinja kind | Why it is not a Python error |
|---|---|
| `DUPLICATE_VARIABLE` | Jinja rejects a duplicated loop variable. Rebinding a name is completely normal Python (`x = 1` then `x = 2`). |
| `DUPLICATE_MACRO` | Python has no macros. The nearest concept, a redefined function, is already `DUPLICATE_FUNCTION`. |
| `DUPLICATE_BLOCK` | `{% block %}` is a template inheritance feature with no Python counterpart. |
| `INVALID_HTML_STRUCTURE` | HTML-only (the `<ul>`/`<li>` rule). |

One kind was deliberately given a **different meaning** on the Python side:

- Jinja reports an invalid binary operand pair as `TYPE_MISMATCH`.
- Python reports it as `TYPE_ERROR`, because that is what CPython raises
  (`TypeError`), and `TYPE_MISMATCH` is reserved for a value contradicting an
  explicit annotation.

The Jinja error classes themselves were **not** modified to resemble the
Python ones.

---

## 4. Missing Flask variable

Free variables are collected from the parsed Jinja AST and compared against
the keyword context of the `render_template(...)` calls found in the Python
AST. Both sides are structured compiler data — no variable name is ever
recovered by parsing a human-readable error message.

Excluded from "required", because the template declares them itself:

- Jinja builtins (`url_for`, `request`, `session`, `config`, `g`, `range`,
  `dict`, `namespace`, `get_flashed_messages`) — the list is
  `TemplateFrontend.TEMPLATE_BUILTINS`, referenced directly so it cannot drift
- loop variables, plus the implicit `loop` object
- macro names and macro parameters
- `{% set %}` targets
- names guarded by `{% if x is defined %}`

### Documented limitation

`index.html` is intentionally rendered from four routes with different `page`
values and different context arguments, and a variable like `product` is only
read inside the branch for one of those pages. Reporting every variable that
any *individual* call fails to supply would therefore be wrong.

This first implementation is deliberately conservative: **a variable is
reported only when no `render_template()` call for that template supplies
it.** A variable supplied by one route is consequently accepted for all of
them. Narrowing this further needs per-branch reachability analysis of the
template against each call's `page` value, which is out of scope here.

---

## 5. Architecture

```
Python AST
    -> name resolution and bindings   (python.semantic.NameResolver)
    -> type checking                  (python.semantic.semantic_rules.TypeCheckerRule)
    -> semantic errors                (python.symbol_table.CompilerError)

Python TemplateCall data + Jinja free-variable data
    -> missing Flask variable errors  (compiler.semantic.MissingFlaskVariableAnalyzer)
```

| File | Role |
|---|---|
| `python/semantic/NameResolver.java` | Scopes, bindings, `UNDEFINED_VARIABLE`, `SCOPE`, `USE_BEFORE_ASSIGNMENT` |
| `python/semantic/PyScope.java` | Module and function scopes only |
| `python/semantic/Binding.java` | One declared name and what was proved about it |
| `python/semantic/PythonType.java` | Static types, with `ANY` for "not provable" |
| `python/semantic/PythonBuiltins.java` | Names that are never undefined |
| `python/semantic/ResolutionResult.java` | Resolution output consumed by type checking |
| `python/semantic/semantic_rules/` | `ISemanticRule` / `SemanticContext` / `TypeCheckerRule` |
| `python/semantic/PythonSemanticAnalyzer.java` | Orchestrates the two passes |
| `compiler/semantic/TemplateFreeVariableCollector.java` | Free variables of a Jinja template |
| `compiler/semantic/MissingFlaskVariableAnalyzer.java` | Template needs vs. route context |

The layout deliberately parallels `jinja2/symbol_table/` and
`jinja2/symbol_table/semantic_rules/`.

The analyzer is **read-only** with respect to the AST and every other stage.
It does not evaluate Python values, does not statically interpret Python, adds
no render-request provider or provider fallback, does not touch CPython
execution, render-context generation, Jinja rendering or expression
evaluation, and generates no JavaScript.

Errors reach the shared `errors.ErrorReporter` through the existing
`report(String file, python.symbol_table.CompilerError)` adapter, so Python
and Jinja problems print in one report in the same
`[KIND] file line N: message` format.

**Stopping code generation.** `CompilationPipeline` returns immediately after
`analyzeSemantics(...)` when the reporter has errors, and again after the
missing-Flask-variable check. That is the analyzer's only interaction with
code generation. Two tests cover it: one asserts the render provider is never
called for a program with a semantic error, and a control case asserts a clean
program *does* reach it — so the test proves the stop is caused by the errors
rather than by the pipeline halting unconditionally.

---

## 6. Audit

### 6.1 The lexer still tokenizes Python booleans in lowercase

`grammars/python/PythonLexer.g4` has:

```antlr
TRUE:  'true';     // line 29
FALSE: 'false';    // line 31
NONE:  'None';     // line 37  <- already correct
```

Python spells these `True` and `False`. `NONE` is already right, which makes
the inconsistency clearer. The required correction is:

```antlr
TRUE:  'True';
FALSE: 'False';
```

**Treated as a separate, minimal lexer fix and not applied here**, and the
generated ANTLR files under `src/antlr/python/` were **not** regenerated —
the generation command and ANTLR version still need to be verified first
(`pom.xml` pins the *runtime* at 4.13.2, but no generation plugin is
configured, so the tool version used to produce the committed sources is not
recorded anywhere).

Current consequence: `True` and `False` in `tests/app.py` lex as identifiers
rather than `BoolAtom`. The analyzer is unaffected either way — `True`,
`False` and `None` are in `PythonBuiltins`, so they resolve cleanly today, and
once the lexer is fixed they become `BoolAtom` and type as `BOOL`. No test
depends on which of the two happens.

### 6.2 Two points in the brief that conflict — how they were resolved

**Duplicate errors.** The opening request asks for bonus errors and names
"a duplicate thing and parameter function". The rules section then says not to
add duplicate-declaration errors. These were reconciled by adding **nothing**:
`DUPLICATE_FUNCTION` and `DUPLICATE_PARAMETER` already exist in
`python/symbol_table/SymbolTableBuilder`, so they are kept as-is and simply
given test coverage. No duplicate-route, argument-count, return-placement or
break-placement error was added.

**Reassignment as a type mismatch.** Implemented per the rule as stated:
`x = 1` then `x = "text"` is legal and never reported, and a variable with
more than one assignment types as `ANY`. If the rubric or a professor-provided
example turns out to require the non-Python "first assignment fixes the type"
behaviour, this is a single change in
`TypeCheckerRule.inferVariableType(...)` — please confirm before it is made.

### 6.3 Existing behaviour left alone

`python/symbol_table/SymbolTableBuilder` still opens scopes for `if`, `for`
and `while`. That table is only used for its declaration-level checks and its
return value is discarded by the pipeline, so those scopes no longer drive any
name-resolution result — `NameResolver` owns scoping and models Python
correctly. The builder was left untouched rather than rewritten, since the
analyzer must not modify other compiler stages. Removing the now-cosmetic
scopes from it would be a reasonable, separate cleanup.

---

## 7. Test coverage

| # | Case | Covers |
|---|---|---|
| 1 | An unresolvable name is reported | `UNDEFINED_VARIABLE` |
| 2 | Builtin names are not undefined | no false positives on builtins |
| 3 | A name defined later in the module is visible from a function | deferred function bodies |
| 4 | A variable local to another function is a scope error | `SCOPE` |
| 5 | One function cannot see another function's local | `SCOPE` |
| 6 | `if`/`for`/`while` do not create a scope | correct Python scoping |
| 7 | A module-level global stays visible inside a function | `global` |
| 8 | Reading a local before it is assigned | `USE_BEFORE_ASSIGNMENT` |
| 9 | Use before assignment is distinct from undefined | the two are not conflated |
| 10 | A name assigned later in a loop body is not reported | no false positive |
| 11 | `5 + "hello"` | `TYPE_ERROR` |
| 12 | Indexing an int | `TYPE_ERROR` |
| 13 | Calling a non-callable | `TYPE_ERROR` |
| 14 | An unknown runtime type produces no guessed error | `ANY` handling |
| 15 | Valid operations on known types are accepted | no false positives |
| 16 | Comparing a str with an int | `TYPE_ERROR` |
| 17 | An annotated parameter given the wrong known type | `TYPE_MISMATCH` |
| 18 | A keyword argument checked against its annotation | `TYPE_MISMATCH` |
| 19 | A correctly typed argument is accepted | numeric tower |
| 20 | An unannotated parameter is never a mismatch | explicit expectations only |
| 21 | An argument of unknown type is not reported | `ANY` handling |
| 22 | Normal reassignment to a different type is legal | not a mismatch |
| 23 | A reassigned variable becomes unknown rather than wrong | no fixed first type |
| 24 | A function defined twice | `DUPLICATE_FUNCTION` |
| 25 | A duplicated parameter name | `DUPLICATE_PARAMETER` |
| 26 | Distinct functions and parameters are accepted | no false positives |
| 27 | A context variable no route supplies | `MISSING_FLASK_VARIABLE` |
| 28 | Loop, set and macro locals are not missing variables | template-local names |
| 29 | A variable supplied by any route satisfies every route | documented limitation |
| 30 | `is defined` does not demand a context variable | optional context |
| 31 | The real `tests/app.py` analyzes without false errors | whole project |
| 32 | The real templates report no missing Flask variables | whole project |
| 33 | Semantic errors stop compilation before code generation | end to end |
| 34 | A clean program does reach code generation | control for #33 |
