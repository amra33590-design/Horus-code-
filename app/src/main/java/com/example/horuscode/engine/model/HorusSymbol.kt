package com.example.horuscode.engine.model

enum class SymbolCategory {
    CORE,
    VARIABLES,
    CONDITIONS,
    LOOPS,
    IO,
    ACTIONS
}

enum class HorusSymbolType(
    val glyph: String,
    val enName: String,
    val arName: String,
    val defaultKeyword: String,
    val category: SymbolCategory,
    val colorHex: Long,
    val authenticHistoryEn: String,
    val authenticHistoryAr: String,
    val modernMeaningEn: String,
    val modernMeaningAr: String
) {
    ANKH(
        glyph = "☥",
        enName = "Ankh",
        arName = "عنخ (مفتاح الحياة)",
        defaultKeyword = "START",
        category = SymbolCategory.CORE,
        colorHex = 0xFFFFD700, // Gold
        authenticHistoryEn = "In ancient Egypt, the Ankh represented physical life, divine breath, and eternal vitality.",
        authenticHistoryAr = "في مصر القديمة، كان العنخ رمزاً للحياة المادية والروحية ونفحة الخلود الإلهية.",
        modernMeaningEn = "[Modern Horus Code Assignment] Program initiation and lifecycle: Marks the sacred entry point and clean termination of instructions.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] بدء دورة حياة البرنامج ونقطة الانطلاق لتنفيذ الأوامر وانتهائها بأمان."
    ),
    DJED(
        glyph = "𓋹",
        enName = "Djed Pillar",
        arName = "عمود جد",
        defaultKeyword = "SET",
        category = SymbolCategory.VARIABLES,
        colorHex = 0xFF00D2D3, // Turquoise
        authenticHistoryEn = "A pillar-like symbol associated with the god Osiris, symbolizing stability, endurance, and backbone.",
        authenticHistoryAr = "رمز يحاكي العمود الفقري لأوزوريس، ويرمز للاستقرار والثبات والصلابة.",
        modernMeaningEn = "[Modern Horus Code Assignment] Variable storage: Represents stable memory cells that hold values and maintain program state.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] الذاكرة والمتغيرات: يحفظ القيم الرقمية والنصية في خلايا ذاكرة مستقرة للاسترجاع والتعديل."
    ),
    EYE_OF_HORUS(
        glyph = "𓂀",
        enName = "Eye of Horus (Wedjat)",
        arName = "عين حورس (الوجات)",
        defaultKeyword = "IF",
        category = SymbolCategory.CONDITIONS,
        colorHex = 0xFF48DBFB, // Cyan
        authenticHistoryEn = "A sacred symbol of protection, whole-ness, restoration, and keen discernment.",
        authenticHistoryAr = "رمز مقدس للحماية والشفاء والاسترداد والبصيرة الثاقبة ووزن الأمور بمقاييس دقيقة.",
        modernMeaningEn = "[Modern Horus Code Assignment] Conditional evaluation: Evaluates true or false tests to discern which path the program follows.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] الشروط المنطقية: تقييم الشروط (صواب أو خطأ) لاتخاذ القرارات وتوجيه مسار التنفيذ."
    ),
    SCARAB(
        glyph = "𓆣",
        enName = "Scarab (Khepri)",
        arName = "الجعران (خبري)",
        defaultKeyword = "REPEAT",
        category = SymbolCategory.LOOPS,
        colorHex = 0xFFE5A93C, // Amber Gold
        authenticHistoryEn = "Represented cyclical rebirth, continuous transformation, and the rolling of the sun across the sky.",
        authenticHistoryAr = "رمز التحول المستمر والتجدد ودورة شروق الشمس اليومية بحركة دائرية متكررة.",
        modernMeaningEn = "[Modern Horus Code Assignment] Repetition & Loops: Automates repetitive tasks by cycling instructions a specified number of times.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] الحلقات التكرارية: تكرار تنفيذ كتلة من الأوامر لعدد محدد من الدورات المنظمة."
    ),
    PAPYRUS(
        glyph = "𓏛",
        enName = "Papyrus Scroll",
        arName = "لفافة البردي",
        defaultKeyword = "PRINT",
        category = SymbolCategory.IO,
        colorHex = 0xFF9AECDB, // Mint cyan
        authenticHistoryEn = "The medium of record-keeping, legal wisdom, literature, and official communication in Kemet.",
        authenticHistoryAr = "أداة التدوين والمعرفة وتخليد الحكمة والمراسلات الرسمية في الحضارة المصرية.",
        modernMeaningEn = "[Modern Horus Code Assignment] Standard Output: Inscribes messages, calculations, and results onto the console screen.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] الإخراج: طباعة النصوص ونتائج العمليات الحسابية على شاشة المنصة."
    ),
    FEATHER(
        glyph = "𓋖",
        enName = "Feather of Ma'at",
        arName = "ريشة ماعت",
        defaultKeyword = "INPUT",
        category = SymbolCategory.IO,
        colorHex = 0xFFF8A5C2, // Soft Coral
        authenticHistoryEn = "Represented truth, cosmic order, moral balance, and universal justice against which hearts were weighed.",
        authenticHistoryAr = "رمز الحقيقة والنظام الكوني والعدالة وتوازن الموازين الأخلاقية.",
        modernMeaningEn = "[Modern Horus Code Assignment] User Input: Receives values, numbers, and inputs from the human user to weigh into calculations.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] المدخلات: استقبال بيانات ومدخلات من المستخدم والتفاعل معها بمرونة."
    ),
    PYRAMIDION(
        glyph = "𓊗",
        enName = "Pyramidion (Benben)",
        arName = "الهريم (بن بن)",
        defaultKeyword = "ACTION",
        category = SymbolCategory.ACTIONS,
        colorHex = 0xFFF39C12, // Bright Gold
        authenticHistoryEn = "The capstone of obelisks and pyramids that captured the first rays of the dawn sun.",
        authenticHistoryAr = "قمة الهرم أو المسلة، أول ما تعانقه أشعة الشمس عند مطلع الفجر للدلالة على الإنجاز.",
        modernMeaningEn = "[Modern Horus Code Assignment] Physical and maze actions: Dispatches movement and temple commands like MOVE, TURN, or ACTIVATE.",
        modernMeaningAr = "[توظيف حديث في شفرة حورس] أوامر الحركة والتحكم: تنفيذ الإجراءات الملموسة كالحركة والدوران وفتح البوابات."
    )
}
