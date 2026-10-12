package com.example.horuscode.data

import com.example.horuscode.engine.interpreter.InterpreterResult
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock

data class DebuggingChallenge(
    val id: Int,
    val titleEn: String,
    val titleAr: String,
    val bugCategoryEn: String,
    val bugCategoryAr: String,
    val descriptionEn: String,
    val descriptionAr: String,
    val intendedOutcomeEn: String,
    val intendedOutcomeAr: String,
    val brokenBlocks: List<HorusBlock>,
    val errorExplanationEn: String,
    val errorExplanationAr: String,
    val fixHintEn: String,
    val fixHintAr: String,
    val xpReward: Int = 80,
    val validate: (InterpreterResult) -> Pair<Boolean, String>
)

object DebuggingRepository {

    val challenges: List<DebuggingChallenge> = listOf(
        // Challenge 1: The Misnamed Spell (Misspelled / Broken Command)
        DebuggingChallenge(
            id = 1,
            titleEn = "The Misnamed Spell",
            titleAr = "التعويذة المحرفة",
            bugCategoryEn = "Syntax & Parameter Error",
            bugCategoryAr = "خطأ في الصياغة والمعاملات",
            descriptionEn = "The scribe attempted to display 'Temple of Horus', but used an invalid print expression with missing quotes.",
            descriptionAr = "حاول الكاتب طباعة 'Temple of Horus' لكنه نسي علامات الاقتباس، مما جعل المفسر يعتبرها متغيراً غير موجود.",
            intendedOutcomeEn = "Print the exact string: Temple of Horus",
            intendedOutcomeAr = "طباعة النص الصريح: Temple of Horus",
            brokenBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "Temple_of_Horus")), // Missing quotes!
                HorusBlock(type = BlockType.STOP)
            ),
            errorExplanationEn = "Without quotes, the interpreter looks for a memory variable named 'Temple_of_Horus' and fails.",
            errorExplanationAr = "بدون علامات الاقتباس، يبحث المفسر عن متغير بالذاكرة بهذا الاسم ولا يجده فيتوقف البرنامج.",
            fixHintEn = "Edit the PRINT block expression to wrap the text in quotes: \"Temple of Horus\".",
            fixHintAr = "عدل تعبير كتلة الطباعة PRINT وضع النص بين علامتي اقتباس: \"Temple of Horus\".",
            validate = { result ->
                if (result.isSuccess && result.outputs.any { it.contains("Temple of Horus", ignoreCase = true) }) {
                    Pair(true, "Fixed! You properly designated a string literal with quotes.")
                } else {
                    Pair(false, "Program must successfully output 'Temple of Horus'.")
                }
            }
        ),

        // Challenge 2: The Ghost Variable (Undefined Variable)
        DebuggingChallenge(
            id = 2,
            titleEn = "The Ghost Variable",
            titleAr = "المتغير الشبح",
            bugCategoryEn = "Undefined Variable Reference",
            bugCategoryAr = "استدعاء متغير غير معرف في الذاكرة",
            descriptionEn = "The program attempts to calculate bonus points using a variable named 'score', but forgot to allocate 'score' with SET 𓋹 first!",
            descriptionAr = "يحاول البرنامج احتساب نقاط إضافية بالاعتماد على متغير اسمه 'score'، لكنه نسي تعريفه بكتلة SET 𓋹 أولاً!",
            intendedOutcomeEn = "Initialize score = 50, then calculate total = score + 20, and output 70.",
            intendedOutcomeAr = "تعيين score = 50 أولاً، ثم حساب total = score + 20، وطباعة الناتج 70.",
            brokenBlocks = listOf(
                HorusBlock(type = BlockType.START),
                // Missing: SET score = 50
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "total", "val" to "score + 20")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "total")),
                HorusBlock(type = BlockType.STOP)
            ),
            errorExplanationEn = "Variable 'score' was referenced before any SET block created it in memory.",
            errorExplanationAr = "تم استدعاء المتغير 'score' في عملية حسابية قبل تعيينه في الذاكرة بكتلة SET.",
            fixHintEn = "Insert a SET block before 'total' that sets score = 50.",
            fixHintAr = "أضف كتلة SET قبل 'total' تقوم بتعيين score = 50.",
            validate = { result ->
                val total = result.variables["total"]?.asNumber()
                if (result.isSuccess && total == 70.0 && result.outputs.any { it.trim() == "70" }) {
                    Pair(true, "Fixed! Variable 'score' defined prior to calculation.")
                } else {
                    Pair(false, "Ensure score is set to 50 and total is printed as 70.")
                }
            }
        ),

        // Challenge 3: The Broken Balance (Inverted Condition)
        DebuggingChallenge(
            id = 3,
            titleEn = "The Broken Balance",
            titleAr = "الميزان المقلوب",
            bugCategoryEn = "Logic Flaw in Comparison",
            bugCategoryAr = "خطأ منطقي في إشارة المقارنة",
            descriptionEn = "The trial should praise initiates who achieve 80 or more points. However, the comparison was inverted to '< 80', producing the wrong verdict!",
            descriptionAr = "يجب أن يكافئ الاختبار الطلاب الحاصلين على 80 نقطة فأكثر. لكن إشارة المقارنة عُكست إلى '< 80' فأعطت حكماً مقلوباً!",
            intendedOutcomeEn = "When score = 85, print 'Blessed by Horus' (not 'Try Again').",
            intendedOutcomeAr = "عندما يكون score = 85، يجب طباعة 'Blessed by Horus' بدلاً من 'Try Again'.",
            brokenBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "score", "val" to "85")),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "score", "op" to "<", "right" to "80")), // Inverted!
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Blessed by Horus\"")),
                HorusBlock(type = BlockType.ELSE),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Try Again\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.STOP)
            ),
            errorExplanationEn = "Checking 'score < 80' triggers the ELSE branch when score is 85.",
            errorExplanationAr = "فحص 'score < 80' يجعل البرنامج يتجه لفرع ELSE عند حصول الطالب على 85.",
            fixHintEn = "Change the comparison operator in the IF block from '<' to '>='.",
            fixHintAr = "غير معامل المقارنة في كتلة IF من '<' إلى '>='.",
            validate = { result ->
                if (result.isSuccess && result.outputs.any { it.contains("Blessed by Horus", ignoreCase = true) } &&
                    !result.outputs.any { it.contains("Try Again", ignoreCase = true) }) {
                    Pair(true, "Fixed! The Eye of Horus comparison logic is restored.")
                } else {
                    Pair(false, "Expected output: Blessed by Horus.")
                }
            }
        ),

        // Challenge 4: The Unclosed Portal (Missing Structural Block)
        DebuggingChallenge(
            id = 4,
            titleEn = "The Unclosed Portal",
            titleAr = "البوابة المفتوحة",
            bugCategoryEn = "Invalid Block Nesting",
            bugCategoryAr = "عدم إغلاق البنية التركيبية للشرط",
            descriptionEn = "An IF block was opened, but the scribe forgot the closing END_IF block before STOP, causing a structural parsing syntax error.",
            descriptionAr = "فُتحت كتلة شرط IF ولكن نسي الكاتب وضع كتلة الإغلاق END_IF قبل STOP، مما سبب خطأ فادحاً في البنية التركيبية.",
            intendedOutcomeEn = "Valid structure that checks if energy > 10, prints 'Active', and cleanly terminates.",
            intendedOutcomeAr = "بنية سليمة تفحص if energy > 10 وتطبع 'Active' ثم تغلق بـ END_IF وتتوقف بسلام.",
            brokenBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "energy", "val" to "20")),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "energy", "op" to ">", "right" to "10")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Active\"")),
                // Missing: END_IF block!
                HorusBlock(type = BlockType.STOP)
            ),
            errorExplanationEn = "Every IF decision must have a corresponding END_IF delimiter.",
            errorExplanationAr = "كل كتلة قرار شرطي IF يجب أن تتبعها كتلة إغلاق END_IF مقابلة.",
            fixHintEn = "Add an END_IF block after PRINT \"Active\" and before STOP.",
            fixHintAr = "أضف كتلة END_IF بعد أمر الطباعة PRINT وقبل كتلة STOP.",
            validate = { result ->
                if (result.isSuccess && result.outputs.any { it.contains("Active", ignoreCase = true) }) {
                    Pair(true, "Fixed! Structural integrity of the conditional scope verified.")
                } else {
                    Pair(false, "Add END_IF and execute cleanly.")
                }
            }
        ),

        // Challenge 5: The Scarab's Stride (Incorrect Loop Count)
        DebuggingChallenge(
            id = 5,
            titleEn = "The Scarab's Stride",
            titleAr = "خطوات الجعران",
            bugCategoryEn = "Off-by-One Loop Count",
            bugCategoryAr = "خطأ في عدد دورات حلقة التكرار",
            descriptionEn = "The builder needs 5 stones laid, but the loop was configured to repeat only 2 times.",
            descriptionAr = "يحتاج البناء إلى رصف 5 أحجار في المعبد، لكن حلقة التكرار ضبطت على دورتين فقط.",
            intendedOutcomeEn = "Print 'Stone Laid' exactly 5 times.",
            intendedOutcomeAr = "طباعة عبارة 'Stone Laid' 5 مرات تماماً.",
            brokenBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "2")), // Should be 5!
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Stone Laid\"")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            ),
            errorExplanationEn = "Loop configured for 2 iterations instead of the required 5 iterations.",
            errorExplanationAr = "تم ضبط الحلقة على دورتين فقط بدلاً من الدورات الخمس المطلوبة.",
            fixHintEn = "Change the times parameter of the REPEAT block from 2 to 5.",
            fixHintAr = "عدل معامل عدد التكرار في كتلة REPEAT من 2 إلى 5.",
            validate = { result ->
                val count = result.outputs.count { it.contains("Stone Laid", ignoreCase = true) }
                if (result.isSuccess && count == 5) {
                    Pair(true, "Fixed! The Scarab laid exactly 5 stones as commanded.")
                } else {
                    Pair(false, "Loop must execute exactly 5 times (currently: $count).")
                }
            }
        )
    )
}
