package com.example.horuscode.engine.model

import java.util.UUID

enum class BlockType(
    val symbolType: HorusSymbolType,
    val keyword: String,
    val hieroglyphicKeyword: String,
    val labelEn: String,
    val labelAr: String,
    val ancientMeaningAr: String,
    val programmingReasonAr: String,
    val isControlBlock: Boolean = false,
    val isClosingBlock: Boolean = false
) {
    START(
        symbolType = HorusSymbolType.ANKH,
        keyword = "START",
        hieroglyphicKeyword = "☥",
        labelEn = "Ankh: Start Life",
        labelAr = "☥ عنخ: بدء حياة البرنامج",
        ancientMeaningAr = "رمز الحياة ونفحة الخلود عند قدماء المصريين",
        programmingReasonAr = "يبدأ دورة حياة البرنامج ويفتح بوابة تنفيذ الأوامر"
    ),
    STOP(
        symbolType = HorusSymbolType.ANKH,
        keyword = "STOP",
        hieroglyphicKeyword = "☥_ختام",
        labelEn = "Ankh: Terminate",
        labelAr = "☥ عنخ: ختام حياة البرنامج",
        ancientMeaningAr = "اكتمال دورة الحياة بأمان وسلام",
        programmingReasonAr = "يغلق البرنامج بأمان ويوقف تنفيذ التعليمات"
    ),
    SET(
        symbolType = HorusSymbolType.DJED,
        keyword = "SET",
        hieroglyphicKeyword = "𓋹",
        labelEn = "Djed: Store & Stabilize",
        labelAr = "𓋹 عمود جد: تثبيت المتغير في الذاكرة",
        ancientMeaningAr = "رمز الاستقرار والصلابة وعمود أوزوريس الفقري",
        programmingReasonAr = "يمنح القيمة الاستقرار والثبات في خلايا الذاكرة (المتغيرات) ويسمح بتعديلها"
    ),
    PRINT(
        symbolType = HorusSymbolType.PAPYRUS,
        keyword = "PRINT",
        hieroglyphicKeyword = "𓏛",
        labelEn = "Papyrus: Inscribe Output",
        labelAr = "𓏛 لفافة البردي: تدوين وإخراج النتائج",
        ancientMeaningAr = "أداة التدوين والكتابة وحفظ الحكمة والعلوم الرسمية",
        programmingReasonAr = "يدون النصوص ونتائج العمليات الحسابية على شاشة المخرجات للمستخدم"
    ),
    INPUT(
        symbolType = HorusSymbolType.FEATHER,
        keyword = "INPUT",
        hieroglyphicKeyword = "𓋖",
        labelEn = "Ma'at: Weigh Input",
        labelAr = "𓋖 ريشة ماعت: وزن واستقبال المدخلات",
        ancientMeaningAr = "رمز الحق والعدالة والميزان الكوني ووزن القلوب",
        programmingReasonAr = "يستقبل مدخلات المستخدم ويزن قيمتها لإدخالها في حسابات البرنامج"
    ),
    IF(
        symbolType = HorusSymbolType.EYE_OF_HORUS,
        keyword = "IF",
        hieroglyphicKeyword = "𓂀",
        labelEn = "Eye of Horus: Insight & Decision",
        labelAr = "𓂀 عين حورس: بصيرة وفحص الشرط",
        ancientMeaningAr = "عين الوجات المقدسة، رمز البصيرة والحماية والشفاء ووزن المقادير",
        programmingReasonAr = "يفحص بصحة وبصيرة ما إذا كان الشرط صواباً أو خطأ ليختار المسار الصحيح",
        isControlBlock = true
    ),
    ELSE(
        symbolType = HorusSymbolType.EYE_OF_HORUS,
        keyword = "ELSE",
        hieroglyphicKeyword = "وإلا",
        labelEn = "Otherwise (Else)",
        labelAr = "𓂀 وإلا: المسار البديل",
        ancientMeaningAr = "المسار الآخر في ميزان القرار عند عدم تحقق الرؤية الأولى",
        programmingReasonAr = "ينفذ التعليمات البديلة في حال كان الشرط غير متحقق (خطأ)"
    ),
    END_IF(
        symbolType = HorusSymbolType.EYE_OF_HORUS,
        keyword = "END_IF",
        hieroglyphicKeyword = "ختام_الشرط",
        labelEn = "End If",
        labelAr = "𓂀 ختام بصيرة الشرط",
        ancientMeaningAr = "اكتمال حكم عين حورس وإغلاق ميزان القرار",
        programmingReasonAr = "يحدد نهاية كتلة القرار الشرطي",
        isClosingBlock = true
    ),
    REPEAT(
        symbolType = HorusSymbolType.SCARAB,
        keyword = "REPEAT",
        hieroglyphicKeyword = "𓆣",
        labelEn = "Scarab: Repeat Cycle",
        labelAr = "𓆣 الجعران: تكرار الدورات",
        ancientMeaningAr = "رمز التجدد والتحول ودورة شروق الشمس اليومية بحركة دائرية",
        programmingReasonAr = "يكرر الأوامر البرمجية لعدد محدد من الدورات آلياً دون كتابة مكررة",
        isControlBlock = true
    ),
    END_REPEAT(
        symbolType = HorusSymbolType.SCARAB,
        keyword = "END_REPEAT",
        hieroglyphicKeyword = "ختام_التكرار",
        labelEn = "End Repeat",
        labelAr = "𓆣 ختام دورة التكرار",
        ancientMeaningAr = "اكتمال دورات التجدد الشمسية",
        programmingReasonAr = "يحدد نهاية حلقة التكرار",
        isClosingBlock = true
    ),
    ACTION(
        symbolType = HorusSymbolType.PYRAMIDION,
        keyword = "ACTION",
        hieroglyphicKeyword = "𓊗",
        labelEn = "Pyramidion: Perform Action",
        labelAr = "𓊗 الهريم: إطلاق الأوامر المادية",
        ancientMeaningAr = "قمة الهرم والمسلة، أول ما يلامس نور الفجر وبداية الانبثاق",
        programmingReasonAr = "ينفذ الإجراءات الحركية والتفاعلية المادية في معبد المنطق"
    )
}

data class HorusBlock(
    val id: String = UUID.randomUUID().toString(),
    val type: BlockType,
    val params: Map<String, String> = emptyMap()
) {
    /**
     * Text representation using authentic hieroglyphic symbols as the primary syntax!
     */
    fun toCodeString(useHieroglyphs: Boolean = true): String {
        return if (useHieroglyphs) {
            when (type) {
                BlockType.START -> "☥ بدء"
                BlockType.STOP -> "☥ ختام"
                BlockType.SET -> {
                    val variable = params["var"] ?: "س"
                    val value = params["val"] ?: "0"
                    "𓋹 $variable = $value"
                }
                BlockType.PRINT -> {
                    val expr = params["expr"] ?: "\"\""
                    "𓏛 $expr"
                }
                BlockType.INPUT -> {
                    val prompt = params["prompt"] ?: "أدخل قيمة"
                    val variable = params["var"] ?: "المدخل"
                    "𓋖 \"$prompt\" في $variable"
                }
                BlockType.IF -> {
                    val left = params["left"] ?: "س"
                    val op = params["op"] ?: "=="
                    val right = params["right"] ?: "0"
                    "𓂀 $left $op $right"
                }
                BlockType.ELSE -> "وإلا"
                BlockType.END_IF -> "ختام_الشرط"
                BlockType.REPEAT -> {
                    val times = params["times"] ?: "3"
                    "𓆣 $times"
                }
                BlockType.END_REPEAT -> "ختام_التكرار"
                BlockType.ACTION -> {
                    val action = params["action"] ?: "MOVE_FORWARD"
                    val actionName = when (action) {
                        "MOVE_FORWARD" -> "تقدم"
                        "TURN_LEFT" -> "يسار"
                        "TURN_RIGHT" -> "يمين"
                        "COLLECT_KEY" -> "التقط_المفتاح"
                        else -> action
                    }
                    "𓊗 $actionName"
                }
            }
        } else {
            when (type) {
                BlockType.START -> "START"
                BlockType.STOP -> "STOP"
                BlockType.SET -> {
                    val variable = params["var"] ?: "x"
                    val value = params["val"] ?: "0"
                    "SET $variable = $value"
                }
                BlockType.PRINT -> {
                    val expr = params["expr"] ?: "\"\""
                    "PRINT $expr"
                }
                BlockType.INPUT -> {
                    val prompt = params["prompt"] ?: "Enter value"
                    val variable = params["var"] ?: "val"
                    "INPUT \"$prompt\" INTO $variable"
                }
                BlockType.IF -> {
                    val left = params["left"] ?: "x"
                    val op = params["op"] ?: "=="
                    val right = params["right"] ?: "0"
                    "IF $left $op $right"
                }
                BlockType.ELSE -> "ELSE"
                BlockType.END_IF -> "END_IF"
                BlockType.REPEAT -> {
                    val times = params["times"] ?: "3"
                    "REPEAT $times"
                }
                BlockType.END_REPEAT -> "END_REPEAT"
                BlockType.ACTION -> {
                    val action = params["action"] ?: "MOVE_FORWARD"
                    "ACTION $action"
                }
            }
        }
    }

    fun getDisplayName(isArabic: Boolean): String {
        return if (isArabic) type.labelAr else type.labelEn
    }

    fun getSummary(isArabic: Boolean): String {
        return if (isArabic) {
            when (type) {
                BlockType.START -> "☥ ${type.ancientMeaningAr} ← ${type.programmingReasonAr}"
                BlockType.STOP -> "☥ ${type.ancientMeaningAr} ← ${type.programmingReasonAr}"
                BlockType.SET -> {
                    val v = params["var"] ?: "س"
                    val valExpr = params["val"] ?: "0"
                    "𓋹 تثبيت القيمة $valExpr في الذاكرة باسم '$v' (استقرار عمود جد)"
                }
                BlockType.PRINT -> {
                    val expr = params["expr"] ?: "\"\""
                    "𓏛 تدوين على شاشة البردي: $expr"
                }
                BlockType.INPUT -> {
                    val v = params["var"] ?: "س"
                    "𓋖 وزن مدخل المستخدم في ميزان ماعت وحفظه في '$v'"
                }
                BlockType.IF -> {
                    val l = params["left"] ?: "س"
                    val op = params["op"] ?: "=="
                    val r = params["right"] ?: "0"
                    "𓂀 فحص عين حورس: هل $l $op $r؟"
                }
                BlockType.ELSE -> "𓂀 وإلا: في حال لم يتحقق ميزان عين حورس"
                BlockType.END_IF -> "𓂀 ختام ميزان وبصيرة الشرط"
                BlockType.REPEAT -> {
                    val t = params["times"] ?: "3"
                    "𓆣 تدوير الأوامر $t دورات في مسار الجعران"
                }
                BlockType.END_REPEAT -> "𓆣 ختام دورات الجعران"
                BlockType.ACTION -> {
                    val a = params["action"] ?: "MOVE_FORWARD"
                    val actionAr = when (a) {
                        "MOVE_FORWARD", "تقدم" -> "تقدم للأمام خطوة"
                        "TURN_LEFT", "يسار" -> "استدر لليسار"
                        "TURN_RIGHT", "يمين" -> "استدر لليمين"
                        "COLLECT_KEY", "التقط_المفتاح" -> "التقط المفتاح الذهبي"
                        else -> a
                    }
                    "𓊗 انطلاق الأمر من الهريم: $actionAr"
                }
            }
        } else {
            when (type) {
                BlockType.START -> "Begin program lifecycle with Ankh ☥"
                BlockType.STOP -> "Clean halt of program lifecycle with Ankh ☥"
                BlockType.SET -> {
                    val v = params["var"] ?: "x"
                    val valExpr = params["val"] ?: "0"
                    "Store and stabilize $valExpr into memory variable $v via Djed 𓋹"
                }
                BlockType.PRINT -> {
                    val expr = params["expr"] ?: "\"\""
                    "Inscribe on Papyrus 𓏛: $expr"
                }
                BlockType.INPUT -> {
                    val v = params["var"] ?: "x"
                    "Weigh user input with Feather 𓋖 and store in $v"
                }
                BlockType.IF -> {
                    val l = params["left"] ?: "x"
                    val op = params["op"] ?: "=="
                    val r = params["right"] ?: "0"
                    "Eye of Horus 𓂀 evaluates: $l $op $r"
                }
                BlockType.ELSE -> "Otherwise branch if condition is false"
                BlockType.END_IF -> "End of conditional decision"
                BlockType.REPEAT -> {
                    val t = params["times"] ?: "3"
                    "Cycle enclosed instructions $t times via Scarab 𓆣"
                }
                BlockType.END_REPEAT -> "End of repetition cycle"
                BlockType.ACTION -> {
                    val a = params["action"] ?: "MOVE_FORWARD"
                    "Pyramidion 𓊗 executes action: $a"
                }
            }
        }
    }
}
