package com.example.horuscode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.engine.interpreter.HorusInterpreter
import com.example.horuscode.engine.interpreter.InterpreterResult
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock
import com.example.horuscode.ui.components.ConsoleView
import com.example.ui.theme.*

data class DemoStep(
    val stepIndex: Int,
    val titleEn: String,
    val titleAr: String,
    val presenterNotesEn: String,
    val presenterNotesAr: String,
    val keyConceptEn: String,
    val keyConceptAr: String,
    val sampleBlocks: List<HorusBlock>? = null
)

object JudgeDemoRepository {
    val steps = listOf(
        DemoStep(
            1,
            "1. The Educational Challenge",
            "١. التحدي التعليمي",
            "Begin by framing the core problem: Traditional text-based programming syntax imposes an intimidating cognitive barrier on novice students. Abstract concepts like variables, branches, and memory pointers feel distant and unforgiving.",
            "ابدأ بتسليط الضوء على المشكلة الجوهرية: تمثل لغات البرمجة النصية التقليدية حاجزاً ذهنياً مرهقاً للطلاب المبتدئين؛ حيث تبدو مفاهيم كالذاكرة والتفرع والتحكم مجردة وغير ملموسة.",
            "Cognitive load in early programming education",
            "العبء الذهني في المراحل الأولى لتعلم البرمجة"
        ),
        DemoStep(
            2,
            "2. The Horus Code Solution",
            "٢. حل شفرة حورس الابتكاري",
            "Introduce our vision: 'From the symbols of the past to the code of the future.' By mapping core computer science primitives to iconic Egyptian hieroglyphs, abstract computer concepts gain visual identity, stability, and cultural connection.",
            "اطرح رؤيتنا المركزية: «من رموز الماضي… إلى شيفرة المستقبل». بربط أساسيات علوم الحاسوب برموز الهيروغليفية المصرية، تكتسب المفاهيم الحاسوبية هوية بصرية واستقراراً وارتباطاً حضارياً ملهماً.",
            "Symbolic conceptual scaffolding",
            "السقالات المعرفية القائمة على الرمزية البصرية"
        ),
        DemoStep(
            3,
            "3. The Visual Studio Architecture",
            "٣. بنية استوديو البرمجة المرئية",
            "Explain that Horus Code is not a static prototype or mockup; it is an authentic deterministic programming language with an AST parser, sandboxed runtime engine, and real-time execution trace.",
            "أوضح لأعضاء لجنة التحكيم أن شفرة حورس ليست مجرد نموذج شكلي ثابت، بل لغة برمجية حقيقية متكاملة تتضمن محللاً نحوياً ومفسراً تنفيذياً منضبطاً وتتبعاً لحظياً لحالة الذاكرة.",
            "Full architectural execution pipeline",
            "محرك تنفيذي حقيقي ومفسر مستقل"
        ),
        DemoStep(
            4,
            "4. Sequential Execution & Hello World",
            "٤. التتابع المنطقي والبرنامج الأول",
            "Demonstrate our first live execution: The sacred Ankh ☥ bounds the program lifecycle, while the Papyrus Scroll 𓏛 prints messages and computes arithmetic.",
            "استعرض أول تنفيذ حي مباشر: يضبط رمز العنخ ☥ دورة حياة البرنامج، بينما تقوم لفافة البردي 𓏛 بطباعة المخرجات وتقييم المعادلات الرياضية.",
            "Sequence, Start/Stop boundaries, and Standard Output",
            "التسلسل وحدود البداية والنهاية وشاشة المخرجات",
            sampleBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Welcome to Horus Code!\"")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "6 * 7")),
                HorusBlock(type = BlockType.STOP)
            )
        ),
        DemoStep(
            5,
            "5. Variable Memory & Djed Stability",
            "٥. المتغيرات واستقرار الذاكرة (عمود جد)",
            "Explain how the Djed pillar 𓋹 anchors variables in memory. Run a live assignment demonstrating variable initialization and arithmetic updates.",
            "اشرح كيف يمنح عمود جد 𓋹 الاستقرار للمتغيرات في الذاكرة. نفذ عملية مباشرة توضح تخصيص المتغير وتعديل قيمته الحسابية في الذاكرة.",
            "Variable declaration, memory mutation, and expression evaluation",
            "تعريف المتغيرات وتعديل قيمها بالذاكرة وتقييم التعبيرات",
            sampleBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "gold", "val" to "50")),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "gold", "val" to "gold + 25")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Total Gold: \" + gold")),
                HorusBlock(type = BlockType.STOP)
            )
        ),
        DemoStep(
            6,
            "6. Conditional Decision Making (Eye of Horus)",
            "٦. اتخاذ القرارات الشرطية (عين حورس)",
            "Demonstrate the Eye of Horus 𓂀 evaluating conditions (IF / ELSE). Show how truth conditions determine which branch the computer chooses.",
            "استعرض دور عين حورس 𓂀 في تقييم الشروط (IF / ELSE) وكيف يوجه فحص الصواب والخطأ مسار البرنامج لاختيار الفرع المناسب.",
            "Boolean logic and conditional branch execution",
            "المنطق البولياني والتفرع الشرطي المزدوج",
            sampleBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "score", "val" to "85")),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "score", "op" to ">=", "right" to "60")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Trial Passed with Distinction!\"")),
                HorusBlock(type = BlockType.ELSE),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Continue Practicing\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.STOP)
            )
        ),
        DemoStep(
            7,
            "7. Loop Automation (Scarab Cycles)",
            "٧. أتمتة التكرار (دورات الجعران)",
            "Showcase the Scarab 𓆣 automating repetition without manual code duplication. Execute a bounded loop with accumulation.",
            "أبرز دور الجعران 𓆣 في أتمتة التكرار دون تكرار الأوامر يدوياً، مع حماية أمان صارمة تمنع الحلقات اللانهائية.",
            "Bounded iteration cycles and accumulator pattern",
            "الحلقات المنضبطة ونمط التراكم الرياضي",
            sampleBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "3")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"Scarab Cycle Automated\"")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            )
        ),
        DemoStep(
            8,
            "8. Diagnostic Debugging Laboratory",
            "٨. مختبر تصحيح الأخطاء التشخيصي",
            "Highlight the pedagogical value of deliberate debugging: Students encounter intentional syntax and runtime bugs, diagnose them through real error messages, and repair them.",
            "أكد على القيمة التربوية للتنقيح المنظم: يتعامل الطلاب مع برامج تحوي أخطاء مقصودة، ويفهمون رسائل الخطأ الحقيقية، ثم يصلحون الثغرة بأنفسهم.",
            "Error classification and systematic debugging skills",
            "تصنيف الأخطاء واكتساب مهارات التشخيص المنهجي"
        ),
        DemoStep(
            9,
            "9. Temple of Logic Maze Simulation",
            "٩. محاكاة معبد المنطق التفاعلية",
            "Demonstrate how algorithmic code drives physical actions: Guide the Horus explorer on the interactive grid, navigating obstacles and collecting relics.",
            "استعرض كيف يحول الكود التفكير المنطقي إلى حركة ملموسة: قيادة مستكشف حورس على الشبكة وتجاوز العقبات وجمع المفاتيح للوصول إلى المحراب.",
            "Embodied computational thinking and spatial algorithms",
            "التفكير الحاسوبي المجسد وخوارزميات الملاحة المكانية"
        ),
        DemoStep(
            10,
            "10. Measurable Educational Impact",
            "١٠. قياس الأثر التعليمي والتقييم المستمر",
            "Conclude with our rigorous evaluation framework: Built-in pre-tests and post-tests measure real learning gains, mastery retention, and computational problem-solving growth.",
            "اختتم العرض بمنظومة التقييم الدقيقة: اختبارات قبلية وبعدية تسجل أداء الطالب الفعلي لقياس نسب التحسن واكتساب المفاهيم البرمجية بدقة ومصداقية.",
            "Evidence-based learning analytics and student growth",
            "التقييم المبني على الأدلة وتتبع نمو مهارات الطلاب"
        )
    )
}

@Composable
fun JudgeDemoScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    onReplayIntro: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentStepIdx by remember { mutableStateOf(0) }
    val step = JudgeDemoRepository.steps[currentStepIdx]

    // Live runner inside demo step if sampleBlocks present
    var executionResult by remember(currentStepIdx) {
        mutableStateOf<InterpreterResult?>(step.sampleBlocks?.let { HorusInterpreter().execute(it) })
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Presentation Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("judge_demo_header"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusMidnight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(HorusGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("𓂀", fontSize = 24.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "وضع عرض لجنة التحكيم (Judge Demo Mode)" else "Competition Judge Presentation Mode",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = HorusIvory
                            )
                            Text(
                                text = "${if (isArabic) "المرحلة" else "Step"} ${currentStepIdx + 1} / ${JudgeDemoRepository.steps.size}",
                                color = HorusLightGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Direct Launch Intro Button for Judges
                        Button(
                            onClick = onReplayIntro,
                            colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("judge_launch_intro_btn")
                        ) {
                            Text("🎬", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isArabic) "العرض السينمائي" else "Play Intro", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Stepper Navigation Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentStepIdx > 0) currentStepIdx-- },
                    enabled = currentStepIdx > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCardNavy, contentColor = HorusIvory),
                    modifier = Modifier.testTag("judge_prev_step")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isArabic) "السابق" else "Previous")
                }

                LinearProgressIndicator(
                    progress = { (currentStepIdx + 1).toFloat() / JudgeDemoRepository.steps.size },
                    modifier = Modifier.width(120.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = HorusGold,
                    trackColor = HorusElevatedNavy
                )

                Button(
                    onClick = { if (currentStepIdx < JudgeDemoRepository.steps.size - 1) currentStepIdx++ },
                    enabled = currentStepIdx < JudgeDemoRepository.steps.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy),
                    modifier = Modifier.testTag("judge_next_step")
                ) {
                    Text(if (isArabic) "التالي" else "Next")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        // Main Step Content Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("judge_step_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isArabic) step.titleAr else step.titleEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HorusGold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "🎯 ${if (isArabic) step.keyConceptAr else step.keyConceptEn}",
                        color = HorusCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Presenter Notes Box
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = HorusMidnight.copy(alpha = 0.9f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isArabic) "🎙️ ملاحظات المتحدث للجنة التحكيم:" else "🎙️ Presenter Script & Evaluation Notes:",
                                color = HorusLightGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic) step.presenterNotesAr else step.presenterNotesEn,
                                color = HorusIvory,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // Live Interactive Runner if this step has sample code
                    if (step.sampleBlocks != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isArabic) "⚡ تجربة تنفيذ فورية أمام اللجنة:" else "⚡ Live Execution Demonstration:",
                            color = HorusElectricCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        ConsoleView(
                            outputs = executionResult?.outputs ?: emptyList(),
                            isSuccess = executionResult?.isSuccess ?: true,
                            isArabic = isArabic,
                            onClear = { executionResult = null }
                        )
                    }
                }
            }
        }
    }
}
