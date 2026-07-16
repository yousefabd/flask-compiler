# شرح كامل لمشروع مترجم miniFlask (بالعربية)

## ما هو هذا المشروع؟

هذا المشروع هو **مُتَرجِم (Compiler) تعليمي** مكتوب بلغة **Java** باستخدام مولّد المحلّلات **ANTLR 4**.

اللغة المصدرية التي يترجمها المشروع اسمها **miniFlask**، وهي عبارة عن:

* ملف **Python/Flask** (الواجهة الخلفية Backend) — مثل `tests/app.py`
* قوالب **HTML + Jinja2** (الواجهة الأمامية Frontend) — مثل `tests/templates/index.html`
* ملفات **CSS** للتنسيق — مثل `tests/static/styles.css`

المترجم يقرأ هذه الملفات، يحلّلها لغويًا ودلاليًا، يكتشف الأخطاء فيها، ثم **يولّد منها مشروع Flask حقيقي قابل للتشغيل** في مجلد `generated/` يمكن تشغيله مباشرة بأمر:

```
python generated/app.py
```

بمعنى آخر: المشروع "مترجم من miniFlask إلى Python + HTML"، والـ **AST (شجرة التركيب المجرّدة)** هي التمثيل الوسيط بين المرحلتين.

---

## مراحل المترجم (Pipeline)

```
الكود المصدري (app.py + templates/*.html)
        │
        ▼
1) التحليل اللفظي (Lexer)          ← تقسيم النص إلى Tokens
        │
        ▼
2) التحليل النحوي (Parser)         ← بناء شجرة الإعراب (Parse Tree) حسب القواعد g4
        │
        ▼
3) بناء الـ AST (Visitors)          ← تحويل شجرة الإعراب إلى شجرة تركيب مجردة نظيفة
        │
        ▼
4) التحليل الدلالي (Semantic)      ← جدول الرموز + كشف الأخطاء الدلالية + فحص الأنواع
        │
        ▼
5) توليد الكود (Code Generation)   ← إنتاج ملفات Python و HTML قابلة للتشغيل
```

كل مرحلة محاطة بـ **حاجز أخطاء (Error Boundary)**: أي خطأ نحوي أو دلالي أو خطأ توليد يُجمَع في تقرير موحّد ولا يُسقِط البرنامج أبدًا.

---

## هيكل المشروع والملفات

### 1) مجلد `grammars/` — قواعد اللغات (ANTLR Grammar)

| الملف | الوظيفة |
|---|---|
| `python/PythonLexer.g4` + `PythonParser.g4` | قواعد لغة Python المصغّرة: الدوال، الشروط، الحلقات، الاستيراد، الـ decorators (مثل `@app.route`)، التعابير الحسابية والمنطقية، القوائم والقواميس... |
| `html/HTMLLexer.g4` + `HTMLParser.g4` | قواعد HTML مع دعم عبارات Jinja2 داخلها (`{{ }}` و `{% %}`) |
| `jinja2/Jinja2Lexer.g4` + `Jinja2Parser.g4` | قواعد تعابير Jinja2 |
| `css/CSSLexer.g4` + `CSSParser.g4` | قواعد CSS (المحدّدات Selectors والخصائص Declarations) |

### 2) مجلد `src/antlr/` — الأصناف المولّدة من ANTLR

أصناف الـ Lexer والـ Parser والـ Visitor الأساسية المولَّدة تلقائيًا من ملفات `.g4` (لا تُعدَّل يدويًا).

### 3) مجلد `src/python/` — خط أنابيب لغة Python

| المكوّن | الوظيفة |
|---|---|
| `models/` | أصناف عقد الـ AST: `Program`, `FunctionDef`, `IfStatement`, `ForStatement`, `WhileStatement`, `DecoratorStatement`, `BinaryExpression`, `IDTrailer`, والذرّات (`IntegerAtom`, `StringAtom`, `List`, `Dictionary`, ...) — كل عقدة تحفظ بياناتها ورقم السطر وأبناءها |
| `visitor/PythonVisitor.java` | يحوّل شجرة إعراب ANTLR إلى AST |
| `symbol_table/` | جدول الرموز: `Scope`, `Symbol`, `SymbolTable`, `SymbolTableBuilder` — يبني النطاقات (global / function / for / while / if) ويعرّف المتغيرات والدوال والمعاملات، ويدعم عبارة `global` |
| `symbol_table/CompilerError.java` **(جديد)** | أخطاء دلالية خاصة ببايثون: دالة مكررة، معامل مكرر، `return` خارج دالة، `break`/`continue` خارج حلقة، `global` على مستوى الملف |
| `printer/ASTPrinter.java` | طباعة الـ AST على شكل شجرة للأغراض التعليمية |

### 4) مجلد `src/jinja2/` — خط أنابيب القوالب (HTML + Jinja2)

| المكوّن | الوظيفة |
|---|---|
| `models/` | عقد AST القوالب: `TemplateFile`, `HtmlTextNode`, `OutputNode` (أي `{{ expr }}`), عناصر HTML العادية والفارغة، الخصائص Attributes، وعبارات `ForStatementNode`, `IfStatementNode`, `SetStatementNode`, `MacroStatementNode`, `BlockStatementNode`, `ExtendsStatementNode`, `IncludeStatementNode`، والتعابير (وصول لخاصية، فهرسة، استدعاء، فلاتر `|length` ...) |
| `visitor/AntlrToTemplateAstVisitor.java` | يحوّل شجرة إعراب HTML/Jinja إلى AST |
| `symbol_table/` | جدول رموز القوالب + `TypeChecker` لفحص الأنواع (TYPE_MISMATCH / TYPE_ERROR) + `CompilerError` بأنواعها (متغير غير معرّف، متغير خارج نطاقه، تكرارات...) |
| `symbol_table/semantic_rules/` | قواعد دلالية قابلة للإضافة (نمط Strategy) مثل `UlLiRule` (فحص أن `<li>` داخل `<ul>`) |

### 5) مجلد `src/css/` و `src/html/` — خطوط أنابيب مساعدة

بناء AST لملفات CSS وجداول رموز لعناصر HTML مع قواعد دلالية (مثل كشف المراجع المكسورة).

### 6) مجلد `src/errors/` — **(جديد)** إطار معالجة الأخطاء الموحّد

| الصنف | الوظيفة |
|---|---|
| `CompilerStage` | مرحلة الخطأ: `PARSING`, `SEMANTIC_ANALYSIS`, `CODE_GENERATION`, `IO` |
| `CompilerException` | الأب المجرد لكل أخطاء المترجم (يحمل المرحلة + الملف + السطر) |
| `ParseError` / `SemanticError` / `CodeGenError` | استثناءات لكل مرحلة |
| `CompilerProblem` | سجل خطأ موحّد قابل للطباعة بصيغة `[KIND] file line N: message` |
| `ErrorReporter` | المجمّع المركزي: يستقبل أخطاء بايثون وأخطاء Jinja2 والاستثناءات ويطبع تقريرًا واحدًا مجمّعًا حسب المرحلة |
| `SyntaxErrorListener` | مستمع ANTLR يلتقط الأخطاء النحوية بدل طباعتها على الشاشة، فلا يُبنى AST من شجرة معطوبة |

### 7) مجلد `src/codegen/` — **(جديد)** مولّد الكود

| الصنف | الوظيفة |
|---|---|
| `PythonCodeGenerator.java` | يمشي على AST بايثون ويولّد كود Python تنفيذي: كل عبارة وتعبير ومعامل له ترجمة، الأقواس الصريحة محفوظة (`ParenAtom`)، وعوامل `and/or/not` المتداخلة تُقوّس تلقائيًا للحفاظ على المعنى. كما **يحقن معالجات أخطاء وقت التشغيل** (`@app.errorhandler(404)` و `@app.errorhandler(Exception)`) قبل حارس `if __name__ == "__main__"` عند اكتشاف تطبيق Flask |
| `TemplateCodeGenerator.java` | يمشي على AST القوالب ويولّد HTML/Jinja2: العناصر والخصائص، `{{ expr }}`، وكل عبارات `{% %}`، مع الحفاظ على النصوص الأصلية حرفيًا |
| `FlaskProjectGenerator.java` | **قائد المشروع كاملًا**: يفسّر `app.py`، يكتشف القوالب المستدعاة عبر `render_template(...)` مع متغيرات السياق الممرَّرة لها، يحلّل كل قالب دلاليًا (مع تغذية جدول الرموز بأسماء Flask المدمجة مثل `url_for` و `request` + متغيرات السياق)، ثم يكتب المخرجات وينسخ الملفات الثابتة — وكل ذلك خلف حاجز أخطاء واحد |

### 8) `src/Main.java` — نقطة الدخول

يحتوي دوال تجريبية لكل خط أنابيب (`python()`, `jinja()`, `types()`, `css()`) بالإضافة إلى الدالة الجديدة **`compile()`** التي تشغّل الترجمة الكاملة من المصدر إلى المخرجات.

### 9) مجلد `tests/` — البرنامج المصدري (مدخلات المترجم)

| الملف | المحتوى |
|---|---|
| `app.py` | تطبيق "متجر منتجات" كامل بـ Flask: قائمة منتجات، دوال بحث/حذف/أكبر معرّف، ومسارات `/` و `/products` و `/product/<id>` و `/add` (GET+POST) و `/delete/<id>` مع رسائل flash |
| `templates/index.html` | قالب واحد متعدد الصفحات (رئيسية، قائمة المنتجات، تفاصيل منتج، إضافة منتج) باستخدام `{% if %}` و `{% for %}` و `{% set %}` والفلاتر |
| `templates/variables.html`, `types.html`, `scopes.html`, `ulli.html`, `attr_value.html` | قوالب تجريبية لاختبار جدول الرموز وفحص الأنواع والقواعد الدلالية |
| `static/styles.css` | التنسيق |

### 10) مجلد `generated/` — المخرجات (يُنشأ تلقائيًا، خارج git)

```
generated/
├── app.py               ← الباك-إند المولَّد + معالجات أخطاء 404/500 المحقونة
├── templates/
│   └── index.html       ← القوالب المولَّدة (فقط المستدعاة فعليًا في app.py)
└── static/
    └── styles.css       ← منسوخ كما هو
```

### 11) `docs/CODEGEN.md`

توثيق تفصيلي (بالإنجليزية) لتصميم معالجة الأخطاء وجداول تحويل كل عقدة AST إلى اللغة الهدف.

---

## أهم المزايا (Features)

1. **أربع لغات مدعومة بالتحليل**: Python، HTML، Jinja2، CSS — لكل منها Lexer/Parser/AST خاص.
2. **جداول رموز حقيقية** بنطاقات متداخلة (global / function / loop / macro / block) مع دعم `global` في بايثون ومتغير `loop` السحري في Jinja.
3. **فحص أنواع** في القوالب (TypeChecker): كشف العمليات غير الصالحة وعدم توافق الأنواع.
4. **قواعد دلالية قابلة للتوسعة** (واجهة `ISemanticRule`) مثل قاعدة `ul/li`.
5. **تحليل دلالي واعٍ بـ Flask (ميزة مميزة)**: المترجم يقرأ استدعاءات `render_template('index.html', page='home', products=products)` من الباك-إند ويعرف أن `page` و `products` متغيرات معرفة داخل `index.html` — أي فحص "متغير غير معرّف" **عابر للملفات** بين Python و HTML.
6. **توليد كود كامل**: من الـ AST إلى مشروع Flask يعمل فعليًا بنفس سلوك البرنامج المصدري.
7. **معالجة أخطاء شاملة على ثلاث طبقات**:
   * أخطاء **نحوية** تُجمَع عبر `SyntaxErrorListener` (لا انهيار، لا AST معطوب)
   * أخطاء **دلالية** من بايثون و Jinja معًا في تقرير واحد
   * أخطاء **التوليد والإدخال/الإخراج** عبر `CodeGenError` + حارس أخير لأي استثناء غير متوقع (`INTERNAL`)
8. **أمان وقت التشغيل في التطبيق المولَّد**: صفحات 404 و 500 ودّية بدل شاشة traceback.

---

## الاختبارات والتحقق (ما تم فحصه فعليًا)

### أ) المسار السليم (Happy Path)

* ترجمة `tests/app.py` + `index.html` نجحت بدون أي خطأ.
* الملف المولَّد `generated/app.py` **اجتاز فحص بايثون الحقيقي** (`python -m py_compile`).
* تشغيل التطبيق المولَّد عبر Flask test client:
  * `/`, `/products`, `/product/1`, `/add` → **200**
  * `POST /add` بمنتج جديد → **302** والمنتج "Tablet" ظهر في القائمة ✔
  * `/delete/2` → المنتج حُذف فعلًا ورسالة flash ظهرت ✔
  * رابط غير موجود `/missing` → **404** من المعالج المولَّد ✔

### ب) مسارات الأخطاء (تمّت بأربع حالات مُعدّة خصيصًا)

| الحالة | النتيجة |
|---|---|
| خطأ نحوي في `app.py` (مثل `def broken(:`) | تقرير `[SYNTAX] ... line 1: mismatched input ':'` — بدون انهيار |
| أخطاء دلالية (`global` بمستوى الملف، `break` خارج حلقة، معامل مكرر، دالة مكررة، `return` خارج دالة) | **خمسة أخطاء** ظهرت كلها بأنواعها وأسطرها الصحيحة |
| متغير غير معرّف داخل قالب | `[UNDEFINED_VARIABLE] ... 'missing_variable'` |
| `render_template('nope.html')` لقالب غير موجود | خطأ دلالي واضح: "refers to missing template" |

في كل الحالات: `success = false` + تقرير مجمّع، **ولم يُولَّد أي ملف ناقص**.

---

## كيف تشغّل المشروع؟

```bash
# 1) الترجمة (من جذر المشروع، مع مسار jar الخاص بـ ANTLR)
javac -encoding UTF-8 -cp antlr-4.13.2-complete.jar -d out $(find src -name "*.java")

# 2) تشغيل المترجم (ينفّذ Main.compile)
java -cp "out;antlr-4.13.2-complete.jar" Main

# 3) تشغيل التطبيق الناتج
python generated/app.py
# ثم افتح http://127.0.0.1:5000
```

---

## الخلاصة

المشروع مترجم تعليمي متكامل يغطي دورة الترجمة كاملة: **قواعد لغوية → تحليل لفظي ونحوي → AST → جداول رموز وتحليل دلالي وفحص أنواع → توليد كود هدف تنفيذي**، مع إطار معالجة أخطاء موحّد يجعل المترجم لا ينهار مهما كانت المدخلات سيئة، وميزة فريدة هي الربط الدلالي بين الباك-إند (Python/Flask) والفرونت-إند (Jinja2/HTML). الناتج النهائي ليس مجرد طباعة أشجار، بل **تطبيق ويب حقيقي يعمل في المتصفح** بنفس سلوك البرنامج المصدري.
