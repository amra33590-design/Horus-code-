package com.example.horuscode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.UserProgressManager
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val progress by progressManager.progressFlow.collectAsState()
    var showResetConfirm by remember { mutableStateOf(false) }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = {
                Text(if (isArabic) "إعادة ضبط التقدم؟" else "Reset All Progress?", color = HorusIvory, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = if (isArabic) "هل أنت متأكد من رغبتك في مسح نقاط الخبرة والإنجازات والمستويات المكتملة والبدء من جديد؟"
                    else "Are you sure you want to erase all XP, achievements, and completed levels to start fresh?",
                    color = HorusMutedIvory
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        progressManager.resetProgress()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HorusErrorCrimson)
                ) {
                    Text(if (isArabic) "نعم، مسح التقدم" else "Yes, Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(if (isArabic) "إلغاء" else "Cancel", color = HorusIvory)
                }
            },
            containerColor = HorusCardNavy
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero XP Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("dashboard_hero_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = HorusMidnight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold.copy(alpha = 0.5f)))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(HorusGold.copy(alpha = 0.2f))
                            .border(2.dp, HorusGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("☥", fontSize = 32.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${progress.totalXp} XP",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = HorusGold
                    )

                    Text(
                        text = if (isArabic) "رصيد الخبرة البرمجية في شفرة حورس" else "Total Horus Code Experience",
                        style = MaterialTheme.typography.bodySmall,
                        color = HorusCyan
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Key Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatPill(
                            value = "${progress.completedLevels.size}/8",
                            label = if (isArabic) "المستويات" else "Levels",
                            color = HorusCyan
                        )
                        StatPill(
                            value = "${progress.completedLessons.size}/8",
                            label = if (isArabic) "الأكاديمية" else "Academy",
                            color = HorusElectricCyan
                        )
                        StatPill(
                            value = "${progress.solvedDebugging.size}/5",
                            label = if (isArabic) "المختبر" else "Debug",
                            color = HorusLightGold
                        )
                        StatPill(
                            value = "${progress.solvedMazes.size}/3",
                            label = if (isArabic) "المعبد" else "Mazes",
                            color = HorusSuccessGreen
                        )
                    }
                }
            }
        }

        // Achievements Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🏆", fontSize = 18.sp)
                    Text(
                        text = if (isArabic) "أوسمة الإنجاز والتميز:" else "Unlocked Achievements:",
                        color = HorusIvory,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Text(
                    text = "${progress.unlockedAchievements.size}/${UserProgressManager.allAchievements.size}",
                    color = HorusGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        items(UserProgressManager.allAchievements) { ach ->
            val isUnlocked = progress.unlockedAchievements.contains(ach.id)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isUnlocked) HorusGold.copy(alpha = 0.6f) else HorusSubtleBorder.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
                    .testTag("achievement_card_${ach.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUnlocked) HorusCardNavy else HorusMidnight.copy(alpha = 0.6f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isUnlocked) HorusGold.copy(alpha = 0.2f) else HorusElevatedNavy.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ach.iconGlyph,
                            fontSize = 22.sp,
                            color = if (isUnlocked) HorusGold else HorusMutedIvory.copy(alpha = 0.4f)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) ach.titleAr else ach.titleEn,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) HorusIvory else HorusMutedIvory.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (isArabic) ach.descAr else ach.descEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isUnlocked) HorusMutedIvory else HorusMutedIvory.copy(alpha = 0.4f),
                            fontSize = 12.sp
                        )
                    }

                    if (isUnlocked) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Unlocked", tint = HorusSuccessGreen, modifier = Modifier.size(22.dp))
                    } else {
                        Icon(Icons.Default.Lock, contentDescription = "Locked", tint = HorusMutedIvory.copy(alpha = 0.3f), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Reset Progress Button at bottom
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showResetConfirm = true },
                modifier = Modifier.fillMaxWidth().testTag("reset_progress_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusErrorCrimson),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusErrorCrimson.copy(alpha = 0.5f)))
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isArabic) "إعادة ضبط جميع الإحصائيات والتقدم" else "Reset All Statistics & Progress")
            }
        }
    }
}

@Composable
private fun StatPill(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
        Text(text = label, fontSize = 11.sp, color = HorusMutedIvory)
    }
}
