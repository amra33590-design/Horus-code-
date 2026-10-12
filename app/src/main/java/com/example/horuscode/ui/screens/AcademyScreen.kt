package com.example.horuscode.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.AcademyLesson
import com.example.horuscode.data.AcademyRepository
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.engine.interpreter.HorusInterpreter
import com.example.horuscode.engine.parser.HorusParser
import com.example.horuscode.ui.components.ConsoleView
import com.example.ui.theme.*

@Composable
fun AcademyScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val progress by progressManager.progressFlow.collectAsState()
    var selectedLessonId by remember { mutableStateOf(1) }
    val lesson = AcademyRepository.lessons.find { it.id == selectedLessonId } ?: AcademyRepository.lessons.first()

    // Quiz state
    var selectedOptionIndex by remember(lesson.id) { mutableStateOf<Int?>(null) }
    var quizSubmitted by remember(lesson.id) { mutableStateOf(false) }

    // Interactive hieroglyphic code execution state
    var userHieroglyphicCode by remember(lesson.id) { mutableStateOf(lesson.exampleCode) }
    var sampleExecutionResult by remember(lesson.id) { mutableStateOf<com.example.horuscode.engine.interpreter.InterpreterResult?>(null) }
    var codeExecutedSuccessfully by remember(lesson.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp)
    ) {
        // Top Lesson Selector Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(AcademyRepository.lessons) { l ->
                val isCompleted = progress.completedLessons.contains(l.id)
                val isSelected = l.id == selectedLessonId

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedLessonId = l.id
                        selectedOptionIndex = null
                        quizSubmitted = false
                        sampleExecutionResult = null
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(l.hieroglyphicSymbol, fontSize = 14.sp)
                            if (isCompleted) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HorusSuccessGreen, modifier = Modifier.size(13.dp))
                            }
                            Text(
                                text = "${if (isArabic) "درس" else "L"} ${l.id}",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HorusCyan,
                        selectedLabelColor = HorusDeepNavy,
                        containerColor = HorusCardNavy,
                        labelColor = HorusIvory
                    ),
                    modifier = Modifier.testTag("lesson_chip_${l.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Lesson Header Card with Symbol Cartouche
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("lesson_card_${lesson.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Big Cartouche with the Hieroglyph
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(HorusGold.copy(alpha = 0.2f))
                                    .border(2.dp, HorusGold, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(lesson.hieroglyphicSymbol, fontSize = 28.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isArabic) lesson.titleAr else lesson.titleEn,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HorusIvory
                                )
                                Text(
                                    text = if (isArabic) lesson.summaryAr else lesson.summaryEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HorusCyan
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Box 1: Ancient Egyptian Meaning (معنى الرمز عند الفراعنة)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = HorusMidnight.copy(alpha = 0.8f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isArabic) "🏛️ أصل الرمز في الحضارة المصرية القديمة:" else "🏛️ Authentic Ancient Egyptian Origin:",
                                    color = HorusGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = if (isArabic) lesson.symbolAncientMeaningAr else lesson.symbolAncientMeaningEn,
                                    color = HorusIvory,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Box 2: Logical Connection to Programming (الرابط البرمجي)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = HorusElevatedNavy.copy(alpha = 0.7f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isArabic) "💻 لماذا يؤدي هذا الدور في لغة حورس البرمجية؟" else "💻 Why this role in Horus Code?",
                                    color = HorusCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = if (isArabic) lesson.programmingConnectionAr else lesson.programmingConnectionEn,
                                    color = HorusMutedIvory,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Hieroglyphic Coding Laboratory
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lesson_hieroglyph_studio_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusMidnight),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(HorusGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(lesson.hieroglyphicSymbol, fontSize = 18.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(
                                        text = if (isArabic) "محرر شفرة حورس بالرموز الهيروغليفية" else "Hieroglyphic Code Laboratory",
                                        color = HorusGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (isArabic) "برمج بالرمز الهيروغليفي (${lesson.hieroglyphicSymbol}) وتحقق من وظيفته" else "Program with hieroglyph (${lesson.hieroglyphicSymbol}) and test output",
                                        color = HorusCyan,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Reset button
                            IconButton(
                                onClick = {
                                    userHieroglyphicCode = lesson.exampleCode
                                    sampleExecutionResult = null
                                    codeExecutedSuccessfully = false
                                },
                                modifier = Modifier.size(30.dp).testTag("reset_lesson_code_btn")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = HorusMutedIvory, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hieroglyphic Insertion Palette Bar
                        Text(
                            text = if (isArabic) "لوحة الرموز (انقر لإدراج الرمز والأمر):" else "Hieroglyph Palette (Tap to inscribe):",
                            color = HorusMutedIvory,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val paletteItems = listOf(
                                Triple("☥ بدء", "\n☥ بدء\n", "☥"),
                                Triple("☥ ختام", "\n☥ ختام\n", "☥"),
                                Triple("𓋹 تثبيت", "\n𓋹 س = 10\n", "𓋹"),
                                Triple("𓏛 تدوين", "\n𓏛 \"حورس\"\n", "𓏛"),
                                Triple("𓂀 فحص", "\n𓂀 س > 5\n", "𓂀"),
                                Triple("𓆣 تكرار", "\n𓆣 3\n", "𓆣"),
                                Triple("𓋖 وزن", "\n𓋖 \"أدخل قيمة\" في مدخل\n", "𓋖"),
                                Triple("𓊗 انطلاق", "\n𓊗 تقدم\n", "𓊗")
                            )

                            items(paletteItems) { (label, snippet, glyph) ->
                                val isTargetGlyph = glyph == lesson.hieroglyphicSymbol
                                AssistChip(
                                    onClick = {
                                        userHieroglyphicCode = userHieroglyphicCode.trimEnd() + snippet
                                    },
                                    label = {
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isTargetGlyph) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (isTargetGlyph) HorusGold.copy(alpha = 0.25f) else HorusCardNavy,
                                        labelColor = if (isTargetGlyph) HorusLightGold else HorusIvory
                                    ),
                                    border = AssistChipDefaults.assistChipBorder(enabled = true).copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(
                                            if (isTargetGlyph) HorusGold else HorusSubtleBorder.copy(alpha = 0.5f)
                                        )
                                    ),
                                    modifier = Modifier.testTag("lesson_palette_$label")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hieroglyphic Code Input Editor
                        OutlinedTextField(
                            value = userHieroglyphicCode,
                            onValueChange = { userHieroglyphicCode = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("lesson_code_input"),
                            minLines = 4,
                            maxLines = 8,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                color = HorusElectricCyan,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = HorusDeepNavy,
                                unfocusedContainerColor = HorusDeepNavy,
                                focusedBorderColor = HorusCyan,
                                unfocusedBorderColor = HorusSubtleBorder.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Execution Action Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (userHieroglyphicCode.contains(lesson.hieroglyphicSymbol))
                                    (if (isArabic) "✓ رمز الدرس متضمن في الكود" else "✓ Target glyph included")
                                else (if (isArabic) "💡 أضف رمز الدرس ${lesson.hieroglyphicSymbol}" else "💡 Add lesson glyph ${lesson.hieroglyphicSymbol}"),
                                color = if (userHieroglyphicCode.contains(lesson.hieroglyphicSymbol)) HorusSuccessGreen else HorusLightGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Button(
                                onClick = {
                                    val (blocks, errors) = HorusParser.parseTextToBlocks(userHieroglyphicCode)
                                    if (errors.isNotEmpty()) {
                                        sampleExecutionResult = com.example.horuscode.engine.interpreter.InterpreterResult(
                                            isSuccess = false,
                                            outputs = emptyList(),
                                            trace = emptyList(),
                                            variables = emptyMap(),
                                            actions = emptyList(),
                                            stepsCount = 0,
                                            errorMessageEn = errors.first().messageEn,
                                            errorMessageAr = errors.first().messageAr
                                        )
                                        codeExecutedSuccessfully = false
                                    } else {
                                        val res = HorusInterpreter().execute(blocks)
                                        sampleExecutionResult = res
                                        if (res.isSuccess) {
                                            codeExecutedSuccessfully = true
                                            if (userHieroglyphicCode.contains(lesson.hieroglyphicSymbol)) {
                                                progressManager.markProgramExecuted()
                                            }
                                        } else {
                                            codeExecutedSuccessfully = false
                                        }
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("run_lesson_sample_btn")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isArabic) "تشغيل لغة حورس بالهيروغليفية" else "Run Horus Hieroglyphic Code",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Success Celebration Card when student programs successfully with the glyph
                        if (codeExecutedSuccessfully && userHieroglyphicCode.contains(lesson.hieroglyphicSymbol)) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("glyph_success_celebration"),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = HorusElevatedNavy),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("🎉", fontSize = 20.sp)
                                    Column {
                                        Text(
                                            text = if (isArabic) "أحسنت! برمجت بنجاح برمز ${lesson.hieroglyphicSymbol} في لغة حورس!"
                                            else "Brilliant! You successfully programmed with ${lesson.hieroglyphicSymbol}!",
                                            color = HorusGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isArabic) "الرمز أدى دوره البرمجي تماماً وفقاً لمعناه في الحضارة المصرية القديمة."
                                            else "The symbol executed its computational task perfectly aligned with its ancient Egyptian heritage.",
                                            color = HorusIvory,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Output Console View
                        if (sampleExecutionResult != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ConsoleView(
                                outputs = sampleExecutionResult!!.outputs,
                                isSuccess = sampleExecutionResult!!.isSuccess,
                                isArabic = isArabic,
                                onClear = {
                                    sampleExecutionResult = null
                                    codeExecutedSuccessfully = false
                                }
                            )
                        }
                    }
                }
            }

            // Interactive Knowledge Check (Quiz)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("lesson_quiz_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = HorusGold, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (isArabic) "اختبار فهم الرمز الهيروغليفي ووظيفته:" else "Knowledge Check: Hieroglyph Function:",
                                color = HorusGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isArabic) lesson.quiz.questionAr else lesson.quiz.questionEn,
                            color = HorusIvory,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val options = if (isArabic) lesson.quiz.optionsAr else lesson.quiz.optionsEn
                        options.forEachIndexed { idx, opt ->
                            val isSelected = selectedOptionIndex == idx
                            val isCorrect = idx == lesson.quiz.correctIndex
                            val optionBorder = when {
                                quizSubmitted && isCorrect -> HorusSuccessGreen
                                quizSubmitted && isSelected && !isCorrect -> HorusErrorCrimson
                                isSelected -> HorusCyan
                                else -> HorusSubtleBorder.copy(alpha = 0.5f)
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable(enabled = !quizSubmitted) {
                                        selectedOptionIndex = idx
                                    }
                                    .border(1.dp, optionBorder, RoundedCornerShape(8.dp))
                                    .testTag("quiz_option_$idx"),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) HorusElevatedNavy else HorusMidnight
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { if (!quizSubmitted) selectedOptionIndex = idx },
                                        colors = RadioButtonDefaults.colors(selectedColor = HorusCyan)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = opt, color = HorusIvory, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!quizSubmitted) {
                            Button(
                                onClick = {
                                    if (selectedOptionIndex != null) {
                                        quizSubmitted = true
                                        if (selectedOptionIndex == lesson.quiz.correctIndex) {
                                            progressManager.markLessonCompleted(lesson.id, lesson.xpReward)
                                        }
                                    }
                                },
                                enabled = selectedOptionIndex != null,
                                modifier = Modifier.fillMaxWidth().testTag("submit_quiz_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy)
                            ) {
                                Text(if (isArabic) "تأكيد الإجابة" else "Submit Answer", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            val isCorrect = selectedOptionIndex == lesson.quiz.correctIndex
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCorrect) HorusSuccessGreen.copy(alpha = 0.2f) else HorusErrorCrimson.copy(alpha = 0.2f))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = if (isCorrect) (if (isArabic) "إجابة صحيحة! أحسنت (+100 XP)" else "Correct! Well done (+100 XP)")
                                    else (if (isArabic) "إجابة غير صحيحة، راجع معنى الرمز وأعد المحاولة" else "Incorrect answer. Review the symbol meaning and retry."),
                                    color = if (isCorrect) HorusSuccessGreen else HorusErrorCrimson,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isArabic) lesson.quiz.explanationAr else lesson.quiz.explanationEn,
                                    color = HorusIvory,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
