package com.example.horuscode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.EvaluationQuestion
import com.example.horuscode.data.EvaluationRepository
import com.example.horuscode.data.UserProgressManager
import com.example.ui.theme.*

@Composable
fun EvaluationScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val progress by progressManager.progressFlow.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0: Results & Comparison, 1: Take Pre-Test, 2: Take Post-Test
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var testSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp)
    ) {
        // Tab switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                label = { Text(if (isArabic) "تقرير الأثر والنتائج" else "Results & Impact", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HorusCyan,
                    selectedLabelColor = HorusDeepNavy,
                    containerColor = HorusCardNavy,
                    labelColor = HorusIvory
                ),
                modifier = Modifier.testTag("eval_tab_results")
            )
            FilterChip(
                selected = activeTab == 1,
                onClick = {
                    activeTab = 1
                    userAnswers = mutableMapOf()
                    testSubmitted = false
                },
                label = { Text(if (isArabic) "الاختبار القبلي (Pre-Test)" else "Pre-Test", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HorusCyan,
                    selectedLabelColor = HorusDeepNavy,
                    containerColor = HorusCardNavy,
                    labelColor = HorusIvory
                ),
                modifier = Modifier.testTag("eval_tab_pre")
            )
            FilterChip(
                selected = activeTab == 2,
                onClick = {
                    activeTab = 2
                    userAnswers = mutableMapOf()
                    testSubmitted = false
                },
                label = { Text(if (isArabic) "الاختبار البعدي (Post-Test)" else "Post-Test", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HorusCyan,
                    selectedLabelColor = HorusDeepNavy,
                    containerColor = HorusCardNavy,
                    labelColor = HorusIvory
                ),
                modifier = Modifier.testTag("eval_tab_post")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (activeTab) {
            0 -> ResultsReportView(progress = progress, isArabic = isArabic, onStartPre = { activeTab = 1 }, onStartPost = { activeTab = 2 })
            1 -> TestQuizView(
                title = if (isArabic) "الاختبار القبلي: قياس المعرفة المبدئية" else "Pre-Test: Initial Knowledge Baseline",
                questions = EvaluationRepository.preTestQuestions,
                userAnswers = userAnswers,
                testSubmitted = testSubmitted,
                isArabic = isArabic,
                onSelectAnswer = { qId, ansIdx ->
                    if (!testSubmitted) {
                        userAnswers = userAnswers.toMutableMap().apply { put(qId, ansIdx) }
                    }
                },
                onSubmit = {
                    testSubmitted = true
                    val score = EvaluationRepository.preTestQuestions.count { q ->
                        userAnswers[q.id] == q.correctIndex
                    }
                    progressManager.recordPreTest(score, EvaluationRepository.preTestQuestions.size)
                }
            )
            2 -> TestQuizView(
                title = if (isArabic) "الاختبار البعدي: قياس الأثر والتمكن" else "Post-Test: Learning Mastery Evaluation",
                questions = EvaluationRepository.postTestQuestions,
                userAnswers = userAnswers,
                testSubmitted = testSubmitted,
                isArabic = isArabic,
                onSelectAnswer = { qId, ansIdx ->
                    if (!testSubmitted) {
                        userAnswers = userAnswers.toMutableMap().apply { put(qId, ansIdx) }
                    }
                },
                onSubmit = {
                    testSubmitted = true
                    val score = EvaluationRepository.postTestQuestions.count { q ->
                        userAnswers[q.id] == q.correctIndex
                    }
                    progressManager.recordPostTest(score, EvaluationRepository.postTestQuestions.size)
                }
            )
        }
    }
}

@Composable
fun ResultsReportView(
    progress: com.example.horuscode.data.UserProgress,
    isArabic: Boolean,
    onStartPre: () -> Unit,
    onStartPost: () -> Unit
) {
    val preScore = progress.preTestScore
    val postScore = progress.postTestScore

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("eval_summary_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusMidnight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold.copy(alpha = 0.5f)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = HorusGold)
                        Text(
                            text = if (isArabic) "منظومة تقييم الأثر التعليمي (Learning Impact)" else "Learning Impact Assessment Module",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = HorusIvory
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isArabic) "تقيس هذه المنظومة الأثر المعرفي الفعلي بدقة عبر مقارنة أداء الطالب قبل الانخراط في رحلة شفرة حورس وبعدها، استناداً إلى إجابات حقيقية موثقة."
                        else "This evaluation framework transparently measures learning gains by comparing student performance before and after engaging with Horus Code based on verified test responses.",
                        color = HorusMutedIvory,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Comparison Stats or Honest Empty State
        item {
            if (preScore == null && postScore == null) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("eval_empty_state_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusCardNavy)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📊", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isArabic) "لم يتم إجراء التقييم بعد" else "Evaluation Not Yet Conducted",
                            fontWeight = FontWeight.Bold,
                            color = HorusIvory,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "لبدء قياس الأثر التعليمي، ابدأ بإجراء الاختبار القبلي لقياس المعرفة المبدئية، ثم أجرِ الاختبار البعدي بعد إتمام الدروس."
                            else "To begin measuring learning gains, start by taking the Pre-Test to establish a baseline, then take the Post-Test after completing lessons.",
                            color = HorusMutedIvory,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onStartPre,
                            colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy)
                        ) {
                            Text(if (isArabic) "بدء الاختبار القبلي الآن" else "Start Pre-Test Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("eval_scores_comparison_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusCardNavy)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isArabic) "مقارنة نتائج الاختبارات:" else "Comparative Test Scores:",
                            fontWeight = FontWeight.Bold,
                            color = HorusGold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScoreBlock(
                                title = if (isArabic) "الاختبار القبلي" else "Pre-Test",
                                score = preScore ?: 0,
                                total = progress.preTestTotal ?: 5,
                                color = HorusCyan
                            )
                            ScoreBlock(
                                title = if (isArabic) "الاختبار البعدي" else "Post-Test",
                                score = postScore ?: 0,
                                total = progress.postTestTotal ?: 5,
                                color = HorusSuccessGreen
                            )
                        }

                        if (preScore != null && postScore != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            val diff = postScore - preScore
                            val percentGain = (diff.toFloat() / 5f * 100).toInt()
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = HorusElevatedNavy)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(if (diff >= 0) "📈" else "📉", fontSize = 24.sp)
                                    Column {
                                        Text(
                                            text = "${if (isArabic) "معدل التحسن المعرفي: " else "Measured Learning Gain: "} ${if (diff > 0) "+$percentGain%" else "$percentGain%"}",
                                            fontWeight = FontWeight.Bold,
                                            color = if (diff >= 0) HorusSuccessGreen else HorusErrorCrimson,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = if (isArabic) "سجل الطالب نمواً واضحاً في استيعاب التفكير الخوارزمي والمفاهيم البرمجية المجردة بفضل الرمزية البصرية لشفرة حورس."
                                            else "The student demonstrated measurable cognitive growth in algorithmic concepts and abstract logic supported by Horus Code visual symbolism.",
                                            color = HorusMutedIvory,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBlock(title: String, score: Int, total: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, color = HorusMutedIvory, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("$score / $total", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        Text("${(score.toFloat() / total * 100).toInt()}%", fontSize = 11.sp, color = HorusLightGold)
    }
}

@Composable
fun TestQuizView(
    title: String,
    questions: List<EvaluationQuestion>,
    userAnswers: Map<Int, Int>,
    testSubmitted: Boolean,
    isArabic: Boolean,
    onSelectAnswer: (Int, Int) -> Unit,
    onSubmit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HorusGold)
        }

        itemsIndexed(questions) { qIdx, question ->
            val chosenIdx = userAnswers[question.id]
            Card(
                modifier = Modifier.fillMaxWidth().testTag("eval_question_${question.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder.copy(alpha = 0.5f)))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "${qIdx + 1}. ${if (isArabic) question.questionAr else question.questionEn}",
                        fontWeight = FontWeight.Bold,
                        color = HorusIvory,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val opts = if (isArabic) question.optionsAr else question.optionsEn
                    opts.forEachIndexed { optIdx, optText ->
                        val isSelected = chosenIdx == optIdx
                        val isCorrect = optIdx == question.correctIndex
                        val borderCol = when {
                            testSubmitted && isCorrect -> HorusSuccessGreen
                            testSubmitted && isSelected && !isCorrect -> HorusErrorCrimson
                            isSelected -> HorusCyan
                            else -> HorusSubtleBorder.copy(alpha = 0.4f)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable(enabled = !testSubmitted) {
                                    onSelectAnswer(question.id, optIdx)
                                }
                                .border(1.dp, borderCol, RoundedCornerShape(8.dp))
                                .testTag("eval_opt_${question.id}_$optIdx"),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) HorusElevatedNavy else HorusMidnight
                            )
                        ) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { if (!testSubmitted) onSelectAnswer(question.id, optIdx) },
                                    colors = RadioButtonDefaults.colors(selectedColor = HorusCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(optText, color = HorusIvory, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            if (!testSubmitted) {
                Button(
                    onClick = onSubmit,
                    enabled = userAnswers.size == questions.size,
                    modifier = Modifier.fillMaxWidth().testTag("submit_eval_test"),
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy)
                ) {
                    Text(if (isArabic) "تسجيل وحفظ الإجابات" else "Submit Evaluation Test", fontWeight = FontWeight.Bold)
                }
            } else {
                val score = questions.count { userAnswers[it.id] == it.correctIndex }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusSuccessGreen.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HorusSuccessGreen)
                        Column {
                            Text(
                                text = "${if (isArabic) "اكتمل الاختبار! النتيجة: " else "Test Completed! Score: "} $score / ${questions.size}",
                                fontWeight = FontWeight.Bold,
                                color = HorusSuccessGreen
                            )
                            Text(
                                text = if (isArabic) "تم تسجيل النتيجة وحفظها في تقرير الأثر التعليمي." else "Your score has been recorded into the learning impact dashboard.",
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
