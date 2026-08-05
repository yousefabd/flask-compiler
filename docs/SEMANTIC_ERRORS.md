# Python semantic analysis

This mirrors the existing Jinja2 semantic-analysis design for the Python side
of the compiler. It reports name, scope and type errors in `app.py`, plus one
cross-language error that compares what a template needs against what the
routes actually pass to `render_template(...)`.

This document was revised after an adversarial review found several confirmed
defects in the first version. Section 6 is the full record of what was found
and how each was fixed; the rest of the document describes the corrected,
current behaviour.

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
------------------------------------------------------------
56 passed, 0 failed
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
| `TYPE_MISMATCH` | A value contradicts an explicit type expectation (an annotation). |
| `MISSING_FLASK_VARIABLE` | A template reads a variable that no `render_template()` call supplies. |

**Undefined variable** — builtins are never reported:

```python
print(missing_name)      # UNDEFINED_VARIABLE
print(len([1, 2]))       # fine — print and len are builtins
print(hex(10))           # fine — the builtin list is fairly wide; see PythonBuiltins
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

Resolution is **flow-insensitive**: a name declared anywhere in a scope
resolves everywhere in that scope, regardless of textual position. This
under-approximates CPython, which can raise `UnboundLocalError` for a read
that precedes its assignment on the branch or iteration actually taken:

```python
def compute(flag):
    if flag:
        value = 1
    print(value)          # no error here, even though flag=False raises at runtime
```

Reporting that correctly needs control-flow analysis (which branch ran, which
iteration). An earlier version of this analyzer approximated it with an
execution-order-tracking error kind, `USE_BEFORE_ASSIGNMENT`; it was removed
after review found it wrong in both directions — see §6.1.

**Type error** — only when both operand types are proven:

```python
value = 5 + "hello"      # TYPE_ERROR: unsupported operand types for +: 'int' and 'str'
number = 10
first = number[0]        # TYPE_ERROR: 'int' object is not subscriptable
total = 5
total()                  # TYPE_ERROR: 'int' object is not callable
found = 1 in "abc"       # TYPE_ERROR: 'in <string>' requires a string left operand
value = 5 @ 2            # TYPE_ERROR: no built-in type supports '@'
```

When a type is unknown it becomes `ANY` and the check is skipped. Nothing is
guessed in order to produce an error:

```python
def handle(payload, count):
    combined = payload + "text"   # payload is a parameter — ANY, no error
    item = payload[0]             # no error
```

`and`/`or` (Python's logical operators) do **not** force a `bool` result: they
short-circuit and return one of the operand values itself —
`"" or "fallback"` evaluates to the string `"fallback"`. The checker types the
result as the common type of both operands when they agree, `ANY` otherwise
(mirroring how Jinja's own `TypeCheckerRule` handles `AND`/`OR`), never
unconditionally `BOOL`:

```python
value = "" or "fallback"
combined = value + "!"    # fine — value is STRING, not a guessed BOOL
```

`not` still always yields a real `bool`.

**Type mismatch** — explicit expectations only. This grammar has three: a
parameter annotation checked against a call argument, a parameter annotation
checked against its own default value, and a return annotation checked
against a `return` value.

```python
def set_age(age: int):
    pass

set_age("twenty")            # TYPE_MISMATCH: parameter 'age' expects 'int'

def set_age(x: int = "x"):   # TYPE_MISMATCH: default value contradicts annotation
    pass

def get_id() -> int:
    return "x"                # TYPE_MISMATCH: return value contradicts annotation
```

A bare `return` (implicitly `None`) and a tuple `return a, b` are not checked
against a scalar annotation — both would need a more expressive annotation
model than this grammar's bare-name annotations support.

Ordinary Python rebinding is **not** a mismatch:

```python
x = 1
x = "text"               # legal Python — accepted
```

A variable assigned more than once resolves to `ANY` rather than to the type
of its first assignment, so the first assignment never fixes the type. The
same conservative treatment applies when a name is declared as *both* a
function and a plain variable in the same scope (`def convert(): ...` then
`convert = 3`, or the reverse) — see §6.3.

**Missing Flask variable** — see section 4.

### 2.2 Bonus errors

Four beyond the required five, all real CPython `SyntaxError`s, all
pre-existing in `python/symbol_table/SymbolTableBuilder` before this work
started (previously untested):

| Kind | Why it is a distinct error |
|---|---|
| `DUPLICATE_PARAMETER` | `def f(value, value)` is a hard `SyntaxError` in CPython. |
| `RETURN_OUTSIDE_FUNCTION` | `return` outside a function body — a hard `SyntaxError`. |
| `BREAK_OUTSIDE_LOOP` | `break` outside a loop — a hard `SyntaxError`. |
| `CONTINUE_OUTSIDE_LOOP` | `continue` outside a loop — a hard `SyntaxError`. |

Two siblings that were also in that builder were **removed** after review —
they flagged completely legal Python, not errors — see §6.2:

- `DUPLICATE_FUNCTION` (redefining a function is ordinary name rebinding)
- `GLOBAL_AT_MODULE_LEVEL` (`global x` at module level is redundant, not illegal)

---

## 3. Jinja errors deliberately *not* ported

The Jinja analyzer reports nine kinds. These were not carried over, because
they are not errors in Python:

| Jinja kind | Why it is not a Python error |
|---|---|
| `DUPLICATE_VARIABLE` | Jinja rejects a duplicated loop variable. Rebinding a name is completely normal Python (`x = 1` then `x = 2`). |
| `DUPLICATE_MACRO` | Python has no macros. Redefining a function is legal Python (see §6.2), not an error. |
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

### Why this never double-reports with Jinja's own analysis

Jinja's own template analysis is fed the exact same *union* of every call's
context arguments (`CompilationPipeline.collectContextVariables`), so it
already reports its own `UNDEFINED_VARIABLE` for any name absent from that
union — precisely the condition this check also looks for. Running both
unconditionally would report the same root cause twice under two different
error kinds (confirmed by review — see §6.7). `CompilationPipeline` now stops
as soon as Jinja's own analysis finds anything, before this check runs, so
`MISSING_FLASK_VARIABLE` only ever fires when Jinja found nothing for any
template — meaning it never overlaps with a Jinja error.

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
| `python/semantic/NameResolver.java` | Scopes, bindings, `UNDEFINED_VARIABLE`, `SCOPE` |
| `python/semantic/PyScope.java` | Module and function scopes only; detects function/variable rebinding |
| `python/semantic/Binding.java` | One declared name and what was proved about it |
| `python/semantic/PythonType.java` | Static types, with `ANY` for "not provable" |
| `python/semantic/PythonBuiltins.java` | Names that are never undefined |
| `python/semantic/ResolutionResult.java` | Resolution output consumed by type checking, functions keyed by `Binding` |
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
`analyzeSemantics(...)` when the reporter has errors, and again after Jinja's
own template analysis, and again after the missing-Flask-variable check. That
is the analyzer's only interaction with code generation. Two tests cover it:
one asserts the render provider is never called for a program with a semantic
error, and a control case asserts a clean program *does* reach it.

---

## 6. Review record

An adversarial review (`important-semantic-findings.md`) ran the analyzer
against inputs beyond the original test suite and found eight confirmed
issues. All eight were fixed; each has a dedicated regression test in
`SemanticTestRunner`'s "Adversarial regressions" section, alongside the
existing suite. The full before/after mechanics are in
`docs/SEMANTIC_ANALYSIS_CHANGES.md`; this section is the short version.

### 6.1 `USE_BEFORE_ASSIGNMENT` was wrong in both directions — removed

The execution-order approximation accepted a loop-carried read
(`for x in xs: print(carry); carry = x` — a real `UnboundLocalError` on
iteration one) and had no way to catch a name assigned in only one `if`
branch. Rather than patch a check that was confidently wrong on findable
inputs, it was removed along with its flow-tracking code (`PyScope`'s
`assignedSoFar`/`markAssigned`/`isAssigned`, `NameResolver`'s
`preAssignLoopBindings`). See §2.1 for the resulting, explicitly
flow-insensitive behaviour.

### 6.2 The legacy builder flagged two kinds of legal Python

`python/symbol_table/SymbolTableBuilder` reported `DUPLICATE_FUNCTION` for a
redefined function and `GLOBAL_AT_MODULE_LEVEL` for `global x` at module
level. Neither is a CPython error: redefinition is ordinary name rebinding,
and `global` at module level is redundant (the module namespace already is
the global namespace) but legal. Both error-reporting branches were removed;
the underlying `define()`/`declareGlobal()` calls were kept.

### 6.3 Rebinding a name between function and variable was modeled wrong

`PyScope.declare()` kept the *first* declaration of a name outright, so:

```python
def convert(value: int):
    pass

convert = 3
convert()          # analyzer: silent (should catch this — but see below)
```

```python
convert = 3

def convert(value: int):
    pass

convert("text")    # analyzer: TYPE_ERROR "'int' object is not callable" (wrong — this succeeds)
```

The second case was a real false positive. Fixed by having `PyScope.declare()`
mark a `Binding` **rebound** when it sees a function/variable clash for the
same name in the same scope; a rebound binding types as `ANY` regardless of
which kind it started as. This does not make the *first* example detect its
real `TypeError` (that needs execution-order modeling, out of scope) — it
makes both examples equally, honestly "unknown" instead of one being silently
accepted and the other being confidently wrong.

### 6.4 Functions were looked up by name, not by resolved binding

`ResolutionResult` stored `Map<String, FunctionDef>`. Two functions sharing a
name in different scopes — a nested `def` shadowing a module-level one — could
therefore have the *wrong* function's parameter annotations checked against a
call that actually targets the other:

```python
def helper(x: int):
    pass

def outer():
    def helper(y: str):
        pass
    helper("ok")    # was checked against x: int (wrong) — false TYPE_MISMATCH
```

Fixed by keying the map on the resolved `Binding` (`IdentityHashMap<Binding,
FunctionDef>`) instead of the bare name — the same `Binding` `NameResolver`
already resolved for that specific call site, so the function looked up is
exactly the one that call actually reaches.

### 6.5 Confirmed type-checker gaps

| Input | Was | Now |
|---|---|---|
| `value = "" or "fallback"; value + "!"` | `TYPE_ERROR` (`or` typed unconditionally `BOOL`) | Accepted (`or` types as the common operand type) |
| `5 @ 2` | No error | `TYPE_ERROR` |
| `1 in "abc"` | No error | `TYPE_ERROR` (string containment requires a string left operand) |
| `hex(10)` | `UNDEFINED_VARIABLE` | Accepted (`hex`, `oct`, `bin`, and ~30 other builtins added) |
| `def f() -> int: return "x"` | No mismatch | `TYPE_MISMATCH` |
| `def f(x: int = "x")` | No mismatch | `TYPE_MISMATCH` |

The claim that parameter annotations are the grammar's only explicit type
expectation was also wrong — the grammar supports return annotations too, now
checked (§2.1).

### 6.6 Dotted imports bound the wrong name

`import os.path` was binding `path` (`.getLast()` on the dotted name) instead
of `os`, which is what Python actually binds. Fixed in both
`NameResolver` and the legacy `SymbolTableBuilder` (`.getFirst()`).

### 6.7 One missing Flask value produced duplicate diagnostics

Confirmed and fixed — see §4, "Why this never double-reports with Jinja's own
analysis".

### 6.8 A star import crashed the legacy builder

`from math import *` parses with `FromImportStatement.targets == null` (see
`PythonVisitor.visitFromImport`); `SymbolTableBuilder.visitImportStatement`
dereferenced it unconditionally, throwing a `NullPointerException` that
propagated as an `INTERNAL` compiler error. Fixed with a null check;
`NameResolver`'s own handling of star imports already had one.

### 6.9 The lexer still tokenizes Python booleans in lowercase

Unrelated to the review above — a pre-existing audit finding, carried
forward. `grammars/python/PythonLexer.g4` has:

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

### 6.10 Two points in the original brief that conflict — how they were resolved

**Duplicate errors.** The opening request asked for bonus errors and named "a
duplicate thing and parameter function"; the rules section said not to add
duplicate-declaration errors. Reconciled by adding **nothing new**:
`DUPLICATE_PARAMETER` already existed and is kept (a real `SyntaxError`).
`DUPLICATE_FUNCTION` also already existed but was **removed** after review
confirmed it flags legal Python, not because of the original conflict. No
duplicate-route, argument-count, return-placement or break-placement error was
added.

**Reassignment as a type mismatch.** Implemented per the rule as stated:
`x = 1` then `x = "text"` is legal and never reported, and a variable with
more than one assignment types as `ANY`. If the rubric or a professor-provided
example turns out to require the non-Python "first assignment fixes the type"
behaviour, this is a single change in
`TypeCheckerRule.inferVariableType(...)` — please confirm before it is made.

---

## 7. Test coverage

56 cases in `SemanticTestRunner`, organized into ten groups matching the
`main` method's print order:

| Group | Cases | Covers |
|---|---|---|
| Undefined variable | 3 | `UNDEFINED_VARIABLE`, builtins never flagged, deferred function bodies |
| Scope | 4 | `SCOPE`, `if`/`for`/`while` not creating scope, `global` |
| Flow-insensitive resolution | 3 | The documented §2.1 trade-off — loop-carry, `if`-branch, plain forward reference |
| Type error | 6 | `5 + "hello"`, indexing, calling, `ANY` skip, valid ops, ordering comparison |
| Type mismatch | 12 | Parameter/keyword/return/default-value checks, `ANY` skip, rebinding is not a mismatch |
| Legacy declaration-placement | 5 | `DUPLICATE_PARAMETER`, `RETURN_OUTSIDE_FUNCTION`, `BREAK_OUTSIDE_LOOP`, `CONTINUE_OUTSIDE_LOOP` |
| Missing Flask variable | 4 | `MISSING_FLASK_VARIABLE`, template-local exclusions, the multi-route limitation, `is defined` |
| Adversarial regressions | 15 | One test per confirmed review finding (§6.1–§6.8) |
| Whole-project | 4 | Real `tests/app.py`/`tests/templates`, end-to-end stop-before-codegen with a control case |

Every case asserts the complete set of reported kinds (not just "contains the
expected one"), so an extra false positive fails the run. An exception
escaping a case is caught and reported as `INTERNAL` rather than silently
passing — this is what caught the star-import crash and the `ArrayDeque`
null-element bug introduced while fixing the return-annotation check, both
during this same review pass.
