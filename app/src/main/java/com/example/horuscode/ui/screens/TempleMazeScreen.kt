package com.example.horuscode.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.Direction
import com.example.horuscode.data.MazeMap
import com.example.horuscode.data.MazeState
import com.example.horuscode.data.TempleMazeRepository
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.engine.interpreter.HorusInterpreter
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock
import com.example.horuscode.ui.components.HorusBlockCard
import com.example.horuscode.ui.components.HorusStrings
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TempleMazeScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val progress by progressManager.progressFlow.collectAsState()

    var selectedMapId by remember { mutableStateOf(1) }
    val currentMap = TempleMazeRepository.maps.find { it.id == selectedMapId } ?: TempleMazeRepository.maps.first()

    // Maze explorer state
    var mazeState by remember(currentMap.id) {
        mutableStateOf(
            MazeState(
                playerX = currentMap.startX,
                playerY = currentMap.startY,
                direction = currentMap.startDir
            )
        )
    }

    var blocks by remember(currentMap.id) { mutableStateOf(currentMap.starterBlocks) }
    var isExecuting by remember { mutableStateOf(false) }
    var executionMessage by remember { mutableStateOf<String?>(null) }
    var showWinDialog by remember { mutableStateOf(false) }

    // Win dialog
    if (showWinDialog) {
        AlertDialog(
            onDismissRequest = { showWinDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("👑", fontSize = 24.sp)
                    Text(if (isArabic) "فتح المعبد القديم!" else "Sanctuary Unlocked!", color = HorusGold, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (isArabic) "نجح برنامجك في قيادة مستكشف حورس إلى المحراب الذهبي ببراعة خوارزمية تامة!"
                        else "Your Horus Code program successfully guided the explorer to the Golden Shrine through algorithmic precision!",
                        color = HorusIvory,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "+150 XP",
                        color = HorusGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showWinDialog = false
                        if (selectedMapId < 3) selectedMapId++
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy)
                ) {
                    Text(if (selectedMapId < 3) (if (isArabic) "المتاهة التالية" else "Next Maze") else (if (isArabic) "إغلاق" else "Done"))
                }
            },
            containerColor = HorusCardNavy
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HorusDeepNavy)
            .padding(12.dp)
    ) {
        // Map Selection Tabs
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TempleMazeRepository.maps) { m ->
                val isSolved = progress.solvedMazes.contains(m.id)
                val isSelected = m.id == selectedMapId

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedMapId = m.id
                        mazeState = MazeState(m.startX, m.startY, m.startDir)
                        executionMessage = null
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (isSolved) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HorusSuccessGreen, modifier = Modifier.size(14.dp))
                            }
                            Text(if (isArabic) m.nameAr else m.nameEn, fontSize = 12.sp)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HorusCyan,
                        selectedLabelColor = HorusDeepNavy,
                        containerColor = HorusCardNavy,
                        labelColor = HorusIvory
                    ),
                    modifier = Modifier.testTag("maze_map_chip_${m.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Grid Canvas & Status
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, HorusSubtleBorder, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = HorusMidnight)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Info header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) currentMap.nameAr else currentMap.nameEn,
                        color = HorusGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Pos: (${mazeState.playerX}, ${mazeState.playerY}) • ${mazeState.direction.name}",
                        color = HorusCyan,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // The Interactive Maze Grid
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(HorusCardNavy)
                        .testTag("maze_grid_canvas"),
                    contentAlignment = Alignment.Center
                ) {
                    MazeGridCanvas(
                        map = currentMap,
                        state = mazeState
                    )
                }

                // Execution message banner
                if (executionMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = executionMessage!!,
                        color = if (mazeState.isWin) HorusSuccessGreen else HorusErrorCrimson,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Control buttons: Run In Maze, Reset Maze, Quick Add Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = {
                        if (isExecuting) return@Button
                        coroutineScope.launch {
                            isExecuting = true
                            executionMessage = null
                            // Reset state
                            mazeState = MazeState(currentMap.startX, currentMap.startY, currentMap.startDir)
                            delay(200)

                            val interpreter = HorusInterpreter()
                            val res = interpreter.execute(blocks)

                            if (!res.isSuccess) {
                                executionMessage = if (isArabic) res.errorMessageAr else res.errorMessageEn
                                isExecuting = false
                                return@launch
                            }

                            // Animate actions step-by-step on grid
                            for (action in res.actions) {
                                mazeState = TempleMazeRepository.simulateStep(mazeState, action, currentMap)
                                delay(350)
                                if (mazeState.isFailed) {
                                    executionMessage = if (isArabic) mazeState.failReasonAr else mazeState.failReasonEn
                                    break
                                }
                                if (mazeState.isWin) {
                                    executionMessage = if (isArabic) "وصلت إلى قدس الأقداس بنجاح!" else "Sanctuary reached!"
                                    progressManager.markMazeSolved(currentMap.id, 150)
                                    showWinDialog = true
                                    break
                                }
                            }

                            if (!mazeState.isWin && !mazeState.isFailed) {
                                executionMessage = if (isArabic) "انتهى البرنامج دون الوصول للمحراب. أضف أوامر أخرى!" else "Program finished before reaching the shrine. Add more commands!"
                            }
                            isExecuting = false
                        }
                    },
                    enabled = !isExecuting,
                    modifier = Modifier.testTag("run_maze_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isArabic) "تشغيل في المعبد" else "Run in Maze", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        mazeState = MazeState(currentMap.startX, currentMap.startY, currentMap.startDir)
                        executionMessage = null
                    },
                    modifier = Modifier.testTag("reset_maze_state"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusMutedIvory),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(HorusStrings.btnReset(isArabic), fontSize = 11.sp)
                }
            }

            // Quick Add action button
            IconButton(
                onClick = {
                    val stopIdx = blocks.indexOfFirst { it.type == BlockType.STOP }
                    val newBlock = HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD"))
                    blocks = if (stopIdx != -1) {
                        blocks.toMutableList().apply { add(stopIdx, newBlock) }
                    } else blocks + newBlock
                },
                modifier = Modifier.size(34.dp).testTag("quick_add_move_forward")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Action", tint = HorusLightGold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Code Blocks Workspace for Maze
        Text(
            text = if (isArabic) "أوامر المستكشف في المعبد:" else "Explorer Navigation Instructions:",
            color = HorusMutedIvory,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("maze_blocks_list"),
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
    }
}

@Composable
fun MazeGridCanvas(
    map: MazeMap,
    state: MazeState,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize().padding(8.dp)) {
        val cellW = size.width / map.width
        val cellH = size.height / map.height

        // Draw grid tiles
        for (y in 0 until map.height) {
            for (x in 0 until map.width) {
                val left = x * cellW
                val top = y * cellH

                val isWall = map.walls.contains(x to y)
                val isGoal = (x == map.goalX && y == map.goalY)
                val isKey = map.keys.contains(x to y) && !state.collectedKeys.contains(x to y)

                if (isWall) {
                    // Sandstone column obstacle
                    drawRoundRect(
                        color = Color(0xFF1B2A4A),
                        topLeft = Offset(left + 2, top + 2),
                        size = Size(cellW - 4, cellH - 4),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(left + 5, top + 5),
                        size = Size(cellW - 10, cellH - 10),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                } else {
                    // Pathway tile
                    drawRoundRect(
                        color = Color(0xFF0A1128),
                        topLeft = Offset(left + 1, top + 1),
                        size = Size(cellW - 2, cellH - 2),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    drawRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(left, top),
                        size = Size(cellW, cellH),
                        style = Stroke(width = 0.5f)
                    )
                }

                // Goal Golden Shrine
                if (isGoal) {
                    drawCircle(
                        color = Color(0xFFFFD700),
                        radius = cellW * 0.35f,
                        center = Offset(left + cellW / 2, top + cellH / 2)
                    )
                    drawCircle(
                        color = Color(0xFF0A1128),
                        radius = cellW * 0.18f,
                        center = Offset(left + cellW / 2, top + cellH / 2)
                    )
                }

                // Key Gem
                if (isKey) {
                    drawCircle(
                        color = Color(0xFF48DBFB),
                        radius = cellW * 0.25f,
                        center = Offset(left + cellW / 2, top + cellH / 2)
                    )
                }
            }
        }

        // Draw Player Character (Horus Explorer)
        val pLeft = state.playerX * cellW
        val pTop = state.playerY * cellH
        val pCenterX = pLeft + cellW / 2
        val pCenterY = pTop + cellH / 2

        // Glowing aura
        drawCircle(
            color = Color(0xFF00D2D3).copy(alpha = 0.3f),
            radius = cellW * 0.45f,
            center = Offset(pCenterX, pCenterY)
        )
        // Explorer body
        drawCircle(
            color = Color(0xFF00D2D3),
            radius = cellW * 0.32f,
            center = Offset(pCenterX, pCenterY)
        )

        // Direction pointer (beak / arrow)
        val dirAngleRad = Math.toRadians(state.direction.angle.toDouble() - 90.0)
        val arrowX = pCenterX + (cellW * 0.3f * Math.cos(dirAngleRad)).toFloat()
        val arrowY = pCenterY + (cellW * 0.3f * Math.sin(dirAngleRad)).toFloat()

        drawCircle(
            color = Color(0xFFFFD700),
            radius = cellW * 0.12f,
            center = Offset(arrowX, arrowY)
        )
    }
}
