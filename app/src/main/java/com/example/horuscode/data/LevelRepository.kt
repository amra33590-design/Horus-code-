package com.example.horuscode.data

import com.example.horuscode.engine.interpreter.InterpreterResult
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock

data class LevelValidationResult(
    val passed: Boolean,
    val feedbackEn: String,
    val feedbackAr: String
)

data class HorusLevel(
    val id: Int,
    val titleEn: String,
    val titleAr: String,
    val conceptEn: String,
    val conceptAr: String,
    val instructionsEn: String,
    val instructionsAr: String,
    val taskEn: String,
    val taskAr: String,
    val hintsEn: List<String>,
    val hintsAr: List<String>,
    val starterBlocks: List<HorusBlock>,
    val solutionBlocks: List<HorusBlock>,
    val successExplanationEn: String,
    val successExplanationAr: String,
    val xpReward: Int = 100,
    val validate: (InterpreterResult) -> LevelValidationResult
)

object LevelRepository {

    val levels: List<HorusLevel> = listOf(
        // Level 1: The First Spark
        HorusLevel(
            id = 1,
            titleEn = "The First Spark",
            titleAr = "الشرارة الأولى",
            conceptEn = "Every program has a beginning and an end. Instructions execute sequentially from top to bottom.",
            conceptAr = "كل برنامج حاسوبي له بداية ونهاية محددتان. تسري الأوامر بتتابع متسلسل من البداية إلى النهاية.",
            instructionsEn = "Use the sacred Ankh ☥ (START / STOP) to define your program's boundary, and the Papyrus scroll 𓏛 (PRINT) to display 'Horus'.",
            instructionsAr = "استخدم رمز العنخ ☥ (START و STOP) لضبط حدود البرنامج، ولفافة البردي 𓏛 (PRINT) لكتابة 'Horus'.",
            taskEn = "Assemble a program with START, PRINT \"Horus\", and STOP.",
            taskAr = "ركّب برنامجاً يحتوي على START، ثم PRINT \"Horus\"، ثم STOP.",
            hintsEn = listOf(
                "Ensure START is the first block and STOP is the last block.",
                "Drag or add the PRINT block between START and STOP."
            ),
            hintsAr = listOf(
                "تأكد أن START هي الكتلة الأولى وSTOP هي الكتلة الأخيرة.",
                "أضف كتلة PRINT بين البداية والنهاية وضبط النص على \"Horus\"."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Hello World\"")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Horus\"")),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Excellent! You established a valid sequential program flow with start, print, and stop instructions.",
            successExplanationAr = "رائع جداً! لقد نجحت في بناء تسلسل برمجي سليم يبدأ وينتهي بتتابع منضبط.",
            validate = { result ->
                if (!result.isSuccess) {
                    LevelValidationResult(false, "Program failed with an error.", "فشل تنفيذ البرنامج بسبب خطأ.")
                } else if (result.outputs.any { it.contains("Horus", ignoreCase = true) }) {
                    LevelValidationResult(true, "Level 1 passed! You mastered basic program structure.", "اجتزت المستوى الأول بنجاح! أتقنت الهيكل الأساسي للبرنامج.")
                } else {
                    LevelValidationResult(false, "Output must contain 'Horus'. Currently printed: ${result.outputs}", "يجب أن تحتوي المخرجات على 'Horus'. المخرجات الحالية: ${result.outputs}")
                }
            }
        ),

        // Level 2: Words of Horus
        HorusLevel(
            id = 2,
            titleEn = "Words of Horus",
            titleAr = "كلمات حورس",
            conceptEn = "Programs communicate with users through Output. In Horus Code, the Papyrus Scroll 𓏛 (PRINT) evaluates expressions and displays results.",
            conceptAr = "تتواصل البرامج مع المستخدم عبر المخرجات. في شفرة حورس، تقوم لفافة البردي 𓏛 (PRINT) بتقييم التعبيرات وعرض النتائج.",
            instructionsEn = "Inscribe two outputs: first print \"Wisdom of Kemet\", then calculate and print 7 * 6.",
            instructionsAr = "اكتب مخرجين: الأول يطبع النص \"Wisdom of Kemet\"، والثاني يحسب ويطبع نتيجة 7 * 6.",
            taskEn = "Print two lines: \"Wisdom of Kemet\" and 42.",
            taskAr = "اطبع سطرين: \"Wisdom of Kemet\" ثم ناتج الحساب 42.",
            hintsEn = listOf(
                "You can put math expressions directly in PRINT, like 7 * 6.",
                "Use double quotes around text strings."
            ),
            hintsAr = listOf(
                "يمكنك وضع التعبير الحسابي مباشرة داخل PRINT مثل 7 * 6.",
                "استخدم علامات الاقتباس حول النصوص."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Wisdom of Kemet\"")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Wisdom of Kemet\"")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "7 * 6")),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Great work! You demonstrated that PRINT can output both string literals and evaluated arithmetic.",
            successExplanationAr = "عمل ممتاز! أثبتت أن أمر الطباعة يستطيع إخراج النصوص والعمليات الحسابية المحسوبة.",
            validate = { result ->
                if (!result.isSuccess) {
                    LevelValidationResult(false, "Program failed.", "فشل تنفيذ البرنامج.")
                } else {
                    val hasText = result.outputs.any { it.contains("Wisdom", ignoreCase = true) }
                    val hasMath = result.outputs.any { it.contains("42") }
                    if (hasText && hasMath) {
                        LevelValidationResult(true, "Level 2 passed! Perfect string and math output.", "اجتزت المستوى 2 بنجاح! مخرجات نصية وحسابية متقنة.")
                    } else {
                        LevelValidationResult(false, "Expected output containing 'Wisdom' and '42'. Outputs: ${result.outputs}", "المطلوب مخرجات تحتوي على 'Wisdom' و '42'. المخرجات الحالية: ${result.outputs}")
                    }
                }
            }
        ),

        // Level 3: The Djed Memory
        HorusLevel(
            id = 3,
            titleEn = "The Djed Memory",
            titleAr = "ذاكرة الجد",
            conceptEn = "Variables are named memory boxes that hold values. The Djed pillar 𓋹 (SET) allocates and modifies variable states.",
            conceptAr = "المتغيرات هي حاويات تخزين في الذاكرة تحمل قيماً. عمود جد 𓋹 (SET) يخصص قيم المتغيرات ويعدلها.",
            instructionsEn = "Create a variable named 'power' with value 50, then update 'power' to power + 25, and PRINT power.",
            instructionsAr = "أنشئ متغيراً باسم 'power' بقيمة 50، ثم حدّث قيمة 'power' إلى power + 25، واطبع 'power'.",
            taskEn = "Set power = 50, then power = power + 25, and PRINT power.",
            taskAr = "عيّن power = 50، ثم power = power + 25، واطبع المتغير power.",
            hintsEn = listOf(
                "Use two SET blocks for the 'power' variable.",
                "In the second SET block, write val: power + 25."
            ),
            hintsAr = listOf(
                "استخدم كتلتين من نوع SET للمتغير 'power'.",
                "في الكتلة الثانية اكتب التعبير: power + 25."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "power", "val" to "50")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "power", "val" to "50")),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "power", "val" to "power + 25")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "power")),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Impressive! You successfully declared and mutated a variable in program memory.",
            successExplanationAr = "مبهر! لقد نجحت في تعريف متغير وتعديل قيمته داخل ذاكرة البرنامج.",
            validate = { result ->
                val powerVal = result.variables["power"]?.asNumber()
                val printed75 = result.outputs.any { it.trim() == "75" }
                if (powerVal == 75.0 && printed75) {
                    LevelValidationResult(true, "Level 3 passed! Variable state correctly computed to 75.", "اجتزت المستوى 3 بنجاح! تم احتساب قيمة المتغير بدقة (75).")
                } else {
                    LevelValidationResult(false, "Expected power to be 75 and printed. Current variable: $powerVal, printed: ${result.outputs}", "المطلوب أن يكون power مساوياً 75 ومطبوعاً. القيمة الحالية: $powerVal، والمخرجات: ${result.outputs}")
                }
            }
        ),

        // Level 4: The Eye of Judgment
        HorusLevel(
            id = 4,
            titleEn = "The Eye of Judgment",
            titleAr = "عين الحِكمة والقرار",
            conceptEn = "Computers make decisions using conditions. The Eye of Horus 𓂀 (IF / ELSE) guides execution down different paths.",
            conceptAr = "تتخذ الحواسيب قراراتها عبر الشروط المنطقية. عين حورس 𓂀 (IF / ELSE) توجه مسار البرنامج بناء على صحة الشرط.",
            instructionsEn = "Set score = 85. Check IF score >= 60. If true, PRINT \"Passed the Trial\"; ELSE PRINT \"Needs Practice\". End with END_IF.",
            instructionsAr = "عيّن score = 85. افحص الشرط IF score >= 60. إذا تحقق اطبع \"Passed the Trial\" وإلا ELSE اطبع \"Needs Practice\".",
            taskEn = "Build an IF-ELSE decision structure where score = 85.",
            taskAr = "ابنِ هيكل قرار شرطي IF-ELSE مع المتغير score = 85.",
            hintsEn = listOf(
                "Use IF score >= 60, followed by PRINT \"Passed the Trial\".",
                "Follow that with an ELSE block, then PRINT \"Needs Practice\", and finally END_IF."
            ),
            hintsAr = listOf(
                "استخدم IF score >= 60 متبوعة بـ PRINT \"Passed the Trial\".",
                "أتبع ذلك بكتلة ELSE ثم PRINT \"Needs Practice\" ثم أغلق بـ END_IF."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "score", "val" to "85")),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "score", "op" to ">=", "right" to "60")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Passed the Trial\"")),
                HorusBlock(type = BlockType.ELSE),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Needs Practice\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "score", "val" to "85")),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "score", "op" to ">=", "right" to "60")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Passed the Trial\"")),
                HorusBlock(type = BlockType.ELSE),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Needs Practice\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Brilliant! The Eye of Horus successfully evaluated the conditional branch.",
            successExplanationAr = "عبقري! قيمت عين حورس الشرط المنطقي واختارت المسار الصحيح بدقة.",
            validate = { result ->
                if (result.outputs.any { it.contains("Passed the Trial", ignoreCase = true) } &&
                    !result.outputs.any { it.contains("Needs Practice", ignoreCase = true) }) {
                    LevelValidationResult(true, "Level 4 passed! Correct conditional branch executed.", "اجتزت المستوى 4 بنجاح! نُفذ الفرع الشرطي الصحيح.")
                } else {
                    LevelValidationResult(false, "Expected only 'Passed the Trial' in output. Outputs: ${result.outputs}", "المطلوب طباعة 'Passed the Trial' فقط. المخرجات: ${result.outputs}")
                }
            }
        ),

        // Level 5: The Scarab's Path
        HorusLevel(
            id = 5,
            titleEn = "The Scarab's Path",
            titleAr = "مسار الجعران",
            conceptEn = "Loops automate repetitive tasks without repeating code lines. The Scarab 𓆣 (REPEAT) cycles instructions efficiently.",
            conceptAr = "تختصر الحلقات التكرارية المهام المكررة دون إعادة كتابة الكود. الجعران 𓆣 (REPEAT) يدير تكرار الأوامر بفاعلية.",
            instructionsEn = "Use REPEAT 4 to print \"Step of the Scarab\" four times, enclosed with END_REPEAT.",
            instructionsAr = "استخدم REPEAT 4 لطباعة \"Step of the Scarab\" أربع مرات، واختم بـ END_REPEAT.",
            taskEn = "Run a loop of 4 iterations printing \"Step of the Scarab\".",
            taskAr = "أنشئ حلقة تكرار من 4 دورات تطبع \"Step of the Scarab\".",
            hintsEn = listOf(
                "Place REPEAT 4 before the PRINT block.",
                "Ensure END_REPEAT is placed immediately after the PRINT block."
            ),
            hintsAr = listOf(
                "ضع REPEAT 4 قبل كتلة PRINT.",
                "تأكد من وضع END_REPEAT مباشرة بعد كتلة PRINT."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "2")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Step of the Scarab\"")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "4")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Step of the Scarab\"")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Superb! You harnessed the Scarab's cycle to eliminate redundant manual instructions.",
            successExplanationAr = "رائع! استثمرت دورة الجعران التكرارية لاختصار الكود وتنفيذ المهام التكرارية.",
            validate = { result ->
                val count = result.outputs.count { it.contains("Step of the Scarab", ignoreCase = true) }
                if (count == 4) {
                    LevelValidationResult(true, "Level 5 passed! Loop executed exactly 4 times.", "اجتزت المستوى 5 بنجاح! نُفذت الحلقة 4 مرات تماماً.")
                } else {
                    LevelValidationResult(false, "Expected 4 printed lines, but got $count.", "المطلوب 4 أسطر مطبوعة، ولكن تم تسجيل $count.")
                }
            }
        ),

        // Level 6: The Oracle's Input
        HorusLevel(
            id = 6,
            titleEn = "The Oracle's Input",
            titleAr = "مدخلات العرّاف",
            conceptEn = "Interactive programs receive input from users to calculate dynamic results. The Feather of Ma'at 𓋖 (INPUT) captures user data.",
            conceptAr = "تستقبل البرامج التفاعلية مدخلات من المستخدمين لحساب نتائج متبدلة. ريشة ماعت 𓋖 (INPUT) تستقبل البيانات.",
            instructionsEn = "Use INPUT to receive a number into variable 'energy'. Calculate 'boosted' = energy * 2, and PRINT boosted.",
            instructionsAr = "استخدم INPUT لاستقبال رقم في المتغير 'energy'. ثم احسب 'boosted' = energy * 2، واطبع الناتج.",
            taskEn = "Input energy, double it into boosted, and output boosted.",
            taskAr = "استقبل energy، وضاعفها في متغير boosted، واطبع boosted.",
            hintsEn = listOf(
                "Use INPUT block with var: energy.",
                "Follow with SET boosted = energy * 2, then PRINT boosted."
            ),
            hintsAr = listOf(
                "استخدم كتلة INPUT مع اسم المتغير energy.",
                "ثم استخدم SET boosted = energy * 2، ثم PRINT boosted."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.INPUT, params = mapOf("prompt" to "Enter energy", "var" to "energy")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.INPUT, params = mapOf("prompt" to "Enter energy", "var" to "energy")),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "boosted", "val" to "energy * 2")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "boosted")),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Outstanding! You created an interactive data processing pipeline with dynamic user input.",
            successExplanationAr = "متميز! بنيت مساراً لمعالجة البيانات التفاعلية من مدخلات المستخدم.",
            validate = { result ->
                val boostedVal = result.variables["boosted"]?.asNumber()
                val energyVal = result.variables["energy"]?.asNumber() ?: 0.0
                if (boostedVal != null && boostedVal == energyVal * 2.0 && result.outputs.isNotEmpty()) {
                    LevelValidationResult(true, "Level 6 passed! Dynamic input accurately doubled to $boostedVal.", "اجتزت المستوى 6 بنجاح! تمت مضاعفة المدخل بدقة إلى $boostedVal.")
                } else {
                    LevelValidationResult(false, "Expected boosted = energy * 2 and output printed.", "المطلوب حساب boosted = energy * 2 وطباعة الناتج.")
                }
            }
        ),

        // Level 7: The Temple of Logic
        HorusLevel(
            id = 7,
            titleEn = "The Temple of Logic",
            titleAr = "معبد المنطق الشامل",
            conceptEn = "Real software combines variables, loops, and conditions to solve complex algorithmic problems.",
            conceptAr = "تجمع البرمجيات الحقيقية بين المتغيرات والحلقات والشروط لحل المشكلات المعقدة بكفاءة.",
            instructionsEn = "Initialize gold = 0. Repeat 3 times: gold = gold + 10. After the loop, IF gold >= 30 PRINT \"Sacred Treasury Full\" END_IF.",
            instructionsAr = "عيّن gold = 0. كرر 3 مرات: gold = gold + 10. بعد الحلقة، افحص إذا كان gold >= 30 اطبع \"Sacred Treasury Full\".",
            taskEn = "Accumulate gold to 30 via loop, then trigger condition.",
            taskAr = "اجمع الذهب ليصل إلى 30 عبر حلقة تكرار، ثم أطلق التنبيه الشرطي.",
            hintsEn = listOf(
                "SET gold = 0 before the REPEAT block.",
                "Inside REPEAT 3, put SET gold = gold + 10.",
                "Put IF gold >= 30 after END_REPEAT."
            ),
            hintsAr = listOf(
                "عيّن gold = 0 قبل حلقة REPEAT.",
                "داخل REPEAT 3 ضع SET gold = gold + 10.",
                "ضع شرط IF gold >= 30 بعد END_REPEAT."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "gold", "val" to "0")),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "3")),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "gold", "val" to "gold + 10")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "gold", "val" to "0")),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "3")),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "gold", "val" to "gold + 10")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "gold", "op" to ">=", "right" to "30")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Sacred Treasury Full\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Masterful! You synthesized accumulation, loops, and conditional decision-making seamlessly.",
            successExplanationAr = "إتقان باهر! جمعت بين التراكم الحسابي والحلقات وصنع القرار الشرطي بسلاسة.",
            validate = { result ->
                val goldVal = result.variables["gold"]?.asNumber()
                val printedTreasury = result.outputs.any { it.contains("Sacred Treasury Full", ignoreCase = true) }
                if (goldVal == 30.0 && printedTreasury) {
                    LevelValidationResult(true, "Level 7 passed! Full algorithm executed successfully.", "اجتزت المستوى 7 بنجاح! تم تنفيذ الخوارزمية المتكاملة بنجاح تام.")
                } else {
                    LevelValidationResult(false, "Expected gold=30 and 'Sacred Treasury Full' printed. gold=$goldVal, outputs=${result.outputs}", "المطلوب وصول gold إلى 30 وطباعة 'Sacred Treasury Full'. الحالي: gold=$goldVal والمخرجات=${result.outputs}")
                }
            }
        ),

        // Level 8: The Final Trial
        HorusLevel(
            id = 8,
            titleEn = "The Final Trial",
            titleAr = "الاختبار الأخير",
            conceptEn = "The ultimate test of algorithmic logic: Simulating an energy depletion challenge and activating the ancient temple guardian.",
            conceptAr = "الاختبار الأخير في المنطق الخوارزمي: محاكاة استنزاف الطاقة الحسابي وإيقاظ حارس المعبد القديم.",
            instructionsEn = "Set energy = 100. In a loop of 5 iterations, subtract 15 each time (energy = energy - 15). After loop, IF energy > 20 PRINT \"Guardian Awakened\" ELSE PRINT \"Recharge Needed\" END_IF.",
            instructionsAr = "عيّن energy = 100. في حلقة من 5 دورات، اطرح 15 في كل دورة (energy = energy - 15). بعد الحلقة، إذا كانت energy > 20 اطبع \"Guardian Awakened\" وإلا \"Recharge Needed\".",
            taskEn = "Calculate residual energy and activate the guardian.",
            taskAr = "احسب الطاقة المتبقية وأيقظ حارس المعبد.",
            hintsEn = listOf(
                "Energy starts at 100.",
                "100 - (5 * 15) = 25.",
                "Since 25 > 20, the Guardian Awakened branch will execute."
            ),
            hintsAr = listOf(
                "تبدأ الطاقة بـ 100.",
                "100 - (5 * 15) = 25.",
                "بما أن 25 > 20، سيتم تنفيذ فرع إيقاظ الحارس 'Guardian Awakened'."
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "energy", "val" to "100")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "energy", "val" to "100")),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "5")),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "energy", "val" to "energy - 15")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "energy", "op" to ">", "right" to "20")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Guardian Awakened\"")),
                HorusBlock(type = BlockType.ELSE),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Recharge Needed\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.STOP)
            ),
            successExplanationEn = "Victory! You have conquered the Final Trial and proved your mastery over the fundamental logic of Horus Code!",
            successExplanationAr = "انتصار مبين! لقد اجتزت الاختبار الأخير وأثبتت تمكنك الفائق من المنطق البرمجي لشفرة حورس!",
            validate = { result ->
                val energyVal = result.variables["energy"]?.asNumber()
                val awakened = result.outputs.any { it.contains("Guardian Awakened", ignoreCase = true) }
                if (energyVal == 25.0 && awakened) {
                    LevelValidationResult(true, "Level 8 passed! You are now a certified Horus Code Architect!", "اجتزت المستوى 8 بنجاح! أصبحت الآن مهندس شفرة حورس المعتمد!")
                } else {
                    LevelValidationResult(false, "Expected final energy=25 and 'Guardian Awakened' printed. energy=$energyVal, outputs=${result.outputs}", "المطلوب وصول energy إلى 25 وطباعة 'Guardian Awakened'. الحالي: energy=$energyVal والمخرجات=${result.outputs}")
                }
            }
        )
    )
}
