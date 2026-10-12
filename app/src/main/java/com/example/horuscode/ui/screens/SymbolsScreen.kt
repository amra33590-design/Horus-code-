package com.example.horuscode.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.SymbolDetail
import com.example.horuscode.data.SymbolRepository
import com.example.ui.theme.*

@Composable
fun SymbolsScreen(
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("𓏛", fontSize = 24.sp, color = HorusGold)
            Column {
                Text(
                    text = if (isArabic) "مستكشف الرموز الهيروغليفية" else "Hieroglyphic Symbol Explorer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HorusIvory
                )
                Text(
                    text = if (isArabic) "الربط بين الأصالة التاريخية والتوظيف البرمجي الحديث" else "Historical authenticity & modern programming roles",
                    style = MaterialTheme.typography.labelSmall,
                    color = HorusCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SymbolRepository.symbols) { symbol ->
                val glyphColor = Color(symbol.type.colorHex)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, HorusSubtleBorder.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        .testTag("symbol_card_${symbol.type.name}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = HorusCardNavy)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Title & Glyph
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(glyphColor.copy(alpha = 0.2f))
                                    .border(1.dp, glyphColor, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(symbol.type.glyph, fontSize = 28.sp, color = glyphColor, fontWeight = FontWeight.Bold)
                            }

                            Column {
                                Text(
                                    text = if (isArabic) symbol.type.arName else symbol.type.enName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HorusIvory
                                )
                                Text(
                                    text = "${symbol.hieroglyphicCode} • ${if (isArabic) symbol.pronunciationAr else symbol.pronunciationEn}",
                                    color = HorusLightGold,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Authentic History Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = HorusMidnight.copy(alpha = 0.8f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isArabic) "🏛️ الأثر التاريخي الموثق:" else "🏛️ Authentic Historical Context:",
                                    color = HorusGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isArabic) symbol.authenticHistoryAr else symbol.authenticHistoryEn,
                                    color = HorusMutedIvory,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Modern Horus Code Assignment Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = HorusElevatedNavy.copy(alpha = 0.6f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isArabic) "💻 التوظيف البرمجي في شفرة حورس:" else "💻 Modern Horus Code Role:",
                                    color = HorusCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isArabic) symbol.modernHorusRoleAr else symbol.modernHorusRoleEn,
                                    color = HorusIvory,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${if (isArabic) "الصيغة: " else "Syntax: "}${if (isArabic) symbol.syntaxUsageAr else symbol.syntaxUsageEn}",
                                    color = HorusElectricCyan,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
