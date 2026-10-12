package com.example.horuscode.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.DebuggingChallenge
import com.example.horuscode.data.DebuggingRepository
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
fun DebuggingScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val progress by progressManager.progressFlow.collectAsState()
    var selectedChallengeId by remember { mutableStateOf(1) }
    val challenge = DebuggingRepository.challenges.find { it.id == selectedChallengeId } ?: DebuggingRepository.challenges.first()

    var blocks by remember(challenge.id) { mutableStateOf(challenge.brokenBlocks) }
    var executionResult by remember { mutableStateOf<InterpreterResult?>(null) }
    var testResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var showHint by remember { mutableStateOf(false) }
    var showAddBlockDialog by remember { mutableStateOf(false) }

    if (showAddBlockDialog) {
        AddBlockDialog(
            isArabic = isArabic,
            onDismiss = { showAddBlockDialog = false },
            onAddBlock = { newType ->
                showAddBlockDialog = false
                val newBlock = when (newType) {
                    BlockType.SET -> HorusBlock(type = newType, params = mapOf("var" to "score", "val" to "50"))
                    BlockType.PRINT -> HorusBlock(type = newType, params = mapOf("expr" to "\"Active\""))
                    BlockType.IF -> HorusBlock(type = newType, params = mapOf("left" to "energy", "op" to ">", "right" to "10"))
                    BlockType.REPEAT -> HorusBlock(type = newType, params = mapOf("times" to "5"))
                    else -> HorusBlock(type = newType)
                }
                val stopIndex = blocks.indexOfFirst { it.type == BlockType.STOP }
                blocks = if (stopIndex != -1 && newType != BlockType.STOP) {
                    blocks.toMutableList().apply { add(stopIndex, newBlock) }
                } else blocks + newBlock
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp)
    ) {
        // Challenges Selector Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DebuggingRepository.challenges) { c ->
                val isSolved = progress.solvedDebugging.contains(c.id)
                val isSelected = c.id == selectedChallengeId

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedChallengeId = c.id
                        blocks = c.brokenBlocks
                        executionResult = null
                        testResult = null
                        showHint = false
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (isSolved) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HorusSuccessGreen, modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "${if (isArabic) "لغز" else "Bug"} ${c.id}",
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
                    modifier = Modifier.testTag("debug_chip_${c.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bug Brief Card
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
                            text = if (isArabic) challenge.titleAr else challenge.titleEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HorusGold
                        )
                        Text(
                            text = if (isArabic) challenge.bugCategoryAr else challenge.bugCategoryEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = HorusCyan
                        )
                    }

                    IconButton(onClick = { showHint = !showHint }) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = if (showHint) HorusGold else HorusMutedIvory)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isArabic) challenge.descriptionAr else challenge.descriptionEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = HorusIvory
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(HorusMidnight)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🎯", fontSize = 13.sp)
                    Text(
                        text = "${if (isArabic) "المطلوب: " else "Target: "}${if (isArabic) challenge.intendedOutcomeAr else challenge.intendedOutcomeEn}",
                        color = HorusLightGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                AnimatedVisibility(visible = showHint) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(HorusGold.copy(alpha = 0.15f))
                            .padding(8.dp)
                    ) {
                        Text("💡 ${if (isArabic) challenge.fixHintAr else challenge.fixHintEn}", color = HorusLightGold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Actions Bar: Test & Fix, Add Block, Reset to Broken
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = {
                        val interpreter = HorusInterpreter()
                        val res = interpreter.execute(blocks)
                        executionResult = res
                        val test = challenge.validate(res)
                        testResult = test
                        if (test.first) {
                            progressManager.markDebuggingSolved(challenge.id, challenge.xpReward)
                        }
                    },
                    modifier = Modifier.testTag("test_debug_solution"),
                    colors = ButtonDefaults.buttonColors(containerColor = HorusSuccessGreen, contentColor = HorusDeepNavy),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isArabic) "تشغيل وفحص الإصلاح" else "Test & Repair", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { showAddBlockDialog = true },
                    modifier = Modifier.testTag("debug_add_block"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusIvory),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(HorusStrings.btnAddBlock(isArabic), fontSize = 11.sp)
                }
            }

            OutlinedButton(
                onClick = {
                    blocks = challenge.brokenBlocks
                    executionResult = null
                    testResult = null
                },
                modifier = Modifier.testTag("reset_broken_blocks"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusMutedIvory),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(HorusStrings.btnReset(isArabic), fontSize = 11.sp)
            }
        }

        if (testResult != null) {
            Spacer(modifier = Modifier.height(6.dp))
            val isSuccess = testResult!!.first
            Card(
                modifier = Modifier.fillMaxWidth().testTag("debug_feedback_banner"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSuccess) HorusSuccessGreen.copy(alpha = 0.2f) else HorusErrorCrimson.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (isSuccess) HorusSuccessGreen else HorusErrorCrimson
                    )
                    Text(
                        text = testResult!!.second,
                        color = HorusIvory,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Blocks editor
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("debug_blocks_list"),
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

        ConsoleView(
            outputs = executionResult?.outputs ?: emptyList(),
            isSuccess = executionResult?.isSuccess ?: true,
            errorMessage = if (isArabic) executionResult?.errorMessageAr else executionResult?.errorMessageEn,
            isArabic = isArabic,
            onClear = { executionResult = null }
        )
    }
}
