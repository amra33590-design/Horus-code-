package com.example.horuscode.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.ui.components.HorusStrings
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    isDarkTheme: Boolean,
    onReplayIntro: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progress by progressManager.progressFlow.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(if (isArabic) "إعادة ضبط المنصة؟" else "Reset Application?", color = HorusIvory, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = if (isArabic) "سيتم مسح جميع الإنجازات والدرجات والمستويات المكتملة وإعادة تهيئة بيئة العمل."
                    else "All earned XP, completed levels, and test scores will be reset to default.",
                    color = HorusMutedIvory
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        progressManager.resetProgress()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HorusErrorCrimson)
                ) {
                    Text(if (isArabic) "تأكيد المسح" else "Confirm Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
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
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Identity Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("settings_identity_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = HorusMidnight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold.copy(alpha = 0.6f)))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(HorusCardNavy)
                            .border(2.dp, HorusGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("𓂀", fontSize = 36.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "HORUS CODE | شِفرة حورس",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HorusIvory
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (isArabic) "«من رموز الماضي… إلى شيفرة المستقبل»" else "\"From the symbols of the past to the code of the future.\"",
                        color = HorusCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Language & Theme Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isArabic) "تخصيص الواجهة واللغة" else "Interface & Preferences",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HorusGold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Language Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = HorusCyan)
                            Column {
                                Text(if (isArabic) "اللغة (Language)" else "Language (اللغة)", color = HorusIvory, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text(if (isArabic) "العربية (RTL) / English (LTR)" else "English (LTR) / العربية (RTL)", color = HorusMutedIvory, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = isArabic,
                            onCheckedChange = { progressManager.setLanguage(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = HorusDeepNavy,
                                checkedTrackColor = HorusCyan,
                                uncheckedThumbColor = HorusMutedIvory,
                                uncheckedTrackColor = HorusMidnight
                            ),
                            modifier = Modifier.testTag("toggle_language_switch")
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = HorusSubtleBorder.copy(alpha = 0.4f))

                    // Theme Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = HorusLightGold)
                            Column {
                                Text(if (isArabic) "المظهر الداكن (Midnight Mode)" else "Dark Theme", color = HorusIvory, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text(if (isArabic) "سمة سماء مصر الليلية الفاخرة" else "Egyptian midnight tech palette", color = HorusMutedIvory, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { progressManager.setTheme(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = HorusDeepNavy,
                                checkedTrackColor = HorusGold
                            ),
                            modifier = Modifier.testTag("toggle_theme_switch")
                        )
                    }
                }
            }
        }

        // Creator & Credits Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("creator_credits_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isArabic) "الملكية الفكرية وفريق العمل:" else "Credits & Authorship:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HorusGold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "HORUS CODE | شِفرة حورس",
                        fontWeight = FontWeight.Bold,
                        color = HorusIvory,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Created by Amr Ahmed | عمرو أحمد",
                        fontWeight = FontWeight.Medium,
                        color = HorusCyan,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "© 2026 Amr Ahmed. All Rights Reserved.",
                        color = HorusMutedIvory,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isArabic) "تم تصميم وتطوير هذا التطبيق كمنصة تعليمية ابتكارية تجمع بين عبقرية الهوية المصرية القديمة والتفكير الخوارزمي الحديث للتأهيل للمنافسات والمسابقات الطلابية الكبرى."
                        else "Designed and engineered as an innovative educational platform merging the artistry of ancient Egyptian identity with modern computational thinking for student technology competitions.",
                        color = HorusMutedIvory,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Cinematic Intro Experience Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_cinematic_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusMidnight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold.copy(alpha = 0.6f)))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(HorusGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎬", fontSize = 18.sp)
                            }
                            Column {
                                Text(
                                    text = if (isArabic) "العرض السينمائي الافتتاحي" else "Cinematic Intro Experience",
                                    color = HorusIvory,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = if (isArabic) "مشاهدة المقدمة السينمائية (صحوة حورس)" else "Re-experience the award-level opening cinematic",
                                    color = HorusCyan,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = onReplayIntro,
                            colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("settings_replay_intro_button")
                        ) {
                            Text(if (isArabic) "تشغيل العرض" else "Play Intro", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = HorusSubtleBorder.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Toggle: Show intro upon launching app
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "تشغيل المقدمة تلقائياً عند فتح التطبيق" else "Play Intro on App Launch",
                                color = HorusIvory,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isArabic) "عرض البداية السينمائية دائماً في كل مرة يُفتح فيها التطبيق" else "Always show the cinematic awakening when opening the app",
                                color = HorusMutedIvory,
                                fontSize = 10.sp
                            )
                        }

                        Switch(
                            checked = progress.showIntroOnLaunch,
                            onCheckedChange = { progressManager.setShowIntroOnLaunch(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = HorusDeepNavy,
                                checkedTrackColor = HorusGold
                            ),
                            modifier = Modifier.testTag("toggle_launch_intro_switch")
                        )
                    }
                }
            }
        }

        // Data Management Reset
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = HorusMidnight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (isArabic) "إعادة ضبط البيانات" else "Reset Application Data", color = HorusIvory, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(if (isArabic) "مسح الذاكرة المحلية والبدء من جديد" else "Clear local progress & cache", color = HorusMutedIvory, fontSize = 11.sp)
                    }

                    Button(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = HorusErrorCrimson),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("settings_reset_button")
                    ) {
                        Text(if (isArabic) "مسح" else "Reset", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
