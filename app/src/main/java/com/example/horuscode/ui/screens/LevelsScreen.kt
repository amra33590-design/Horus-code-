package com.example.horuscode.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.horuscode.data.HorusLevel
import com.example.horuscode.data.LevelRepository
import com.example.horuscode.data.LevelValidationResult
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.engine.interpreter.HorusInterpreter
import com.example.horuscode.engine.interpreter.InterpreterResult
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock
import com.example.horuscode.ui.components.ConsoleView
import com.example.horuscode.ui.components.HorusBlockCard
import com.example.horuscode.ui.components.HorusStrings
import com.example.ui.theme.*

@Composable
fun LevelsScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val progress by progressManager.progressFlow.collectAsState()
    var selectedLevelId by remember { mutableStateOf(1) }
    val level = LevelRepository.levels.find { it.id == selectedLevelId } ?: LevelRepository.levels.first()

    // Level workspace blocks state
    var blocks by remember(level.id) { mutableStateOf(level.starterBlocks) }
    var executionResult by remember { mutableStateOf<InterpreterResult?>(null) }
    var validationResult by remember { mutableStateOf<LevelValidationResult?>(null) }
    var showHint by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showAddBlockDialog by remember { mutableStateOf(false) }

    // Success Celebration Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("𓂀", fontSize = 26.sp, color = HorusGold)
                    Text(
                        text = if (isArabic) "اكتمل التحدي بنجاح!" else "Trial Completed!",
                        color = HorusIvory,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isArabic) level.successExplanationAr else level.successExplanationEn,
                        color = HorusMutedIvory,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(HorusGold.copy(alpha = 0.2f))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "+${level.xpReward} XP",
                            color = HorusGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        if (selectedLevelId < 8) {
                            selectedLevelId++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy)
                ) {
                    Text(if (selectedLevelId < 8) HorusStrings.btnNextLevel(isArabic) else (if (isArabic) "رائع!" else "Done"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text(if (isArabic) "البقاء هنا" else "Stay here", color = HorusMutedIvory)
                }
            },
            containerColor = HorusCardNavy
        )
    }

    if (showAddBlockDialog) {
        AddBlockDialog(
            isArabic = isArabic,
            onDismiss = { showAddBlockDialog = false },
            onAddBlock = { newType ->
                showAddBlockDialog = false
                val newBlock = when (newType) {
                    BlockType.SET -> HorusBlock(type = newType, params = mapOf("var" to "x", "val" to "10"))
                    BlockType.PRINT -> HorusBlock(type = newType, params = mapOf("expr" to "\"Hello\""))
                    BlockType.INPUT -> HorusBlock(type = newType, params = mapOf("prompt" to "Enter value", "var" to "val"))
                    BlockType.IF -> HorusBlock(type = newType, params = mapOf("left" to "x", "op" to ">", "right" to "0"))
                    BlockType.REPEAT -> HorusBlock(type = newType, params = mapOf("times" to "3"))
                    BlockType.ACTION -> HorusBlock(type = newType, params = mapOf("action" to "MOVE_FORWARD"))
                    else -> HorusBlock(type = newType)
                }
                val stopIndex = blocks.indexOfFirst { it.type == BlockType.STOP }
                blocks = if (stopIndex != -1 && newType != BlockType.STOP) {
                    blocks.toMutableList().apply { add(stopIndex, newBlock) }
                } else {
                    blocks + newBlock
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp)
    ) {
        // Level Selector Bar (1 to 8 pills)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(LevelRepository.levels) { lvl ->
                val isCompleted = progress.completedLevels.contains(lvl.id)
                val isSelected = lvl.id == selectedLevelId

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedLevelId = lvl.id
                        executionResult = null
                        validationResult = null
                        showHint = false
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (isCompleted) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HorusSuccessGreen, modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "${if (isArabic) "المستوى" else "Lvl"} ${lvl.id}",
                                fontSize = 12.sp,
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
                    modifier = Modifier.testTag("level_chip_${lvl.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Level Brief Card: Title, Concept & Task
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = HorusCardNavy),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isArabic) level.titleAr else level.titleEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HorusGold
                        )
                        Text(
                            text = if (isArabic) level.conceptAr else level.conceptEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = HorusMutedIvory
                        )
                    }

                    IconButton(
                        onClick = { showHint = !showHint },
                        modifier = Modifier.testTag("toggle_hint_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Hint",
                            tint = if (showHint) HorusGold else HorusMutedIvory
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Task banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(HorusElevatedNavy)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🎯", fontSize = 14.sp)
                    Text(
                        text = if (isArabic) level.taskAr else level.taskEn,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = HorusIvory
                    )
                }

                // Expandable Hint
                AnimatedVisibility(visible = showHint) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(HorusGold.copy(alpha = 0.15f))
                            .padding(8.dp)
                    ) {
                        val hints = if (isArabic) level.hintsAr else level.hintsEn
                        hints.forEach { h ->
                            Text("💡 $h", color = HorusLightGold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Level Action Bar: Check Solution, Run, Add Block, Reset to Starter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Verify Solution Button
                Button(
                    onClick = {
                        val interpreter = HorusInterpreter(preloadedInputs = listOf("20", "50"))
                        val res = interpreter.execute(blocks)
                        executionResult = res
                        val validation = level.validate(res)
                        validationResult = validation

                        if (validation.passed) {
                            progressManager.markLevelCompleted(level.id, level.xpReward)
                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier.testTag("verify_solution_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HorusSuccessGreen,
                        contentColor = HorusDeepNavy
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(HorusStrings.btnCheckSolution(isArabic), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { showAddBlockDialog = true },
                    modifier = Modifier.testTag("level_add_block_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusIvory),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(HorusStrings.btnAddBlock(isArabic), fontSize = 11.sp)
                }
            }

            // Reset starter program button
            OutlinedButton(
                onClick = {
                    blocks = level.starterBlocks
                    executionResult = null
                    validationResult = null
                },
                modifier = Modifier.testTag("reset_starter_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusMutedIvory),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder)),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(HorusStrings.btnReset(isArabic), fontSize = 11.sp)
            }
        }

        // Validation Feedback Banner if tested
        if (validationResult != null) {
            Spacer(modifier = Modifier.height(6.dp))
            val isPassed = validationResult!!.passed
            Card(
                modifier = Modifier.fillMaxWidth().testTag("validation_feedback_card"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPassed) HorusSuccessGreen.copy(alpha = 0.2f) else HorusErrorCrimson.copy(alpha = 0.2f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isPassed) HorusSuccessGreen else HorusErrorCrimson))
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isPassed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (isPassed) HorusSuccessGreen else HorusErrorCrimson,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isArabic) validationResult!!.feedbackAr else validationResult!!.feedbackEn,
                        color = HorusIvory,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Center Area: Editable Blocks List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("level_blocks_list"),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            var currentIndent = 0
            itemsIndexed(blocks) { index, block ->
                if (block.type == BlockType.END_IF || block.type == BlockType.END_REPEAT || block.type == BlockType.ELSE) {
                    currentIndent = (currentIndent - 1).coerceAtLeast(0)
                }

                HorusBlockCard(
                    block = block,
                    index = index,
                    indentationLevel = currentIndent,
                    isArabic = isArabic,
                    onParamChange = { key, value ->
                        val updated = block.params.toMutableMap().apply { put(key, value) }
                        blocks = blocks.toMutableList().apply { this[index] = block.copy(params = updated) }
                    },
                    onMoveUp = {
                        if (index > 0) {
                            val list = blocks.toMutableList()
                            val t = list[index]
                            list[index] = list[index - 1]
                            list[index - 1] = t
                            blocks = list
                        }
                    },
                    onMoveDown = {
                        if (index < blocks.size - 1) {
                            val list = blocks.toMutableList()
                            val t = list[index]
                            list[index] = list[index + 1]
                            list[index + 1] = t
                            blocks = list
                        }
                    },
                    onDelete = {
                        blocks = blocks.toMutableList().apply { removeAt(index) }
                    },
                    canMoveUp = index > 0,
                    canMoveDown = index < blocks.size - 1
                )

                if (block.type == BlockType.IF || block.type == BlockType.REPEAT || block.type == BlockType.ELSE) {
                    currentIndent++
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bottom Console Output
        ConsoleView(
            outputs = executionResult?.outputs ?: emptyList(),
            isSuccess = executionResult?.isSuccess ?: true,
            errorMessage = if (isArabic) executionResult?.errorMessageAr else executionResult?.errorMessageEn,
            isArabic = isArabic,
            onClear = { executionResult = null }
        )
    }
}
