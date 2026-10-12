package com.example.horuscode.data

import com.example.horuscode.engine.model.HorusSymbolType

data class SymbolDetail(
    val type: HorusSymbolType,
    val hieroglyphicCode: String, // Gardiner sign list code e.g. S34, D10, L1, R11, Y1, C10, O24
    val pronunciationEn: String,
    val pronunciationAr: String,
    val authenticHistoryEn: String,
    val authenticHistoryAr: String,
    val modernHorusRoleEn: String,
    val modernHorusRoleAr: String,
    val syntaxUsageEn: String,
    val syntaxUsageAr: String,
    val sampleCode: String
)

object SymbolRepository {

    val symbols: List<SymbolDetail> = listOf(
        SymbolDetail(
            type = HorusSymbolType.ANKH,
            hieroglyphicCode = "Gardiner S34",
            pronunciationEn = "'Ankh' (ˁnḫ)",
            pronunciationAr = "عنخ (ˁnḫ)",
            authenticHistoryEn = "Authentic Historical Fact: The Ankh is an ancient Egyptian hieroglyphic symbol that represented the concept of life, breath of life, and eternity. It was frequently depicted held by deities to the nostrils of pharaohs.",
            authenticHistoryAr = "حقيقة تاريخية موثقة: العنخ رمز هيروغليفي مصري قديم يمثل مفهوم الحياة والخلود ونفحة الأبدية، وكان يُصوَّر في أيدي المعبودات وهي تمنح الحياة للرموز الملكية.",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Program Lifecycle & Initiation: Serves as the sacred boundary that marks the clean START and STOP of algorithmic execution.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] دورة حياة البرنامج ونقطة البداية: يمثل نقطة الانطلاق الرسمية START ونقطة الإغلاق المنضبطة STOP لكل برنامج.",
            syntaxUsageEn = "START ... STOP",
            syntaxUsageAr = "START (البداية) ... STOP (النهاية)",
            sampleCode = "START\nPRINT \"Life to Code\"\nSTOP"
        ),
        SymbolDetail(
            type = HorusSymbolType.DJED,
            hieroglyphicCode = "Gardiner R11",
            pronunciationEn = "'Djed' (ḏd)",
            pronunciationAr = "جد (ḏd)",
            authenticHistoryEn = "Authentic Historical Fact: The Djed pillar is an ancient Egyptian symbol representing stability and endurance. In mythology, it is associated with Osiris and represents his backbone.",
            authenticHistoryAr = "حقيقة تاريخية موثقة: عمود جد يمثل في الديانة المصرية القديمة الاستقرار والصلابة والدوام، وارتبط بأوزوريس وعموده الفقري.",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Memory Variables & State: Symbolizes solid memory cells in RAM where values are securely stored and updated via assignment.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] الذاكرة والمتغيرات: يمثل خلايا التخزين الثابتة في الذاكرة العشوائية حيث تُحفظ القيم ويعاد تحديثها بالأمر SET.",
            syntaxUsageEn = "SET <variable> = <expression>",
            syntaxUsageAr = "SET <اسم المتغير> = <التعبير أو القيمة>",
            sampleCode = "START\nSET energy = 100\nSET energy = energy - 10\nPRINT energy\nSTOP"
        ),
        SymbolDetail(
            type = HorusSymbolType.EYE_OF_HORUS,
            hieroglyphicCode = "Gardiner D10",
            pronunciationEn = "'Wedjat' (wḏꜢt)",
            pronunciationAr = "الوجات (عين حورس)",
            authenticHistoryEn = "Authentic Historical Fact: The Wedjat (Eye of Horus) is an ancient Egyptian symbol of protection, wholeness, and healing, mathematically associated with fractional measurement parts (1/2, 1/4, 1/8, 1/16, 1/32, 1/64).",
            authenticHistoryAr = "حقيقة تاريخية موثقة: عين حورس (الوجات) رمز للحماية والشفاء والبصيرة، واستخدمت أجزاؤها في الرياضيات المصرية القديمة لتمثيل الكسور الحسابية (1/2, 1/4, 1/8, 1/16, 1/32, 1/64).",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Conditional Logic (IF / ELSE): Represents the all-seeing evaluator that checks whether conditions hold true, routing execution into branches.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] الشروط المنطقية (IF / ELSE): يمثل الميزان البصير الذي يزن صحة الشرط (صواب أو خطأ) ليوجه تدفق البرنامج.",
            syntaxUsageEn = "IF <condition> ... ELSE ... END_IF",
            syntaxUsageAr = "IF <الشرط> ... ELSE ... END_IF",
            sampleCode = "START\nSET score = 75\nIF score >= 60\n  PRINT \"Passed\"\nELSE\n  PRINT \"Retry\"\nEND_IF\nSTOP"
        ),
        SymbolDetail(
            type = HorusSymbolType.SCARAB,
            hieroglyphicCode = "Gardiner L1",
            pronunciationEn = "'Khepri' (ḫpr)",
            pronunciationAr = "خِبري (الجعران المقدس)",
            authenticHistoryEn = "Authentic Historical Fact: The sacred scarab beetle represented spontaneous generation, transformation, and the continuous rolling of the sun across the horizon each day.",
            authenticHistoryAr = "حقيقة تاريخية موثقة: كان الجعران رمزاً للتجدد المستمر والتحول وشروق الشمس المتعاقب كل يوم بحركة دائرية متجددة.",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Loops & Repetition (REPEAT): Embodies cyclical repetition of code blocks for a specified number of passes without manual code duplication.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] الحلقات والتكرار (REPEAT): يجسد دوران الكود وتكراره لعدد محدد من المرات آلياً.",
            syntaxUsageEn = "REPEAT <count> ... END_REPEAT",
            syntaxUsageAr = "REPEAT <عدد المرات> ... END_REPEAT",
            sampleCode = "START\nREPEAT 3\n  PRINT \"Cycle of Horus\"\nEND_REPEAT\nSTOP"
        ),
        SymbolDetail(
            type = HorusSymbolType.PAPYRUS,
            hieroglyphicCode = "Gardiner Y1",
            pronunciationEn = "'Medyat' (mḏꜢt)",
            pronunciationAr = "مدجات (لفافة البردي)",
            authenticHistoryEn = "Authentic Historical Fact: Papyrus rolled scrolls were the prime recording medium used by Egyptian scribes for wisdom texts, calculations, official records, and legal edicts.",
            authenticHistoryAr = "حقيقة تاريخية موثقة: كانت لفائف البردي وسيلة التدوين المعتمدة لدى الكَتَبَة لتدوين الحسابات والمراسيم والعلوم الأدبية والقانونية.",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Standard Output (PRINT): Transcribes calculated results and textual messages onto the console screen for the user.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] شاشة المخرجات (PRINT): تدوين النتائج والمعادلات والنصوص على الشاشة للمستخدم.",
            syntaxUsageEn = "PRINT <expression>",
            syntaxUsageAr = "PRINT <القيمة أو التعبير المراد طباعته>",
            sampleCode = "START\nPRINT \"Inscribed on Papyrus: \" + (5 * 8)\nSTOP"
        ),
        SymbolDetail(
            type = HorusSymbolType.FEATHER,
            hieroglyphicCode = "Gardiner C10",
            pronunciationEn = "'Shu / Ma'at' (mꜢˁt)",
            pronunciationAr = "ريشة ماعت (mꜢˁt)",
            authenticHistoryEn = "Authentic Historical Fact: The ostrich feather was the emblem of Ma'at, goddess of truth, balance, cosmic harmony, and justice. In the Hall of Two Truths, hearts were weighed against it.",
            authenticHistoryAr = "حقيقة تاريخية موثقة: ريشة النعامة هي شارة الإلهة ماعت، ربة الحقيقة والعدل والميزان الكوني، وكانت توضع في الميزان لوزن القلوب.",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Standard Input (INPUT): Receives incoming variables and measurements from the user, introducing dynamic outside data into calculations.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] المدخلات التفاعلية (INPUT): استقبال البيانات والأرقام من المستخدم لقياسها والتفاعل معها برمجياً.",
            syntaxUsageEn = "INPUT \"prompt\" INTO <variable>",
            syntaxUsageAr = "INPUT \"السؤال\" INTO <المتغير>",
            sampleCode = "START\nINPUT \"Enter your level\" INTO lvl\nPRINT \"Current Level: \" + lvl\nSTOP"
        ),
        SymbolDetail(
            type = HorusSymbolType.PYRAMIDION,
            hieroglyphicCode = "Gardiner O24",
            pronunciationEn = "'Benben' (bnbn)",
            pronunciationAr = "بن بن (الهريم)",
            authenticHistoryEn = "Authentic Historical Fact: The Benben stone or pyramidion was the top stone of the Egyptian pyramid or obelisk, associated with the primordial mound from which creation emerged.",
            authenticHistoryAr = "حقيقة تاريخية موثقة: الهريم (بن بن) هو الحجر الذي يتوج قمة الهرم أو المسلة، وارتبط بتل الخلق الأول وأول نقطة ينبثق منها النور.",
            modernHorusRoleEn = "[Modern Design Assignment in Horus Code] Physical Actions & Movement (ACTION): Dispatches movement and temple maze actuators such as MOVE_FORWARD, TURN_LEFT, and TURN_RIGHT.",
            modernHorusRoleAr = "[توظيف حديث في مشروع شفرة حورس] أوامر الحركة والأفعال (ACTION): يطلق أوامر التحكم المادية في معبد المنطق مثل التقدم والانعطاف والتقاط المفاتيح.",
            syntaxUsageEn = "ACTION <COMMAND_NAME>",
            syntaxUsageAr = "ACTION <اسم الأمر>",
            sampleCode = "START\nACTION MOVE_FORWARD\nACTION TURN_RIGHT\nACTION MOVE_FORWARD\nSTOP"
        )
    )
}
