# Building, Running and Testing

## Quick start

```bat
build.bat
run.bat Main
```

That compiles everything in `src\` and renders a route of `tests\app.py` to stdout.
No Maven, no Python virtualenv needed.

## build.bat

Compiles `src\**\*.java` into `out\`.

The two dependencies declared in `pom.xml` — `antlr4-runtime` and `gson` — are
fetched from Maven Central into `lib\` on first run and cached there. `lib\` is
gitignored, exactly like the `.venv\` that `setup_environment.bat` creates, so no
binaries are committed. Later builds reuse them and run offline.

`pom.xml` still works if you have Maven; `build.bat` exists so the project builds
without it.

## run.bat

Runs a class against the last build.

```bat
run.bat Main                             render the default route (url_test)
run.bat Main view_products               render one route by function name
run.bat tests.AllTests                   every test suite
run.bat tests.PythonErrorTests           Python semantic error suite
run.bat tests.PythonErrorTests --show    ... and print every report
run.bat tests.StaticRenderTests          render-context suite
```

## Regenerating the ANTLR sources

The generated lexers/parsers are committed under `src\antlr\`, so a normal build
never runs ANTLR. After editing a grammar in `grammars\`, regenerate with the same
tool version that produced the committed files — **4.13.2**:

```bat
curl -sSLO https://www.antlr.org/download/antlr-4.13.2-complete.jar

REM lexer (writes PythonLexer.java/.tokens/.interp)
java -jar antlr-4.13.2-complete.jar -Dlanguage=Java -o src\antlr\python grammars\python\PythonLexer.g4

REM parser — -visitor is required, the project uses the visitor API
java -jar antlr-4.13.2-complete.jar -Dlanguage=Java -visitor -o src\antlr\python -lib src\antlr\python grammars\python\PythonParser.g4
```

The `@header` block in each grammar supplies the `package antlr.python;`
declaration, so no `-package` flag is needed, and `superClass = MidLexBase` keeps
the hand-written [MidLexBase.java](../src/antlr/python/MidLexBase.java) as the
lexer's base class — do not delete that file, it is not generated.

Regenerate the parser too whenever a token's **literal text** changes: the parser
embeds the literal names for its syntax-error messages. Token *numbering* only
changes if you add, remove or reorder lexer rules.

Before trusting a regeneration, run the tool against the **unmodified** grammar
first and diff the result against `src\antlr\` — the output should be byte for byte
identical. If it is, the invocation is faithful and any later diff is genuinely
your grammar change.

## Test suites

| Suite | Covers | Cases |
|---|---|---|
| `tests.PythonErrorTests` | Python semantic errors — see [PYTHON_ERRORS.md](PYTHON_ERRORS.md) | 18 |
| `tests.StaticRenderTests` | compile-time render contexts, routes, flash, provider fallback | 8 |
| `tests.AllTests` | all of the above | 26 |

Each exits `0` only when everything passes, so a build can gate on
`run.bat tests.AllTests`.

---

## What the compiler analyzes

Both front ends run on every compilation and report through the same
`ErrorReporter`:

| Front end | Input | Produces |
|---|---|---|
| `PythonFrontend` | `tests\app.py` | AST, symbol table, semantic errors |
| `TemplateFrontend` | `tests\templates\*.html` | one AST + symbol table per rendered template |

The Python symbol table prints after analysis alongside the Jinja ones. Builtins
are left out of the printed Python table — they are injected by the front end,
identical on every run, and would bury the program's own symbols.

---

## How a render context is produced

To render a template, the compiler needs the values a route passes to
`render_template(...)`. There are two ways to get them, tried in order by
`FallbackTemplateRenderRequestProvider`:

**1. `StaticTemplateRenderRequestProvider` — prove them.**
Folds the call's arguments straight from the Python AST. No Python process
involved. It handles:

- literals — strings, ints, floats, booleans, `None`
- collections of literals — lists, sets, dicts
- arithmetic and concatenation over folded values — `unit_price * quantity`
- names bound to any of the above, at module level or earlier in the same function

It also derives the Flask runtime information the templates ask for:

- `url_for(...)` routes from the `@app.route` decorators, including the argument
  names inside `<int:product_id>`, plus Flask's built-in `static` endpoint
- `get_flashed_messages()` from literal `flash(...)` calls in the route body

**2. `CPythonTemplateRenderRequestProvider` — execute them.**
Runs the route under CPython and captures the real context as JSON. Needs
`.venv\` (`setup_environment.bat`).

Folded values use the same Java types the CPython path produces through Gson —
`Long` for integers, `Double` for floats, `String`, `Boolean`, `null`, `List`,
`Map` — which is what makes the two providers interchangeable.

### When folding refuses

The static provider fails rather than guess. A half-proven context would produce
HTML the real app never serves, which is worse than falling back. It refuses when
a value comes from a function call, an attribute, or a subscript, and when a
`flash()` appears inside an `if` or a loop — whether that message is shown depends
on which branch runs, and only executing the route can settle it.

If both providers fail, both reasons are reported:

```
[CODE_GENERATION] tests\app.py line 75: No render context could be produced for function 'product_details':
    - StaticTemplateRenderRequestProvider: Cannot build the render context of 'index.html'
      at compile time: 'product' is not a compile-time constant
    - CPythonTemplateRenderRequestProvider: Could not start CPython for function 'product_details'
```

### What renders today

Against `tests\app.py`, with no `.venv` present:

| Route | Result |
|---|---|
| `index`, `view_products`, `filter_test`, `flash_test`, `url_test` | renders |
| `product_details` | needs CPython — context is `find_product_by_id(product_id)` |
| `add_product` | needs CPython — `flash()` runs inside the POST branch |
| `delete_product` | no `render_template` call; it returns a redirect |

Set up `.venv` with `setup_environment.bat` and the first two fall through to
CPython and render as well.

### Known limitation

The conditional-`flash()` refusal is conservative. In `add_product` the `flash()`
is on the POST path while the `render_template` is on the GET path, so no message
can actually be pending — but establishing that needs control-flow analysis of
which blocks reach the call. Until that exists, the provider declines and CPython
handles it.
