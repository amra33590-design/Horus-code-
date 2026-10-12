package com.example.horuscode.data

data class EvaluationQuestion(
    val id: Int,
    val questionEn: String,
    val questionAr: String,
    val optionsEn: List<String>,
    val optionsAr: List<String>,
    val correctIndex: Int,
    val conceptTestedEn: String,
    val conceptTestedAr: String
)

data class EvaluationTestResult(
    val score: Int, // e.g. 4
    val totalQuestions: Int, // e.g. 5
    val percentage: Int, // e.g. 80
    val userAnswers: Map<Int, Int>, // questionId -> chosenIndex
    val completedAtMillis: Long
)

object EvaluationRepository {

    val preTestQuestions: List<EvaluationQuestion> = listOf(
        EvaluationQuestion(
            id = 1,
            questionEn = "In what order does a computer execute sequential code?",
            questionAr = "بأي ترتيب ينفذ الحاسوب الأكواد البرمجية المتسلسلة؟",
            optionsEn = listOf("From bottom to top", "From top to bottom in order", "Randomly", "Only the middle instructions"),
            optionsAr = listOf("من الأسفل إلى الأعلى", "من الأعلى إلى الأسفل بالتسلسل", "عشوائياً دون ترتيب", "التعليمات التي في المنتصف فقط"),
            correctIndex = 1,
            conceptTestedEn = "Sequential Execution",
            conceptTestedAr = "التنفيذ المتسلسل"
        ),
        EvaluationQuestion(
            id = 2,
            questionEn = "What is the purpose of a variable in programming?",
            questionAr = "ما هو الغرض من المتغير في البرمجة؟",
            optionsEn = listOf("To play audio", "To store and label a value in memory", "To turn off the display", "To connect to the internet"),
            optionsAr = listOf("لتشغيل مقطع صوتي", "لتخزين قيمة وتسميتها في الذاكرة", "لإطفاء شاشة العرض", "للاتصال بالإنترنت"),
            correctIndex = 1,
            conceptTestedEn = "Variables & Memory",
            conceptTestedAr = "المتغيرات والذاكرة"
        ),
        EvaluationQuestion(
            id = 3,
            questionEn = "What does an IF-ELSE condition allow a program to do?",
            questionAr = "ماذا تتيح جملة الشرط IF-ELSE للبرنامج أن يفعل؟",
            optionsEn = listOf("Run forever without stopping", "Make decisions based on whether a condition is true or false", "Delete files", "Increase computer memory"),
            optionsAr = listOf("التشغيل للأبد دون توقف", "اتخاذ قرارات بناءً على صحة أو عدم صحة الشرط", "حذف الملفات", "زيادة سعة الذاكرة"),
            correctIndex = 1,
            conceptTestedEn = "Conditional Branching",
            conceptTestedAr = "التفرع الشرطي"
        ),
        EvaluationQuestion(
            id = 4,
            questionEn = "What is the primary advantage of using a loop (like REPEAT)?",
            questionAr = "ما الميزة الأساسية لاستخدام حلقة التكرار (مثل REPEAT)؟",
            optionsEn = listOf("Writing the same instruction over and over manually", "Executing repeated tasks automatically without code duplication", "Halting program execution", "Restarting the phone"),
            optionsAr = listOf("إعادة كتابة نفس الأمر يدوياً عدة مرات", "تنفيذ المهام المتكررة آلياً دون تكرار الكود", "إيقاف تنفيذ البرنامج", "إعادة تشغيل الهاتف"),
            correctIndex = 1,
            conceptTestedEn = "Loops & Iteration",
            conceptTestedAr = "الحلقات والتكرار"
        ),
        EvaluationQuestion(
            id = 5,
            questionEn = "What is 'debugging'?",
            questionAr = "ما هو 'التنقيح' أو تصحيح الأخطاء (Debugging)؟",
            optionsEn = listOf("Creating viruses", "The process of identifying and fixing errors in code", "Buying a new computer", "Formatting a hard drive"),
            optionsAr = listOf("برمجة فيروسات خبيثة", "عملية تحديد الأخطاء في الكود وفهمها وإصلاحها", "شراء جهاز جديد", "مسح القرص الصلب"),
            correctIndex = 1,
            conceptTestedEn = "Debugging Process",
            conceptTestedAr = "عملية تصحيح الأخطاء"
        )
    )

    val postTestQuestions: List<EvaluationQuestion> = listOf(
        EvaluationQuestion(
            id = 101,
            questionEn = "Given: SET x = 5, then SET x = x + 3. What does PRINT x output?",
            questionAr = "في البرنامج: SET x = 5، ثم SET x = x + 3. ما نتيجة أمر PRINT x؟",
            optionsEn = listOf("5", "3", "8", "53"),
            optionsAr = listOf("5", "3", "8", "53"),
            correctIndex = 2,
            conceptTestedEn = "Variable Mutation & Output",
            conceptTestedAr = "تعديل المتغيرات والمخرجات"
        ),
        EvaluationQuestion(
            id = 102,
            questionEn = "If score = 40, what does this output?\nIF score >= 50\n  PRINT \"Pass\"\nELSE\n  PRINT \"Review\"\nEND_IF",
            questionAr = "إذا كان score = 40، فماذا يطبع هذا الكود؟\nIF score >= 50\n  PRINT \"Pass\"\nELSE\n  PRINT \"Review\"\nEND_IF",
            optionsEn = listOf("Pass", "Review", "Both Pass and Review", "Nothing"),
            optionsAr = listOf("Pass", "Review", "كلاهما معاً Pass و Review", "لا شيء"),
            correctIndex = 1,
            conceptTestedEn = "Conditional Logic Tracing",
            conceptTestedAr = "تتبع الشروط المنطقية"
        ),
        EvaluationQuestion(
            id = 103,
            questionEn = "How many times does this loop print?\nREPEAT 4\n  PRINT \"Horus\"\nEND_REPEAT",
            questionAr = "كم مرة يطبع هذا الكود كلمة 'Horus'؟\nREPEAT 4\n  PRINT \"Horus\"\nEND_REPEAT",
            optionsEn = listOf("1 time", "4 times", "5 times", "Infinite times"),
            optionsAr = listOf("مرة واحدة", "4 مرات", "5 مرات", "مرات لا نهائية"),
            correctIndex = 1,
            conceptTestedEn = "Bounded Loop Execution",
            conceptTestedAr = "تنفيذ الحلقات المنضبط"
        ),
        EvaluationQuestion(
            id = 104,
            questionEn = "Which symbol in Horus Code represents variable storage and memory stability?",
            questionAr = "أي رمز في شفرة حورس يمثل تخزين المتغيرات واستقرار الذاكرة؟",
            optionsEn = listOf("Ankh ☥", "Eye of Horus 𓂀", "Djed Pillar 𓋹", "Scarab 𓆣"),
            optionsAr = listOf("العنخ ☥", "عين حورس 𓂀", "عمود جد 𓋹", "الجعران 𓆣"),
            correctIndex = 2,
            conceptTestedEn = "Horus Code Symbol Concept Mapping",
            conceptTestedAr = "ربط المفاهيم برموز شفرة حورس"
        ),
        EvaluationQuestion(
            id = 105,
            questionEn = "If a program crashes with 'Undefined variable: score', what is the best fix?",
            questionAr = "إذا توقف البرنامج بخطأ 'Undefined variable: score'، فما هو العلاج الصحيح؟",
            optionsEn = listOf("Delete all code", "Define 'score' using SET before referencing it", "Rename the whole program", "Change computer language"),
            optionsAr = listOf("حذف كل الكود", "تعريف 'score' بأمر SET قبل استدعائه في العمليات", "إعادة تسمية البرنامج بالكامل", "تغيير لغة نظام الحاسوب"),
            correctIndex = 1,
            conceptTestedEn = "Diagnostic Debugging",
            conceptTestedAr = "التشخيص البرمجي الدقيق"
        )
    )
}
