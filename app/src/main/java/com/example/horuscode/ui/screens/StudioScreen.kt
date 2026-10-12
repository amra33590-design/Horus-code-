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
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.engine.interpreter.HorusInterpreter
import com.example.horuscode.engine.interpreter.InterpreterResult
import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock
import com.example.horuscode.engine.model.SymbolCategory
import com.example.horuscode.engine.parser.HorusParser
import com.example.horuscode.ui.components.ConsoleView
import com.example.horuscode.ui.components.HorusBlockCard
import com.example.horuscode.ui.components.HorusStrings
import com.example.horuscode.ui.components.TraceViewer
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StudioScreen(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    // Workspace state using authentic hieroglyphs and Arabic identifiers by default!
    var blocks by remember {
        mutableStateOf(
            listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.SET, params = mapOf("var" to "النقاط", "val" to "10")),
                HorusBlock(type = BlockType.IF, params = mapOf("left" to "النقاط", "op" to ">", "right" to "5")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"ممتاز!\"")),
                HorusBlock(type = BlockType.ELSE),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"حاول ثانية\"")),
                HorusBlock(type = BlockType.END_IF),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "3")),
                HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"حورس\"")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            )
        )
    }

    var isCodeView by remember { mutableStateOf(false) }
    var codeText by remember { mutableStateOf(HorusParser.blocksToCode(blocks)) }

    var executionResult by remember { mutableStateOf<InterpreterResult?>(null) }
    var currentActiveBlockIndex by remember { mutableStateOf<Int?>(null) }
    var isStepping by remember { mutableStateOf(false) }
    var showAddBlockDialog by remember { mutableStateOf(false) }
    var showInputDialog by remember { mutableStateOf(false) }
    var inputPromptText by remember { mutableStateOf("أدخل قيمة") }
    var enteredInputValue by remember { mutableStateOf("") }
    var onInputSubmitted by remember { mutableStateOf<((String) -> Unit)?>(null) }

    var selectedTab by remember { mutableStateOf(0) } // 0: Console, 1: Trace

    // Keep code text synchronized when switching views
    LaunchedEffect(isCodeView) {
        if (isCodeView) {
            codeText = HorusParser.blocksToCode(blocks)
        }
    }

    // Input dialog
    if (showInputDialog) {
        AlertDialog(
            onDismissRequest = { showInputDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("𓋖", fontSize = 22.sp, color = HorusCyan)
                    Text(if (isArabic) "وزن المدخلات (ميزان ماعت)" else "User Input Request", color = HorusIvory)
                }
            },
            text = {
                Column {
                    Text(inputPromptText, color = HorusMutedIvory, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = enteredInputValue,
                        onValueChange = { enteredInputValue = it },
                        modifier = Modifier.fillMaxWidth().testTag("user_input_field"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = HorusIvory,
                            unfocusedTextColor = HorusIvory,
                            focusedBorderColor = HorusCyan
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showInputDialog = false
                        onInputSubmitted?.invoke(enteredInputValue)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy)
                ) {
                    Text(if (isArabic) "إدخال" else "Submit")
                }
            },
            containerColor = HorusCardNavy
        )
    }

    // Add Block Modal Dialog
    if (showAddBlockDialog) {
        AddBlockDialog(
            isArabic = isArabic,
            onDismiss = { showAddBlockDialog = false },
            onAddBlock = { newType ->
                showAddBlockDialog = false
                val newBlock = when (newType) {
                    BlockType.SET -> HorusBlock(type = newType, params = mapOf("var" to "س", "val" to "10"))
                    BlockType.PRINT -> HorusBlock(type = newType, params = mapOf("expr" to "\"حورس\""))
                    BlockType.INPUT -> HorusBlock(type = newType, params = mapOf("prompt" to "أدخل قيمة", "var" to "المدخل"))
                    BlockType.IF -> HorusBlock(type = newType, params = mapOf("left" to "س", "op" to ">", "right" to "0"))
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
        // Top Studio Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("☥", fontSize = 20.sp, color = HorusGold)
                    Text(
                        text = if (isArabic) "استوديو البرمجة بالهيروغليفية" else "Hieroglyphic Code Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HorusIvory
                    )
                }
                Text(
                    text = if (isArabic) "برمج مباشرة برموز الفراعنة: ☥ 𓋹 𓂀 𓆣 𓏛 𓋖 𓊗" else "Code directly with ancient Egyptian hieroglyphs",
                    style = MaterialTheme.typography.labelSmall,
                    color = HorusCyan
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = {
                        blocks = listOf(
                            HorusBlock(type = BlockType.START),
                            HorusBlock(type = BlockType.SET, params = mapOf("var" to "الأهرامات", "val" to "3")),
                            HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"تم تشييد: \" + الأهرامات")),
                            HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "الأهرامات")),
                            HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to "\"مجد كيميت!\"")),
                            HorusBlock(type = BlockType.END_REPEAT),
                            HorusBlock(type = BlockType.STOP)
                        )
                        executionResult = null
                        currentActiveBlockIndex = null
                    },
                    modifier = Modifier.testTag("load_example_button"),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusLightGold),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder))
                ) {
                    Text(HorusStrings.btnLoadExample(isArabic), fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        if (isCodeView) {
                            val (parsedBlocks, errors) = HorusParser.parseTextToBlocks(codeText)
                            if (errors.isEmpty()) {
                                blocks = parsedBlocks
                                isCodeView = false
                            } else {
                                executionResult = InterpreterResult(
                                    isSuccess = false,
                                    outputs = emptyList(),
                                    trace = emptyList(),
                                    variables = emptyMap(),
                                    actions = emptyList(),
                                    stepsCount = 0,
                                    errorMessageEn = errors.first().messageEn,
                                    errorMessageAr = errors.first().messageAr
                                )
                            }
                        } else {
                            codeText = HorusParser.blocksToCode(blocks)
                            isCodeView = true
                        }
                    },
                    modifier = Modifier.testTag("toggle_view_mode"),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HorusElevatedNavy, contentColor = HorusCyan)
                ) {
                    Icon(
                        imageVector = if (isCodeView) Icons.Default.GridView else Icons.Default.Code,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCodeView) HorusStrings.btnShowBlocks(isArabic) else HorusStrings.btnShowCode(isArabic),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hieroglyphic Quick Bar: Clickable keyboard row of Egyptian Glyphs!
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = HorusMidnight),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(HorusGold.copy(alpha = 0.4f)))
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                Text(
                    text = if (isArabic) "لوحة الرموز الهيروغليفية السريعة (انقر للإضافة):" else "Hieroglyph Palette (Tap to inscribe):",
                    color = HorusLightGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val glyphShortcuts = listOf(
                        Triple(BlockType.START, "☥ عنخ", "بدء"),
                        Triple(BlockType.SET, "𓋹 جد", "تثبيت"),
                        Triple(BlockType.PRINT, "𓏛 بردي", "تدوين"),
                        Triple(BlockType.IF, "𓂀 حورس", "بصيرة"),
                        Triple(BlockType.REPEAT, "𓆣 جعران", "تكرار"),
                        Triple(BlockType.INPUT, "𓋖 ماعت", "وزن"),
                        Triple(BlockType.ACTION, "𓊗 هريم", "انطلاق"),
                        Triple(BlockType.STOP, "☥ ختام", "ختام")
                    )

                    items(glyphShortcuts) { (type, label, _) ->
                        AssistChip(
                            onClick = {
                                if (isCodeView) {
                                    val snippet = when (type) {
                                        BlockType.START -> "\n☥ بدء\n"
                                        BlockType.STOP -> "\n☥ ختام\n"
                                        BlockType.SET -> "\n𓋹 س = 10\n"
                                        BlockType.PRINT -> "\n𓏛 \"حورس\"\n"
                                        BlockType.IF -> "\n𓂀 س > 5\n"
                                        BlockType.REPEAT -> "\n𓆣 3\n"
                                        BlockType.INPUT -> "\n𓋖 \"أدخل قيمة\" في المدخل\n"
                                        BlockType.ACTION -> "\n𓊗 تقدم\n"
                                        else -> ""
                                    }
                                    codeText += snippet
                                } else {
                                    val newBlock = when (type) {
                                        BlockType.SET -> HorusBlock(type = type, params = mapOf("var" to "س", "val" to "10"))
                                        BlockType.PRINT -> HorusBlock(type = type, params = mapOf("expr" to "\"حورس\""))
                                        BlockType.IF -> HorusBlock(type = type, params = mapOf("left" to "س", "op" to ">", "right" to "5"))
                                        BlockType.REPEAT -> HorusBlock(type = type, params = mapOf("times" to "3"))
                                        BlockType.ACTION -> HorusBlock(type = type, params = mapOf("action" to "MOVE_FORWARD"))
                                        else -> HorusBlock(type = type)
                                    }
                                    val stopIndex = blocks.indexOfFirst { it.type == BlockType.STOP }
                                    blocks = if (stopIndex != -1 && type != BlockType.STOP) {
                                        blocks.toMutableList().apply { add(stopIndex, newBlock) }
                                    } else blocks + newBlock
                                }
                            },
                            label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = HorusElevatedNavy,
                                labelColor = HorusIvory
                            ),
                            border = AssistChipDefaults.assistChipBorder(enabled = true).copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(type.symbolType.colorHex).copy(alpha = 0.5f))
                            ),
                            modifier = Modifier.testTag("quick_glyph_${type.name}")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Control Panel: Run, Step, Add Block, Reset, Clear
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = HorusCardNavy)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // RUN BUTTON
                    Button(
                        onClick = {
                            val activeBlocks = if (isCodeView) {
                                val (pBlocks, errs) = HorusParser.parseTextToBlocks(codeText)
                                if (errs.isNotEmpty()) {
                                    executionResult = InterpreterResult(
                                        isSuccess = false,
                                        outputs = emptyList(),
                                        trace = emptyList(),
                                        variables = emptyMap(),
                                        actions = emptyList(),
                                        stepsCount = 0,
                                        errorMessageEn = errs.first().messageEn,
                                        errorMessageAr = errs.first().messageAr
                                    )
                                    return@Button
                                }
                                blocks = pBlocks
                                pBlocks
                            } else blocks

                            val interpreter = HorusInterpreter(inputProvider = { "25" })
                            val res = interpreter.execute(activeBlocks)
                            executionResult = res
                            currentActiveBlockIndex = null
                            if (res.isSuccess) {
                                progressManager.markProgramExecuted()
                            }
                            selectedTab = 0
                        },
                        modifier = Modifier.testTag("run_program_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = HorusCyan, contentColor = HorusDeepNavy),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(HorusStrings.btnRun(isAr = isArabic), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    // STEP BUTTON
                    FilledTonalButton(
                        onClick = {
                            coroutineScope.launch {
                                isStepping = true
                                val interpreter = HorusInterpreter()
                                val res = interpreter.execute(blocks)
                                executionResult = res
                                selectedTab = 1
                                for (step in res.trace) {
                                    currentActiveBlockIndex = step.blockIndex
                                    delay(400)
                                }
                                isStepping = false
                            }
                        },
                        enabled = !isStepping,
                        modifier = Modifier.testTag("step_program_button"),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = HorusElevatedNavy, contentColor = HorusLightGold),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.SkipNext, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(HorusStrings.btnStep(isAr = isArabic), fontSize = 12.sp)
                    }

                    // ADD BLOCK
                    if (!isCodeView) {
                        OutlinedButton(
                            onClick = { showAddBlockDialog = true },
                            modifier = Modifier.testTag("add_block_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusIvory),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(HorusSubtleBorder)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(HorusStrings.btnAddBlock(isArabic), fontSize = 12.sp)
                        }
                    }
                }

                // Reset & Clear
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = {
                            executionResult = null
                            currentActiveBlockIndex = null
                        },
                        modifier = Modifier.size(34.dp).testTag("reset_studio_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = HorusMutedIvory, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = {
                            blocks = listOf(HorusBlock(type = BlockType.START), HorusBlock(type = BlockType.STOP))
                            executionResult = null
                            currentActiveBlockIndex = null
                        },
                        modifier = Modifier.size(34.dp).testTag("clear_workspace_button")
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Clear", tint = HorusErrorCrimson.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Center Area: Visual Blocks OR Text Code View
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (isCodeView) {
                OutlinedTextField(
                    value = codeText,
                    onValueChange = { codeText = it },
                    modifier = Modifier.fillMaxSize().testTag("text_code_editor"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = HorusIvory,
                        unfocusedTextColor = HorusIvory,
                        focusedContainerColor = HorusMidnight,
                        unfocusedContainerColor = HorusMidnight,
                        focusedBorderColor = HorusCyan,
                        unfocusedBorderColor = HorusSubtleBorder
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 22.sp
                    )
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().testTag("blocks_workspace_list"),
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
                            isCurrentActive = (currentActiveBlockIndex == index),
                            indentationLevel = currentIndent,
                            isArabic = isArabic,
                            onParamChange = { key, value ->
                                val updatedParams = block.params.toMutableMap().apply { put(key, value) }
                                blocks = blocks.toMutableList().apply {
                                    this[index] = block.copy(params = updatedParams)
                                }
                            },
                            onMoveUp = {
                                if (index > 0) {
                                    val list = blocks.toMutableList()
                                    val temp = list[index]
                                    list[index] = list[index - 1]
                                    list[index - 1] = temp
                                    blocks = list
                                }
                            },
                            onMoveDown = {
                                if (index < blocks.size - 1) {
                                    val list = blocks.toMutableList()
                                    val temp = list[index]
                                    list[index] = list[index + 1]
                                    list[index + 1] = temp
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

        Spacer(modifier = Modifier.height(8.dp))

        // Bottom Area: Tab Bar (Console / Trace)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                label = { Text(HorusStrings.tabConsole(isArabic), fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HorusCyan,
                    selectedLabelColor = HorusDeepNavy
                ),
                modifier = Modifier.testTag("tab_console")
            )
            FilterChip(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                label = { Text(HorusStrings.tabTrace(isArabic), fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HorusCyan,
                    selectedLabelColor = HorusDeepNavy
                ),
                modifier = Modifier.testTag("tab_trace")
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        when (selectedTab) {
            0 -> ConsoleView(
                outputs = executionResult?.outputs ?: emptyList(),
                isSuccess = executionResult?.isSuccess ?: true,
                errorMessage = if (isArabic) executionResult?.errorMessageAr else executionResult?.errorMessageEn,
                isArabic = isArabic,
                onClear = { executionResult = null }
            )
            1 -> TraceViewer(
                trace = executionResult?.trace ?: emptyList(),
                isArabic = isArabic
            )
        }
    }
}

@Composable
fun AddBlockDialog(
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onAddBlock: (BlockType) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<SymbolCategory?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("𓊗", fontSize = 22.sp, color = HorusGold)
                Text(if (isArabic) "صندوق الرموز الهيروغليفية" else "Hieroglyphic Toolbox", color = HorusIvory, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text(if (isArabic) "الكل" else "All", fontSize = 11.sp) }
                        )
                    }
                    items(SymbolCategory.values()) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = {
                                val catName = when (cat) {
                                    SymbolCategory.CORE -> if (isArabic) "الحياة والبداية" else "Lifecycle"
                                    SymbolCategory.VARIABLES -> if (isArabic) "الاستقرار والذاكرة" else "Memory"
                                    SymbolCategory.CONDITIONS -> if (isArabic) "بصيرة الشروط" else "Conditions"
                                    SymbolCategory.LOOPS -> if (isArabic) "دورات التكرار" else "Loops"
                                    SymbolCategory.IO -> if (isArabic) "التدوين والوزن" else "I/O"
                                    SymbolCategory.ACTIONS -> if (isArabic) "أوامر الانطلاق" else "Actions"
                                }
                                Text(catName, fontSize = 11.sp)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val availableTypes = BlockType.values().filter {
                    selectedCategory == null || it.symbolType.category == selectedCategory
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(availableTypes) { type ->
                        val color = Color(type.symbolType.colorHex)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAddBlock(type) }
                                .border(1.dp, HorusSubtleBorder.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .testTag("add_block_option_${type.name}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = HorusMidnight)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color.copy(alpha = 0.2f))
                                        .border(1.dp, color, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(type.symbolType.glyph, fontSize = 22.sp, color = color, fontWeight = FontWeight.Bold)
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isArabic) type.labelAr else type.labelEn,
                                        fontWeight = FontWeight.Bold,
                                        color = HorusIvory,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (isArabic) type.ancientMeaningAr else type.keyword,
                                        color = color,
                                        fontSize = 11.sp
                                    )
                                }

                                Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = HorusCyan, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isArabic) "إلغاء" else "Cancel", color = HorusMutedIvory)
            }
        },
        containerColor = HorusCardNavy
    )
}
