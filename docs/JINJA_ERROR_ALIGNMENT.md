# Aligning Jinja2 Errors with Python Errors

Commit `30ec69b`. This documents the change that gave the Jinja2 front end the same
structured error fields the Python front end already had, so the two report
identically.

---

## 1. The problem

The Python error handling was built to mirror the Jinja2 one, and along the way it
gained two things Jinja2 never had: the **scope** an error was found in, and the
**name** of the symbol it was about. Those came from the requirement that every
error carry "scope or context if available" and "the variable/function/template
name if relevant."

The result was a report that treated its two halves differently:

```
Semantic Errors:
  [TypeMismatchError] tests\errors\broken_app.py line 9: Expected str, got int for 'title' (declared at line 8) (in global > function index)
  [UndefinedError] tests\errors\broken_app.py line 10: Variable 'missing_name' is not defined (in global > function index)
  [UndefinedError] tests\templates\index.html line 10: Undefined variable 'product'
  [ScopeError] tests\templates\index.html line 74: Variable 'product' is not visible in this scope
```

Same section, same format, but the Python lines end with `(in ...)` and the template
lines just stop. A reader could reasonably assume the compiler knows less about
templates than it does about Python — it doesn't; the information was there, it was
simply never recorded.

Rather than strip the context from Python to match Jinja2, the poorer of the two
was raised to the richer.

---

## 2. What changed, file by file

Seven files. Two gained capability, five pass the new data through.

### 2.1 `jinja2/symbol_table/Scope.java` — the scope path

A scope knows its name and its parent, so the path is a walk up the chain:

```java
/** Fully qualified scope path, from the template scope down to this one. */
public String getQualifiedName() {
    if (parent == null) return name;
    return parent.getQualifiedName() + " > " + name;
}
```

This is a copy of `python.symbol_table.Scope.getQualifiedName()`, deliberately —
the two produce the same shape of string so the reports read the same.

Jinja2 scope names come from the existing `enterScope(...)` calls, so the paths that
can appear are:

| Path | From |
|---|---|
| `template` | the root scope |
| `template > for` | `{% for %}` |
| `template > macro card` | `{% macro card(...) %}` |
| `template > block content` | `{% block content %}` |
| `template > block content > for` | a loop inside a block |

Note that `{% if %}` does **not** open a scope in Jinja2 — the builder handles
branches without entering one — so an `if` never appears in a path. This differs
from the Python side, where `if` does open a scope; that difference is a property of
the two symbol tables, not of this change.

### 2.2 `jinja2/symbol_table/CompilerError.java` — the two fields

```java
private final String context;     // scope the error was found in, may be null
private final String symbolName;  // variable/macro/block name, may be null
```

The existing three-argument constructor was kept and now delegates:

```java
public CompilerError(Kind kind, String message, int line) {
    this(kind, message, line, null, null);
}

public CompilerError(Kind kind, String message, int line,
                     String context, String symbolName) { ... }
```

Keeping it means no existing caller had to change to compile, and any error that
genuinely has no scope can still say so by omission rather than by passing `null`
twice.

`toString()` appends the context only when there is one:

```java
sb.append('[').append(kind.errorName()).append("] line ")
  .append(line).append(": ").append(message);
if (context != null) sb.append(" (in ").append(context).append(')');
```

This matches `python.symbol_table.CompilerError.toString()` exactly.

### 2.3 `jinja2/symbol_table/SymbolTableBuilder.java` — recording the scope

The builder raised errors by constructing them inline in six places. Each one now
goes through a helper that fills in the scope automatically:

```java
private void error(CompilerError.Kind kind, String message, int line, String symbolName) {
    errors.add(new CompilerError(kind, message, line,
            symbolTable.getCurrentScope().getQualifiedName(), symbolName));
}
```

The helper is the point of the change. Passing the scope at each call site would
have worked, but it would also have been six chances to pass the wrong one — the
builder's current scope is only correct *at the moment the error is raised*, and it
moves as the walk enters and leaves blocks. Reading it inside the helper means it
can never drift from where the error actually happened.

The six sites and the name each now records:

| Error kind | Symbol recorded |
|---|---|
| `DUPLICATE_VARIABLE` | the loop variable name |
| `DUPLICATE_MACRO` | the macro name |
| `DUPLICATE_PARAMETER` | the parameter name |
| `DUPLICATE_BLOCK` | the block name |
| `SCOPE` | the identifier name |
| `UNDEFINED_VARIABLE` | the identifier name |

This mirrors `python.symbol_table.SymbolTableBuilder.error()`, which does the same
thing for the Python builder.

### 2.4 `semantic_rules/SemanticContext.java` — the rule-side helper

The Python rule package had an `error(...)` helper on its context record; the Jinja2
one did not. Added so both rule packages expose the same API:

```java
public void error(CompilerError.Kind kind, String message, int line,
                  String context, String symbolName) {
    errors.add(new CompilerError(kind, message, line, context, symbolName));
}
```

### 2.5 `semantic_rules/TypeCheckerRule.java` — delegating

Its private helper now routes through the context record instead of constructing
the error itself:

```java
private void error(SemanticContext ctx, CompilerError.Kind kind, String msg, int line) {
    ctx.error(kind, msg, line, null, null);
}
```

The `null, null` is deliberate and explained in §4.

### 2.6 `semantic_rules/UlLiRule.java` — recording the tag

Both of its errors are about an HTML tag, so the tag name is the symbol:

- `{{ }}` directly inside a list → records the offending list tag
- a non-`<li>` child of `<ul>`/`<ol>` → records the child's tag name

### 2.7 `errors/ErrorReporter.java` — passing it through

The reporter converts a front end's error into the shared `CompilerProblem`. The
Jinja2 overload dropped the context on the floor because there was none to pass;
now there is:

```java
public void report(String file, jinja2.symbol_table.CompilerError error) {
    problems.add(new CompilerProblem(
            CompilerStage.SEMANTIC_ANALYSIS,
            error.getKind().errorName(),
            file,
            error.getLine(),
            error.getMessage(),
            error.getContext()));      // ← added
}
```

`CompilerProblem` already supported a context field — it was added when the Python
errors needed one — so nothing changed there. This overload is now byte-for-byte the
same shape as the Python one.

---

## 3. The result

```
Semantic Errors:
  [TypeMismatchError] tests\errors\broken_app.py line 9: Expected str, got int for 'title' (declared at line 8) (in global > function index)
  [UndefinedError] tests\errors\broken_app.py line 10: Variable 'missing_name' is not defined (in global > function index)
  [UndefinedError] tests\templates\index.html line 10: Undefined variable 'product' (in template)
  [ScopeError] tests\templates\index.html line 74: Variable 'product' is not visible in this scope (in template)
  [MissingFlaskVariableError] tests\errors\broken_app.py line 10: 'product' was not passed to render_template('index.html') (in function 'index')
```

Every line now reads `[Kind] file line N: message (in scope)`. The **file path** is
what distinguishes a template problem from a Python one — which is the right
discriminator, because it is also where you go to fix it.

---

## 4. What stayed asymmetric, and why that is correct

**Errors raised by a semantic rule carry no scope — in both languages.**

`TypeCheckerRule` and `UlLiRule` walk the tree with their own traversal and do not
maintain a scope stack, so at the moment they raise an error there is no "current
scope" to read. The same is true of `python.symbol_table.semantic_rules.TypeCheckerRule`.

This is not a gap left unfinished; it is the two front ends behaving identically.
Giving the rules a scope would mean either threading a scope stack through every
rule traversal, or re-deriving the scope from the node — both are real work, and
neither is needed for the rules' errors to be actionable, because those errors are
about types and structure rather than about name visibility.

If it ever becomes worth doing, it should be done to **both** rule packages in one
change, or the asymmetry simply moves rather than disappears.

---

## 5. What this change does *not* do

- No error kind was added, removed or renamed.
- No check changed its behaviour — the same programs produce the same errors, on the
  same lines, with the same messages.
- No template or Python analysis logic moved.

It is purely about what gets *recorded* alongside an error and what gets *printed*.

---

## 6. Verification

```bat
build.bat
run.bat tests.AllTests
```

26 cases, all passing. The change is covered indirectly rather than by a dedicated
test: `PythonErrorTests`' pipeline case runs the real `CompilationPipeline` over a
broken app and asserts on the combined report, which contains both Python and Jinja2
errors — so a regression in the Jinja2 half shows up there.

---

## 7. Note for the `css-frontend` branch

That branch was created **before** this change and still has the old Jinja2
`CompilerError` and `SemanticContext`. Its own `css.symbol_table.CompilerError`
already follows the newer five-field shape, so merging is a matter of taking this
commit's versions of the Jinja2 files — there is no conflict of intent, only of
timing.
