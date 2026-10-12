package com.example.horuscode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.engine.model.TraceStep
import com.example.ui.theme.*

@Composable
fun TraceViewer(
    trace: List<TraceStep>,
    isArabic: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, HorusSubtleBorder, RoundedCornerShape(14.dp))
            .testTag("trace_viewer"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = HorusMidnight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "𓂀", fontSize = 18.sp, color = HorusElectricCyan)
                Text(
                    text = if (isArabic) "مسار التتبع الذكي (Execution Trace)" else "Execution Trace Log",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = HorusIvory
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (trace.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(HorusDeepNavy.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "قم بتشغيل البرنامج أو تشغيله خطوة بخطوة لرؤية التحليل المنطقي..." else "Run or step through program to see trace explanation...",
                        style = MaterialTheme.typography.bodySmall,
                        color = HorusMutedIvory.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 180.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(trace) { step ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = HorusCardNavy.copy(alpha = 0.9f)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder.copy(alpha = 0.5f)))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${step.stepNumber}: ${step.instruction}",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = HorusLightGold
                                    )
                                    if (step.variablesSnapshot.isNotEmpty()) {
                                        Text(
                                            text = step.variablesSnapshot.entries.joinToString(", ") { "${it.key}=${it.value}" },
                                            fontSize = 11.sp,
                                            color = HorusCyan,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isArabic) step.explanationAr else step.explanationEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HorusIvory.copy(alpha = 0.9f),
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
