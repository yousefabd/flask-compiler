# Pure Static HTML Generation — Architecture, Resolver, and Bytecode

This document covers the newest capability added to the miniFlask compiler:
producing **fully pre-rendered, Jinja-free static HTML** — `output/*.html` —
alongside the existing executable-Flask-app output (`generated/`), plus the
analysis artifacts under `compiler_output/`. It answers, in order: what was
missing from the architecture, how the resolver and generator work, what
each output folder contains, and — again — the bytecode question, now
restated against a goal that is explicitly "pure HTML/CSS/JS".

---

## 1. Architecture review — what was missing

The existing pipeline (Lexer → Parser → AST → Semantic Analysis → Symbol
Table → Resolver → Code Generator) was sufficient for its previous goal
(generate an *executable* Flask app, where Jinja must stay alive because
request data isn't known until a request arrives). Producing **final**
static HTML is a different goal, and needed exactly two new pieces — no more:

1. **A "Context Data Extraction" step, separate from the Resolver.**
   `python.resolver.PythonResolver` already tracks compile-time constant
   values — but it does so *conservatively for the whole program*: the moment
   it sees `products.append(...)` or `products = new_products` anywhere,
   even inside a route handler that only runs at request time, it correctly
   invalidates `products`'s tracked value (that's exactly right for its job:
   proving a value is safe to fold *at every point in the program*). A static
   site generator isn't simulating requests — it needs the value **as
   literally written at the top of the file**, ignoring what a route *might*
   later do to it. That's a genuinely different question, so it gets its own
   small, dedicated component: `codegen.ModuleContextExtractor`, which looks
   only at top-level assignment statements. This matches the project spec's
   own pipeline diagram, which lists "Context Data Extraction" as its own
   phase, separate from "Resolver".
2. **A route table**, so `url_for(...)` calls (used throughout the real
   template for every link and form action) can be resolved to concrete
   paths instead of being left as unresolvable calls, which would defeat the
   entire point of "no Jinja left at runtime". `codegen.RouteTable` reads the
   `@app.route(...)` decorators already sitting in the Python AST — this AST
   was already being walked for `render_template` discovery, so this is a
   small addition to code that already existed, not a new AST pass.

Everything else needed already existed and is reused as-is: the Python AST,
the Jinja2/HTML AST, `resolver.ConstantValue` (the same value model the
Python and Jinja2 resolvers already use), and the existing `render_template`
call-site discovery in `FlaskProjectGenerator`.

**Symbol Table's role is unchanged and deliberately narrow.** It answers
"is this name declared, in which scope, with what statically-inferred type"
— that's a semantic-analysis question. It is *not* asked to produce output:
`StaticPageGenerator` never touches `jinja2.symbol_table.Symbol` at all. It
consumes a plain `Map<String, ConstantValue>` — the *resolved* context —
built from `ModuleContextExtractor` + a call site's literal keyword
arguments. Keeping these separate means the symbol table's job (scope
correctness) can't accidentally get entangled with the generator's job
(byte-for-byte HTML output); the same separation the project already used
for the executable-app path (`SymbolTableBuilder` builds structure, the two
code generators build the text).

### Why this is a new class, not a mode flag on `TemplateCodeGenerator`

`TemplateCodeGenerator` already existed and does a real, tested job: it
regenerates **live** Flask templates, where preserving `{% if/for %}` is
*correct behavior*, not a limitation — the data genuinely isn't known until
a request arrives. Making it also fully evaluate loops/conditionals would
mean threading a growing pile of "am I in static mode?" checks through every
method, and risked regressing the already-verified executable-app path for a
completely different goal. `codegen.StaticPageGenerator` walks the exact
same AST types (`ForStatementNode`, `IfStatementNode`, `OutputNode`, ...)
with the same dispatch shape, but every method's *purpose* is inverted —
evaluate and substitute, not re-emit Jinja syntax — which is exactly the
kind of difference that justifies a separate class over a mode flag.

---

## 2. How Python data is extracted and resolved

`ModuleContextExtractor.extract(program)` walks only `Program.statements`
(never descending into function bodies) looking for bare `name = <literal>`
assignments, evaluating the right-hand side with `resolver.PythonLiteralEvaluator`
— the same literal-matching logic `PythonResolver` uses for its own constant
tracking, factored out into a shared utility so the two don't duplicate it.
For the real project this produces exactly one entry:

```
products = [{'id': 1, 'name': 'Laptop', 'price': 999.99, ...}, ...]
```

Each `render_template('name.html', k=v, ...)` call site (already discovered
by `FlaskProjectGenerator.collectRenderCalls` for the existing undefined-
variable seeding) contributes its own literal keyword arguments — `page='home'`,
`page='products'`, etc. The **page context** used to evaluate a given
template is simply: module context, with that call site's literal arguments
merged on top. `RouteTable.build(program)` is built once per compile and
handed to every page's generator, since `url_for` resolution doesn't depend
on which page is being generated.

---

## 3. How the Jinja AST is evaluated into static HTML

`StaticPageGenerator` walks the template AST with a small internal `Scope`
(a chain of `Map<String, ConstantValue>`, one per `{% for %}`/`{% set %}`
nesting level, resolving outward to the page context when a name isn't
local) and:

- **`{% for x in expr %}`** — evaluates `expr`; if it's a known list, the
  loop is *unrolled*: the body is generated once per element with `x` (and
  a synthetic `loop` dict exposing `index`/`index0`/`first`/`last`/`length`,
  matching Jinja's real magic variable) bound in a child scope, and the
  results are concatenated. If the iterable isn't resolvable, the loop is
  left as a visible, logged HTML comment rather than guessed at.
- **`{% if/elif/else %}`** — evaluates each condition with Python/Jinja
  truthiness rules (`None`/`False`/`0`/`""`/`[]`/`{}` are falsy); the first
  branch that resolves true wins, and *only that branch's body* is emitted —
  no wrapper survives. An unresolvable condition (e.g. `{% if product %}` on
  the product-details page, where `product` is a request-time lookup) again
  becomes a logged, visible comment instead of a guess.
- **`{{ expr }}`** — evaluated and substituted as text, HTML-escaped the
  same way Flask/Jinja2 auto-escapes `.html` templates by default.
- **`{% set %}`** — evaluated (or, for block-set, the rendered body captured
  as a string, matching real Jinja) and bound into the current scope.
- **Property/index access** (`product.name`, `items[0]`) — resolved against
  a known dict/list value; **filters** — `length`, `format` (Python
  `%`-style, e.g. `"%.2f"|format(price)`), `upper`/`lower`/`title`/
  `capitalize`/`trim`/`default` are supported directly; anything else is a
  logged, empty fallback. **`url_for(...)`** is resolved via `RouteTable`
  (including Flask's built-in `static` endpoint); **`get_flashed_messages()`**
  and `request`/`session`/`config`/`g` resolve to empty, since a static build
  has no HTTP request to serve. **Macros** are supported for the common case
  (positional/keyword/default parameter binding, body rendered as a string).
  **`{% extends %}`/`{% include %}`** (multi-template composition) are
  intentionally out of scope — the real project doesn't use them, and
  supporting them would mean a second, cross-template resolution pass; each
  occurrence is left as a clear, logged comment rather than silently ignored.

Verified on the real project's four pages (`output/index.html`,
`view_products.html`, `product_details.html`, `add_product.html`): every
`{% %}`/`{{ }}` is gone, the product grid is genuinely unrolled with correct
`url_for`-resolved links and `%.2f`-formatted prices, and the one page with
truly request-time data (`product_details`, where `product` comes from
`find_product_by_id(product_id)` at request time) honestly shows a logged,
visible comment instead of fabricating a product.

---

## 4. Output structure

```
output/                    ← pure, final static HTML (the compiler's main deliverable)
├── index.html
├── view_products.html
├── product_details.html
├── add_product.html
└── static/
    └── styles.css         ← copied through unchanged (CSS/JS are support files, not transformed)

compiler_output/           ← analysis artifacts, not meant to be served
├── ast_python.json        ← the Python AST, as JSON (node/line/detail/children)
├── ast_jinja.json         ← every analyzed template's AST, as JSON, keyed by filename
├── semantic_report.txt    ← files analyzed + every semantic problem found (or "No errors.")
└── generation_log.txt     ← what became what, and every runtime-dependent value the
                              generator could not prove and therefore left visibly unresolved

generated/                 ← unchanged: the executable Flask project (app.py + live templates)
```

One filename is generated per Python view function (`index`, `view_products`,
`product_details`, `add_product`), not per template file — the real project's
single `index.html` drives four distinct pages via a `page` parameter, and
"one Jinja template → one generated HTML file" in the spec means one output
page per render, which for a template reused across routes means one file
per route. Support files (`app.py`, CSS/JS) are never transformed themselves;
CSS/JS is copied into `output/static/`, `app.py` stays at its source location.

---

## 5. Error handling for this stage

The whole static-site pass runs inside the same try/catch boundary
`FlaskProjectGenerator.generate()` already had (§ existing docs), plus one
more layer specific to this stage: **each page is generated inside its own
try/catch**, so one page's failure is logged (`generation_log.txt` and a
console warning) without aborting the rest of the batch or the already-
succeeded executable-app output. Every *individual expression* the generator
can't resolve (an unsupported filter, a runtime-only value, an unresolvable
`url_for`) is handled by returning "unknown" and logging — never by throwing
— so a single unresolvable value degrades that one HTML fragment instead of
the whole page. Structural failures (I/O errors writing a file) still raise
`CodeGenError` exactly as the existing generation stages do.

---

## 6. Bytecode — restated for a "pure HTML/CSS/JS" target

Every question asked here was already answered in full in
[`RESOLVER_AND_BYTECODE.md`](RESOLVER_AND_BYTECODE.md) § 6: what bytecode is,
who generates it (CPython's compiler, internally, never by hand), why this
project should generate **source** (Python/HTML) rather than bytecode, how
CPython's own AST → bytecode → PVM pipeline works, and why bytecode is an
*internal implementation detail of CPython*, not a phase that belongs in a
*source-to-source* educational translator's own architecture.

That conclusion holds — and is **strengthened, not just repeated** — by this
round of work: the project's stated final target is now explicitly **"pure
HTML/CSS/JS"**, output that a browser or static file server consumes
directly. Python bytecode has no role in that output whatsoever — the
generated `app.py` is not even the artifact being optimized for anymore; the
`output/*.html` pages are meant to be served with **no Python process
running at all**. Adding a bytecode-generation phase would move the compiler
further from, not closer to, its own goal.

**Recommendation, restated: stop at generated source (Python + static
HTML). Do not generate bytecode.** If a reader wants to see the bytecode
CPython would produce for the generated `app.py`, that already exists for
free: `python -m dis generated/app.py`, since CPython performs that step
internally the moment the file is executed — reimplementing it here would
duplicate a well-tested, versioned-with-CPython feature for no educational
gain the existing AST/Resolver/CodeGen pipeline doesn't already provide.

---

## 7. Known limitations (stated plainly)

- **`{% extends %}`/`{% include %}`** are not evaluated — logged and left as
  a visible comment. Supporting them needs a second, cross-template
  resolution pass (loading and merging another template's AST), which the
  real project doesn't exercise and which was left out to keep this change
  incremental rather than a rewrite.
- **Macros** support the common case (parameter binding, body-as-string) but
  not closures over the call site's own local scope.
- **Filters** cover the ones the real project uses (`length`, `format`) plus
  a handful of cheap, common wins (`upper`/`lower`/`title`/`capitalize`/
  `trim`/`default`); anything else degrades to a logged empty value rather
  than a crash.
- **A pre-existing, unrelated float-precision quirk** is visible in
  `generation_log.txt`'s raw value dump (e.g. `999.989990234375` instead of
  `999.99`): `python.models.atom_statement.FloatAtom` stores its value as a
  Java `float`, not a `double`, so widening it for `ConstantValue.ofFloat`
  carries over `float`'s lower precision. This does **not** affect the
  actual generated HTML (`"%.2f"|format(...)` correctly prints `999.99`
  regardless), only the log's raw display — noted here rather than quietly
  "fixed" in a file this task didn't otherwise touch.
- Inherits the same block-scoping caveat already documented for the
  Python/Jinja2 resolvers in `PROJECT_OVERVIEW.md` § 6.
