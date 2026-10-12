package com.example.horuscode.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock
import com.example.ui.theme.*

@Composable
fun HorusBlockCard(
    block: HorusBlock,
    index: Int,
    isCurrentActive: Boolean = false,
    indentationLevel: Int = 0,
    isArabic: Boolean = true,
    onParamChange: (String, String) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    modifier: Modifier = Modifier
) {
    val blockColor = Color(block.type.symbolType.colorHex)
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isCurrentActive) HorusGold else HorusSubtleBorder.copy(alpha = 0.6f),
        label = "block_border"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = (indentationLevel * 16).dp,
                top = 3.dp,
                bottom = 3.dp
            )
            .border(
                width = if (isCurrentActive) 2.5.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .testTag("block_card_${block.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentActive) HorusCardNavy.copy(alpha = 0.95f) else HorusCardNavy.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentActive) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Header: Glyph, Name, Indent, Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Glyph badge (Cartouche style)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(blockColor.copy(alpha = 0.2f))
                            .border(1.5.dp, blockColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = block.type.symbolType.glyph,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = blockColor
                        )
                    }

                    Column {
                        Text(
                            text = block.getDisplayName(isArabic),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = HorusIvory
                        )
                        Text(
                            text = if (isArabic) "${block.type.symbolType.arName} • ${block.type.hieroglyphicKeyword}" else "${block.type.symbolType.enName} • ${block.type.keyword}",
                            style = MaterialTheme.typography.labelSmall,
                            color = blockColor,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Controls: Up, Down, Delete
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (canMoveUp) {
                        IconButton(
                            onClick = onMoveUp,
                            modifier = Modifier.size(32.dp).testTag("move_up_${block.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Move Up",
                                tint = HorusMutedIvory,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    if (canMoveDown) {
                        IconButton(
                            onClick = onMoveDown,
                            modifier = Modifier.size(32.dp).testTag("move_down_${block.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Move Down",
                                tint = HorusMutedIvory,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).testTag("delete_${block.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete Block",
                            tint = HorusErrorCrimson.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Parameter Editor based on BlockType
            Spacer(modifier = Modifier.height(4.dp))
            BlockParameterRow(
                block = block,
                isArabic = isArabic,
                onParamChange = onParamChange
            )
        }
    }
}

@Composable
private fun BlockParameterRow(
    block: HorusBlock,
    isArabic: Boolean,
    onParamChange: (String, String) -> Unit
) {
    when (block.type) {
        BlockType.SET -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = block.params["var"] ?: "س",
                    onValueChange = { onParamChange("var", it) },
                    label = { Text(if (isArabic) "المتغير (في الذاكرة)" else "Variable", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f).testTag("input_var_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
                Text("=", color = HorusGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                OutlinedTextField(
                    value = block.params["val"] ?: "0",
                    onValueChange = { onParamChange("val", it) },
                    label = { Text(if (isArabic) "القيمة المثبتة" else "Value / Expr", fontSize = 11.sp) },
                    modifier = Modifier.weight(1.5f).testTag("input_val_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
            }
        }

        BlockType.PRINT -> {
            OutlinedTextField(
                value = block.params["expr"] ?: "\"\"",
                onValueChange = { onParamChange("expr", it) },
                label = { Text(if (isArabic) "النص أو المعادلة المراد تدوينها برمز البردي 𓏛" else "Inscribe on Papyrus 𓏛", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth().testTag("input_expr_${block.id}"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = HorusIvory,
                    unfocusedTextColor = HorusIvory,
                    focusedBorderColor = HorusCyan,
                    unfocusedBorderColor = HorusSubtleBorder
                )
            )
        }

        BlockType.INPUT -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = block.params["prompt"] ?: "أدخل قيمة",
                    onValueChange = { onParamChange("prompt", it) },
                    label = { Text(if (isArabic) "رسالة الطلب" else "Prompt", fontSize = 11.sp) },
                    modifier = Modifier.weight(1.5f).testTag("input_prompt_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
                OutlinedTextField(
                    value = block.params["var"] ?: "المدخل",
                    onValueChange = { onParamChange("var", it) },
                    label = { Text(if (isArabic) "في المتغير (وزن ماعت 𓋖)" else "Into Var", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f).testTag("input_target_var_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
            }
        }

        BlockType.IF -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = block.params["left"] ?: "س",
                    onValueChange = { onParamChange("left", it) },
                    label = { Text(if (isArabic) "الطرف الأول" else "Left", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f).testTag("input_if_left_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
                OutlinedTextField(
                    value = block.params["op"] ?: "==",
                    onValueChange = { onParamChange("op", it) },
                    label = { Text(if (isArabic) "المعامل" else "Op", fontSize = 11.sp) },
                    modifier = Modifier.weight(0.7f).testTag("input_if_op_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusGold,
                        unfocusedTextColor = HorusGold,
                        focusedBorderColor = HorusGold,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
                OutlinedTextField(
                    value = block.params["right"] ?: "0",
                    onValueChange = { onParamChange("right", it) },
                    label = { Text(if (isArabic) "الطرف الثاني" else "Right", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f).testTag("input_if_right_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
            }
        }

        BlockType.REPEAT -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isArabic) "دورات في مسار الجعران 𓆣:" else "Scarab 𓆣 cycles:",
                    color = HorusLightGold,
                    fontSize = 13.sp
                )
                OutlinedTextField(
                    value = block.params["times"] ?: "3",
                    onValueChange = { onParamChange("times", it) },
                    modifier = Modifier.width(100.dp).testTag("input_repeat_times_${block.id}"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusLightGold,
                        unfocusedTextColor = HorusLightGold,
                        focusedBorderColor = HorusGold,
                        unfocusedBorderColor = HorusSubtleBorder
                    )
                )
            }
        }

        BlockType.ACTION -> {
            val currentAction = block.params["action"] ?: "MOVE_FORWARD"
            val actionOptions = listOf("MOVE_FORWARD", "TURN_LEFT", "TURN_RIGHT", "COLLECT_KEY")
            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth().testTag("action_dropdown_trigger_${block.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusGold),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
                ) {
                    val label = when (currentAction) {
                        "MOVE_FORWARD", "تقدم" -> if (isArabic) "𓊗 تقدم للأمام خطوة" else "MOVE_FORWARD"
                        "TURN_LEFT", "يسار" -> if (isArabic) "𓊗 استدر لليسار" else "TURN_LEFT"
                        "TURN_RIGHT", "يمين" -> if (isArabic) "𓊗 استدر لليمين" else "TURN_RIGHT"
                        "COLLECT_KEY", "التقط_المفتاح" -> if (isArabic) "𓊗 التقط المفتاح الذهبي" else "COLLECT_KEY"
                        else -> currentAction
                    }
                    Text(label)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(HorusCardNavy)
                ) {
                    actionOptions.forEach { opt ->
                        DropdownMenuItem(
                            text = {
                                val optLabel = when (opt) {
                                    "MOVE_FORWARD" -> if (isArabic) "𓊗 تقدم للأمام خطوة" else "MOVE_FORWARD"
                                    "TURN_LEFT" -> if (isArabic) "𓊗 استدر لليسار" else "TURN_LEFT"
                                    "TURN_RIGHT" -> if (isArabic) "𓊗 استدر لليمين" else "TURN_RIGHT"
                                    "COLLECT_KEY" -> if (isArabic) "𓊗 التقط المفتاح الذهبي" else "COLLECT_KEY"
                                    else -> opt
                                }
                                Text(optLabel, color = HorusIvory)
                            },
                            onClick = {
                                onParamChange("action", opt)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        BlockType.START, BlockType.STOP, BlockType.ELSE, BlockType.END_IF, BlockType.END_REPEAT -> {
            Text(
                text = block.getSummary(isArabic),
                style = MaterialTheme.typography.bodySmall,
                color = HorusMutedIvory,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}
