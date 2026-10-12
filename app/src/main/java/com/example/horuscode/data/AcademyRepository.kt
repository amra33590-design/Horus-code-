package com.example.horuscode.data

import com.example.horuscode.engine.model.HorusSymbolType

data class QuizQuestion(
    val questionEn: String,
    val questionAr: String,
    val optionsEn: List<String>,
    val optionsAr: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationAr: String
)

data class AcademyLesson(
    val id: Int,
    val titleEn: String,
    val titleAr: String,
    val summaryEn: String,
    val summaryAr: String,
    val hieroglyphicSymbol: String,
    val symbolAncientMeaningAr: String,
    val symbolAncientMeaningEn: String,
    val programmingConnectionAr: String,
    val programmingConnectionEn: String,
    val contentEn: String,
    val contentAr: String,
    val associatedSymbol: HorusSymbolType,
    val exampleCode: String,
    val interactiveExerciseTaskEn: String,
    val interactiveExerciseTaskAr: String,
    val quiz: QuizQuestion,
    val xpReward: Int = 100
)

object AcademyRepository {

    val lessons: List<AcademyLesson> = listOf(
        // Lesson 1: Ankh ☥
        AcademyLesson(
            id = 1,
            titleEn = "The Ankh ☥: Life & Program Lifecycle",
            titleAr = "رمز العنخ ☥: مفتاح الحياة وبدء البرنامج",
            summaryEn = "The Ankh initiates and concludes the lifecycle of every Horus Code program.",
            summaryAr = "العنخ يمنح الحياة للبرنامج ويبدأ دورة تنفيذ الأوامر ويختمها بأمان.",
            hieroglyphicSymbol = "☥",
            symbolAncientMeaningAr = "في مصر القديمة، كان العنخ رمزاً للحياة المادية والروحية ونفحة الخلود الأبدية.",
            symbolAncientMeaningEn = "In ancient Egypt, the Ankh represented physical life, divine breath, and eternal vitality.",
            programmingConnectionAr = "الرابط البرمجي: تماماً كما يبدأ العنخ الحياة، فإنه يبدأ دورة حياة البرنامج بالأمر (☥ بدء) ويختمها بسلام بالأمر (☥ ختام). لا يمكن لبرنامج أن يعمل بدون بداية ونهاية.",
            programmingConnectionEn = "Programming Connection: Just as the Ankh breathes life, it defines the program boundary: '☥ بدء' starts lifecycle and '☥ ختام' safely terminates.",
            contentEn = """
                In ancient Egypt, the Ankh was the sacred key of life.
                In HORUS CODE, we use ☥ as our foundational symbol:
                - '☥ بدء' initiates execution.
                - '☥ ختام' marks the clean termination of the code.
                Everything in between executes sequentially from top to bottom.
            """.trimIndent(),
            contentAr = """
                في مصر القديمة، كان رمز العنخ ☥ هو المفتاح المقدس للحياة.
                في شفرة حورس، نبرمج بالرمز ☥ مباشرة:
                - نكتب في بداية البرنامج: ☥ بدء
                - ونكتب في نهاية البرنامج: ☥ ختام
                بين هذين الحدين تسري جميع أوامر البرنامج بتتابع متسلسل ودقيق.
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.ANKH,
            exampleCode = """
                ☥ بدء
                𓏛 "أهلاً بك في لغة حورس البرمجية"
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Inscribe a program using Ankh ☥ to start and terminate.",
            interactiveExerciseTaskAr = "اكتب برنامجاً يبدأ برمز العنخ (☥ بدء) وينتهي برمز العنخ (☥ ختام).",
            quiz = QuizQuestion(
                questionEn = "Why does Horus Code use the Ankh ☥ for START and STOP?",
                questionAr = "لماذا توظف شفرة حورس رمز العنخ ☥ لأمرَي البداية والنهاية؟",
                optionsEn = listOf("Because it looks modern", "Because it historically meant Life, representing program lifecycle", "Random choice", "To pause execution"),
                optionsAr = listOf("لأنه رمز حديث فقط", "لأنه تاريخياً رمز الحياة، فيمثل دورة حياة البرنامج وانطلاقه وختامه", "اختيار عشوائي", "لإيقاف المعالج مؤقتاً"),
                correctIndex = 1,
                explanationEn = "The Ankh historically symbolizes life, making it the perfect representation for program lifecycle.",
                explanationAr = "العنخ رمز الحياة في الحضارة المصرية، ولذا يجسد منطقياً دورة حياة البرنامج وبدايته وانتهائه."
            )
        ),

        // Lesson 2: Papyrus Scroll 𓏛
        AcademyLesson(
            id = 2,
            titleEn = "The Papyrus Scroll 𓏛: Inscribing Output",
            titleAr = "لفافة البردي 𓏛: تدوين وإخراج النتائج",
            summaryEn = "The Papyrus scroll records wisdom and outputs text or calculations onto the console.",
            summaryAr = "لفافة البردي كانت أداة التدوين لدى الكَتَبَة، وتُستخدم لإخراج النصوص والعمليات الحسابية.",
            hieroglyphicSymbol = "𓏛",
            symbolAncientMeaningAr = "وسيلة التدوين المعتمدة لدى الكَتَبَة في مصر القديمة لحفظ المعارف والحسابات والمراسيم الرسمية.",
            symbolAncientMeaningEn = "The writing medium used by Egyptian scribes for wisdom texts, mathematics, and official records.",
            programmingConnectionAr = "الرابط البرمجي: البرمجة تحتاج وسيلة لإظهار النتائج للمستخدم (شاشة المخرجات Output)؛ ولذا نستخدم رمز لفافة البردي 𓏛 لتدوين النصوص وحساب المعادلات وعرضها.",
            programmingConnectionEn = "Programming Connection: Standard Output (Console). The scribe's scroll 𓏛 transcribes messages and evaluated arithmetic onto the screen.",
            contentEn = """
                Ancient Egyptian scribes inscribed history and mathematics on papyrus rolls.
                In HORUS CODE, the symbol 𓏛 outputs values:
                - Text: 𓏛 "Wisdom of Kemet"
                - Math calculations: 𓏛 7 * 6
                The interpreter evaluates the expression and displays the result on screen.
            """.trimIndent(),
            contentAr = """
                كان الكاتب المصري القديم يخلد المعارف والأرقام على لفائف البردي.
                في لغة حورس، نستخدم رمز لفافة البردي 𓏛 لإخراج النتائج:
                - لطباعة نص: 𓏛 "حكمة الأجداد"
                - لحساب معادلة وطباعتها: 𓏛 7 * 6
                يقوم المفسر بحساب النتيجة وتدوينها فوراً على شاشة المخرجات.
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.PAPYRUS,
            exampleCode = """
                ☥ بدء
                𓏛 "حكمة من وادي النيل"
                𓏛 10 + 20
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Inscribe two outputs on papyrus: a greeting text and a mathematical equation.",
            interactiveExerciseTaskAr = "دوّن مخرجين برمز البردي 𓏛: جملة ترحيبية ثم معادلة حسابية.",
            quiz = QuizQuestion(
                questionEn = "What does the Papyrus symbol 𓏛 do in Horus Code?",
                questionAr = "ما وظيفة رمز لفافة البردي 𓏛 في شفرة حورس؟",
                optionsEn = listOf("Deletes files", "Inscribes and outputs text or calculation results on screen", "Turns off power", "Creates an infinite loop"),
                optionsAr = listOf("حذف الملفات", "تدوين وطباعة النصوص والنتائج الحسابية على شاشة المخرجات", "إطفاء الجهاز", "صناعة حلقة لا نهائية"),
                correctIndex = 1,
                explanationEn = "Papyrus 𓏛 represents standard output, printing messages and calculations.",
                explanationAr = "لفافة البردي 𓏛 تمثل المخرجات (PRINT) وتدون النتائج على الشاشة."
            )
        ),

        // Lesson 3: Djed Pillar 𓋹
        AcademyLesson(
            id = 3,
            titleEn = "The Djed Pillar 𓋹: Stability & Variable Memory",
            titleAr = "عمود جد 𓋹: رمز الاستقرار وتثبيت المتغيرات",
            summaryEn = "The Djed represents stability, anchoring variables into memory cells.",
            summaryAr = "عمود جد يرمز للثبات والصلابة، ويُستخدم لتثبيت المتغيرات وحفظ قيمها في الذاكرة.",
            hieroglyphicSymbol = "𓋹",
            symbolAncientMeaningAr = "رمز يحاكي العمود الفقري لأوزوريس، ويمثل الاستقرار والصلابة والدوام وعدم التزعزع.",
            symbolAncientMeaningEn = "Associated with Osiris, symbolizing endurance, stability, and unwavering strength.",
            programmingConnectionAr = "الرابط البرمجي: تحتاج البرمجة لخلايا ذاكرة مستقرة تحفظ القيم ولا تضيع (المتغيرات Variables)؛ ولذا نستخدم رمز عمود جد 𓋹 لتثبيت قيمة المتغير وتعديلها: 𓋹 الطاقة = 100.",
            programmingConnectionEn = "Programming Connection: Variables & Memory. Djed 𓋹 anchors data values into RAM: 𓋹 score = 100.",
            contentEn = """
                Variables are labeled containers in computer memory.
                In HORUS CODE, the Djed pillar 𓋹 stabilizes values:
                - 𓋹 score = 50
                - 𓋹 score = score + 25
                The value is held safely in memory and can be recalled anytime.
            """.trimIndent(),
            contentAr = """
                المتغيرات هي خانات مسماة في الذاكرة تحفظ البيانات.
                في لغة حورس، يمنح عمود جد 𓋹 الثبات والاستقرار للمتغير:
                - 𓋹 الطاقة = 50
                - 𓋹 الطاقة = الطاقة + 25
                تستقر القيمة في الذاكرة ويمكن استرجاعها واستخدامها في أي وقت.
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.DJED,
            exampleCode = """
                ☥ بدء
                𓋹 الطاقة = 50
                𓋹 الطاقة = الطاقة + 25
                𓏛 الطاقة
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Stabilize a variable using Djed 𓋹 and increment its value.",
            interactiveExerciseTaskAr = "ثبّت متغيراً برمز عمود جد 𓋹 وزد قيمته في الذاكرة ثم اطبعه برمز البردي 𓏛.",
            quiz = QuizQuestion(
                questionEn = "Why is the Djed Pillar 𓋹 mapped to variables in programming?",
                questionAr = "لماذا تم ربط عمود جد 𓋹 بالمتغيرات في البرمجة؟",
                optionsEn = listOf("Because it meant stability, representing reliable memory storage", "To delete variables", "Because it is round", "No relation"),
                optionsAr = listOf("لأنه رمز الاستقرار والثبات، فيمثل خلايا الذاكرة التي تحفظ القيم بثبات", "لحذف المتغيرات", "لأنه دائري الشكل", "لا يوجد أي رابط"),
                correctIndex = 0,
                explanationEn = "Djed historically represents stability and endurance, mirroring how computer memory reliably holds data.",
                explanationAr = "عمود جد يرمز للاستقرار والصلابة، تماماً كما تحفظ خلايا الذاكرة قيم المتغيرات بثبات وموثوقية."
            )
        ),

        // Lesson 4: Eye of Horus 𓂀
        AcademyLesson(
            id = 4,
            titleEn = "The Eye of Horus 𓂀: Insight & Conditional Decisions",
            titleAr = "عين حورس 𓂀: البصيرة واتخاذ القرارات الشرطية",
            summaryEn = "The Wedjat Eye represents insight and fractional weighing, evaluating IF/ELSE conditions.",
            summaryAr = "عين الوجات ترمز للبصيرة الثاقبة ووزن المقادير، وتُستخدم لفحص الشروط واتخاذ القرارات.",
            hieroglyphicSymbol = "𓂀",
            symbolAncientMeaningAr = "عين حورس (الوجات) المقدسة، رمز الحماية والشفاء والبصيرة النافذة، واستخدمت أجزاؤها لوزن المقادير والكسور الحسابية.",
            symbolAncientMeaningEn = "The Wedjat eye of Horus: Divine protection, whole-ness, restoration, keen discernment, and fractional measurement.",
            programmingConnectionAr = "الرابط البرمجي: اتخاذ القرارات (الشروط IF / ELSE)؛ البصيرة الثاقبة التي تزن الأمور وتفحص هل الشرط صواب أم خطأ لتختار المسار الصحيح: 𓂀 الدرجة >= 60.",
            programmingConnectionEn = "Programming Connection: Conditional logic (IF / ELSE). Discerning truth from falsehood to route execution branches.",
            contentEn = """
                Programs make intelligent decisions based on conditions.
                With the Eye of Horus 𓂀, the computer evaluates truth:
                - 𓂀 score >= 60
                  𓏛 "Passed"
                - وإلا
                  𓏛 "Retry"
                - ختام_الشرط
            """.trimIndent(),
            contentAr = """
                تتخذ البرامج قراراتها بناءً على فحص الشروط المنطقية.
                برمز عين حورس 𓂀، تفحص بصيرة البرنامج صحة الشرط:
                - 𓂀 الدرجة >= 60
                    𓏛 "ناجح"
                - وإلا
                    𓏛 "حاول ثانية"
                - ختام_الشرط
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.EYE_OF_HORUS,
            exampleCode = """
                ☥ بدء
                𓋹 الدرجة = 85
                𓂀 الدرجة >= 60
                  𓏛 "اجتزت الاختبار بتميز"
                وإلا
                  𓏛 "تحتاج مراجعة"
                ختام_الشرط
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Create a conditional decision branch using Eye of Horus 𓂀.",
            interactiveExerciseTaskAr = "ابنِ قراراً شرطياً باستخدام عين حورس 𓂀 مع فرع 'وإلا' وأغلق بـ 'ختام_الشرط'.",
            quiz = QuizQuestion(
                questionEn = "What does the Eye of Horus 𓂀 evaluate in code?",
                questionAr = "ماذا تفحص عين حورس 𓂀 في لغة البرمجة؟",
                optionsEn = listOf("Screen brightness", "Whether a condition is true or false to choose a branch", "Audio volume", "Battery level"),
                optionsAr = listOf("سطوع الشاشة", "ما إذا كان الشرط صواباً أو خطأ لتوجيه مسار التنفيذ", "مستوى الصوت", "شحن البطارية"),
                correctIndex = 1,
                explanationEn = "Eye of Horus 𓂀 represents keen insight, evaluating boolean tests to guide program flow.",
                explanationAr = "عين حورس 𓂀 تمثل البصيرة وميزان الفحص، وتقيم صحة الشرط لتوجيه مسار البرنامج."
            )
        ),

        // Lesson 5: Scarab Beetle 𓆣
        AcademyLesson(
            id = 5,
            titleEn = "The Scarab 𓆣: Cyclical Transformation & Loops",
            titleAr = "الجعران المقدس 𓆣: التجدد ودورات التكرار (الحلقات)",
            summaryEn = "The Scarab represents the daily cycle of the sun, automating repetitive instruction loops.",
            summaryAr = "الجعران يرمز لدورة شروق الشمس اليومية المتعاقبة، ويُستخدم لأتمتة الحلقات التكرارية.",
            hieroglyphicSymbol = "𓆣",
            symbolAncientMeaningAr = "رمز خِبري والتجدد المستمر والتحول ودورة شروق الشمس اليومية بحركة دائرية متكررة.",
            symbolAncientMeaningEn = "Associated with Khepri: Continual rebirth, transformation, and the rolling of the sun in continuous cycles.",
            programmingConnectionAr = "الرابط البرمجي: الحلقات التكرارية (Loops / REPEAT)؛ بدلاً من تكرار كتابة الأوامر يدوياً، نستخدم رمز الجعران 𓆣 لتكرار كتلة التعليمات لعدد محدد من الدورات: 𓆣 4.",
            programmingConnectionEn = "Programming Connection: Repetition & loops. Cycling instructions for a bounded number of iterations: 𓆣 4.",
            contentEn = """
                Instead of duplicating code lines, loops cycle instructions automatically.
                The Scarab 𓆣 embodies this cyclical repetition:
                - 𓆣 4
                  𓏛 "Cycle of Horus"
                - ختام_التكرار
                Executes exactly 4 times without manual duplication.
            """.trimIndent(),
            contentAr = """
                بدلاً من إعادة كتابة الأوامر عدة مرات يدوياً، تدير الحلقات التكرار تلقائياً.
                يجسد الجعران 𓆣 هذه الدورة المنظمة:
                - 𓆣 4
                    𓏛 "دورة في مسار الجعران"
                - ختام_التكرار
                ينفذ الأمر 4 مرات بدقة دون تكرار كتابة الكود.
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.SCARAB,
            exampleCode = """
                ☥ بدء
                𓆣 4
                  𓏛 "خطوة في مسار الجعران"
                ختام_التكرار
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Automate repeated outputs using Scarab 𓆣 loop.",
            interactiveExerciseTaskAr = "استخدم حلقة الجعران 𓆣 لطباعة رسالة 4 مرات واختم بـ 'ختام_التكرار'.",
            quiz = QuizQuestion(
                questionEn = "Why does Horus Code use the Scarab 𓆣 for loops?",
                questionAr = "لماذا تم اختيار الجعران 𓆣 لتمثيل الحلقات التكرارية؟",
                optionsEn = listOf("Because it moves slowly", "Because historically it represented cyclical transformation and repeating sun cycles", "To stop code", "No reason"),
                optionsAr = listOf("لأنه كائن بطيء", "لأنه رمز التحول والتجدد ودورات شروق الشمس اليومية المتعاقبة", "لإيقاف الكود", "بدون سبب"),
                correctIndex = 1,
                explanationEn = "The Scarab historically represents cyclical rebirth and daily solar motion, perfectly mirroring loop cycles.",
                explanationAr = "الجعران رمز الدورة اليومية للشمس والتحول المستمر، مما يجعله التطبيق الأمثل للحلقات التكرارية."
            )
        ),

        // Lesson 6: Feather of Ma'at 𓋖
        AcademyLesson(
            id = 6,
            titleEn = "The Feather of Ma'at 𓋖: Truth, Balance & User Input",
            titleAr = "ريشة ماعت 𓋖: العدالة ووزن واستقبال المدخلات",
            summaryEn = "The Feather of Ma'at weighs incoming values from the user to incorporate into computations.",
            summaryAr = "ريشة ماعت تزن الحق والعدل، وتُستخدم لاستقبال مدخلات المستخدم والتفاعل معها.",
            hieroglyphicSymbol = "𓋖",
            symbolAncientMeaningAr = "شارة الإلهة ماعت، ربة الحقيقة والعدل والميزان الكوني، وكانت توضع في كفة الميزان لوزن القلوب.",
            symbolAncientMeaningEn = "The ostrich feather of Ma'at: Cosmic order, truth, and balance against which hearts were weighed.",
            programmingConnectionAr = "الرابط البرمجي: استقبال المدخلات (User Input)؛ البرامج التفاعلية تحتاج لوزن واستقبال بيانات خارجية من المستخدم لقياسها: 𓋖 \"أدخل طاقتك\" في الطاقة.",
            programmingConnectionEn = "Programming Connection: Interactive Input (INPUT). Receiving and measuring dynamic values provided by the human user.",
            contentEn = """
                Interactive programs receive data from users.
                With the Feather of Ma'at 𓋖, the program receives and weighs input:
                - 𓋖 "Enter energy" في energy
                - 𓋹 boosted = energy * 2
                - 𓏛 boosted
            """.trimIndent(),
            contentAr = """
                تحتاج البرامج التفاعلية لاستقبال بيانات من المستخدمين.
                برمز ريشة ماعت 𓋖، يستقبل البرنامج المدخل ويزنه في الذاكرة:
                - 𓋖 "أدخل طاقتك" في الطاقة
                - 𓋹 المضاعف = الطاقة * 2
                - 𓏛 المضاعف
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.FEATHER,
            exampleCode = """
                ☥ بدء
                𓋖 "أدخل الطاقة" في الطاقة
                𓋹 المضاعف = الطاقة * 2
                𓏛 المضاعف
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Prompt the user with Feather 𓋖 and compute with the input.",
            interactiveExerciseTaskAr = "استقبل مدخلاً بريشة ماعت 𓋖 وضاعف قيمته بعمود جد 𓋹 واطبعه بالبردي 𓏛.",
            quiz = QuizQuestion(
                questionEn = "What is the programming role of the Feather of Ma'at 𓋖?",
                questionAr = "ما الوظيفة البرمجية لريشة ماعت 𓋖 في شفرة حورس؟",
                optionsEn = listOf("Deleting text", "Receiving and weighing interactive user input into variables", "Restarting the device", "Printing to paper"),
                optionsAr = listOf("حذف النصوص", "استقبال ووزن مدخلات المستخدم وحفظها في المتغيرات", "إعادة تشغيل الجهاز", "الطباعة الورقية"),
                correctIndex = 1,
                explanationEn = "Feather 𓋖 represents measuring and capturing external input from the user.",
                explanationAr = "ريشة ماعت 𓋖 تمثل وزن واستقبال مدخلات المستخدم التفاعلية."
            )
        ),

        // Lesson 7: Pyramidion 𓊗
        AcademyLesson(
            id = 7,
            titleEn = "The Pyramidion 𓊗: Spatial Actions & Movement",
            titleAr = "الهريم 𓊗: قمة الهرم وإطلاق الأوامر المادية",
            summaryEn = "The Pyramidion (Benben) capstone initiates physical actions in the Temple of Logic.",
            summaryAr = "الهريم (بن بن) هو حجر قمة الهرم، ويُستخدم لإطلاق أوامر الحركة والأفعال في معبد المنطق.",
            hieroglyphicSymbol = "𓊗",
            symbolAncientMeaningAr = "حجر قمة الهرم أو المسلة (بن بن)، أول ما يلامس أشعة شمس الفجر، ويرمز لبداية الانبثاق والعمل والإنارة.",
            symbolAncientMeaningEn = "The Benben stone crowning pyramids and obelisks, the primordial mound capturing dawn's first light.",
            programmingConnectionAr = "الرابط البرمجي: الأوامر والإجراءات المادية (Actions / Functions)؛ قيادة مستكشف حورس في معبد المنطق: (𓊗 تقدم)، (𓊗 يسار)، (𓊗 يمين)، (𓊗 التقط_المفتاح).",
            programmingConnectionEn = "Programming Connection: Action commands in the Temple of Logic maze: 𓊗 تقدم (move forward), 𓊗 يمين (turn right).",
            contentEn = """
                Programs don't just calculate numbers—they control physical devices, motors, and game characters.
                In HORUS CODE, the Pyramidion 𓊗 dispatches actions:
                - 𓊗 تقدم (Move Forward)
                - 𓊗 يسار (Turn Left)
                - 𓊗 يمين (Turn Right)
                - 𓊗 التقط_المفتاح (Collect Key)
            """.trimIndent(),
            contentAr = """
                البرامج لا تقتصر على الحسابات، بل تحرك الروبوتات وتوجه الشخصيات.
                في لغة حورس، يطلق الهريم 𓊗 الأوامر التنفيذية:
                - 𓊗 تقدم (تحريك المستكشف للأمام)
                - 𓊗 يسار (استدارة لليسار 90 درجة)
                - 𓊗 يمين (استدارة لليمين 90 درجة)
                - 𓊗 التقط_المفتاح (جمع المفتاح الذهبي)
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.PYRAMIDION,
            exampleCode = """
                ☥ بدء
                𓊗 تقدم
                𓊗 يسار
                𓊗 تقدم
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Inscribe a navigation routine using Pyramidion 𓊗.",
            interactiveExerciseTaskAr = "اكتب تسلسلاً حركياً يقود المستكشف باستخدام الهريم 𓊗.",
            quiz = QuizQuestion(
                questionEn = "What does the Pyramidion 𓊗 represent in Horus Code?",
                questionAr = "ماذا يمثل رمز الهريم 𓊗 في شفرة حورس؟",
                optionsEn = listOf("Audio mute", "Physical action and movement commands in the maze", "Screen lock", "Font size"),
                optionsAr = listOf("كتم الصوت", "أوامر الحركة والتنفيذ المادي في معبد المنطق", "قفل الشاشة", "حجم الخط"),
                correctIndex = 1,
                explanationEn = "Pyramidion 𓊗 dispatches movement and maze actuators.",
                explanationAr = "الهريم 𓊗 يطلق أوامر الحركة والتنقل في معبد المنطق."
            )
        ),

        // Lesson 8: Integrated Horus Code
        AcademyLesson(
            id = 8,
            titleEn = "Integrated Horus Code: Architecture & Debugging",
            titleAr = "شفرة حورس المتكاملة: التفكير الخوارزمي والتنقيح",
            summaryEn = "Synthesizing all hieroglyphic symbols into a coherent algorithmic solution.",
            summaryAr = "دمج جميع الرموز الهيروغليفية معاً لبناء خوارزمية متكاملة وتصحيح الأخطاء باحتراف.",
            hieroglyphicSymbol = "👑",
            symbolAncientMeaningAr = "التناغم والتكامل بين جميع الرموز المصرية لصناعة صرح معرفي خالد.",
            symbolAncientMeaningEn = "Harmonious synthesis of Egyptian wisdom into enduring intellectual achievements.",
            programmingConnectionAr = "الرابط البرمجي: دمج المتغيرات (𓋹)، مع الشروط (𓂀)، مع الحلقات (𓆣)، والمخرجات (𓏛) لحل التحديات الكبرى واكتشاف الأخطاء البرمجية وإصلاحها.",
            programmingConnectionEn = "Programming Connection: Complete algorithmic synthesis and diagnostic debugging.",
            contentEn = """
                Real software combines variables, loops, conditions, and input/output.
                With HORUS CODE, programming becomes intuitive, visual, and deeply meaningful:
                - From symbols of the past (☥, 𓋹, 𓂀, 𓆣, 𓏛, 𓋖, 𓊗) to code of the future!
            """.trimIndent(),
            contentAr = """
                تجمع البرمجيات الحقيقية بين المتغيرات والشروط والحلقات والمدخلات والمخرجات.
                بشفرة حورس، تتحول البرمجة إلى لغة بصرية متجذرة في الحضارة ومنضبطة في التفكير الخوارزمي:
                «من رموز الماضي… إلى شيفرة المستقبل!»
            """.trimIndent(),
            associatedSymbol = HorusSymbolType.ANKH,
            exampleCode = """
                ☥ بدء
                𓋹 الذهب = 0
                𓆣 3
                  𓋹 الذهب = الذهب + 10
                ختام_التكرار
                𓂀 الذهب >= 30
                  𓏛 "خزينة المعبد ممتلئة"
                ختام_الشرط
                ☥ ختام
            """.trimIndent(),
            interactiveExerciseTaskEn = "Inscribe a multi-step algorithmic solution combining all hieroglyphs.",
            interactiveExerciseTaskAr = "ابنِ خوارزمية متكاملة تجمع بين تثبيت الذاكرة 𓋹، والتكرار 𓆣، وبصيرة الشرط 𓂀، والبردي 𓏛.",
            quiz = QuizQuestion(
                questionEn = "What is the primary philosophy of HORUS CODE?",
                questionAr = "ما هي الفلسفة المركزية لمشروع شفرة حورس؟",
                optionsEn = listOf("English-only coding", "Teaching logic by learning the meaning of hieroglyphic symbols and mapping them to code", "No logic", "Text-only memorization"),
                optionsAr = listOf("البرمجة بالإنجليزية فقط", "تعلم البرمجة عبر فهم معاني الرموز الهيروغليفية بالعربية وتوظيفها كلغة برمجية حقيقية", "إلغاء التفكير المنطقي", "الحفظ النظري دون فهم"),
                correctIndex = 1,
                explanationEn = "Horus Code teaches programming logic through the authentic meanings of ancient Egyptian hieroglyphs.",
                explanationAr = "شفرة حورس تعلّم المنطق البرمجي عبر الفهم العميق لمعاني الرموز الهيروغليفية وتوظيفها مباشرة كلغة برمجية."
            )
        )
    )
}
