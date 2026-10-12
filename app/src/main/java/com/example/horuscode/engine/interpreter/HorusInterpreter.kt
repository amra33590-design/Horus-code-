package com.example.horuscode.engine.interpreter

import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock
import com.example.horuscode.engine.model.HorusValue
import com.example.horuscode.engine.model.TraceStep
import com.example.horuscode.engine.parser.ExpressionEvaluator

data class InterpreterResult(
    val isSuccess: Boolean,
    val outputs: List<String>,
    val trace: List<TraceStep>,
    val variables: Map<String, HorusValue>,
    val actions: List<String>,
    val stepsCount: Int,
    val errorMessageEn: String? = null,
    val errorMessageAr: String? = null
)

class HorusInterpreter(
    private val maxSteps: Int = 1000,
    private val inputProvider: ((prompt: String) -> String)? = null,
    private val preloadedInputs: List<String> = emptyList()
) {
    private val variables = mutableMapOf<String, HorusValue>()
    private val outputs = mutableListOf<String>()
    private val trace = mutableListOf<TraceStep>()
    private val actions = mutableListOf<String>()
    private var inputIndex = 0

    fun execute(blocks: List<HorusBlock>): InterpreterResult {
        variables.clear()
        outputs.clear()
        trace.clear()
        actions.clear()
        inputIndex = 0

        if (blocks.isEmpty()) {
            return InterpreterResult(
                isSuccess = true,
                outputs = emptyList(),
                trace = emptyList(),
                variables = emptyMap(),
                actions = emptyList(),
                stepsCount = 0
            )
        }

        var pc = 0
        var steps = 0

        // Call stack for loops: holds (loopStartPc, currentIteration, totalIterations)
        data class LoopFrame(val loopStartPc: Int, var currentIteration: Int, val totalIterations: Int)
        val loopStack = ArrayDeque<LoopFrame>()

        while (pc < blocks.size) {
            steps++
            if (steps > maxSteps) {
                return InterpreterResult(
                    isSuccess = false,
                    outputs = outputs,
                    trace = trace,
                    variables = variables,
                    actions = actions,
                    stepsCount = steps,
                    errorMessageEn = "Execution limit exceeded ($maxSteps steps). Check for infinite loops in Scarab 𓆣 block.",
                    errorMessageAr = "تم تجاوز الحد الأقصى للتنفيذ ($maxSteps خطوة). تحقق من عدم وجود حلقة لا نهائية في كتلة الجعران 𓆣."
                )
            }

            val block = blocks[pc]
            var outputProduced: String? = null
            var explanationEn = ""
            var explanationAr = ""

            try {
                when (block.type) {
                    BlockType.START -> {
                        explanationEn = "Program initiated at Ankh ☥ boundary."
                        explanationAr = "بدء تنفيذ البرنامج عند رمز الحياة عنخ ☥."
                        pc++
                    }

                    BlockType.STOP -> {
                        explanationEn = "Program stopped cleanly at Ankh ☥ terminal."
                        explanationAr = "اكتمال وتنفيذ البرنامج بأمان عند رمز عنخ ☥."
                        recordTrace(steps, pc, block, explanationEn, explanationAr, null)
                        break
                    }

                    BlockType.SET -> {
                        val varName = block.params["var"] ?: "x"
                        val valExpr = block.params["val"] ?: "0"
                        val evaluated = ExpressionEvaluator.evaluate(valExpr, variables)
                        variables[varName] = evaluated
                        explanationEn = "Djed 𓋹 stored $evaluated in memory variable '$varName'."
                        explanationAr = "قام عمود جد 𓋹 بحفظ القيمة $evaluated في الذاكرة بالمتغير '$varName'."
                        pc++
                    }

                    BlockType.PRINT -> {
                        val expr = block.params["expr"] ?: "\"\""
                        val evaluated = ExpressionEvaluator.evaluate(expr, variables)
                        val text = evaluated.toString()
                        outputs.add(text)
                        outputProduced = text
                        explanationEn = "Papyrus 𓏛 output inscribed: '$text'."
                        explanationAr = "دونت لفافة البردي 𓏛 في شاشة المخرجات: '$text'."
                        pc++
                    }

                    BlockType.INPUT -> {
                        val prompt = block.params["prompt"] ?: "Enter input"
                        val varName = block.params["var"] ?: "inputVal"
                        val inputValueStr = if (inputIndex < preloadedInputs.size) {
                            preloadedInputs[inputIndex++]
                        } else {
                            inputProvider?.invoke(prompt) ?: "10"
                        }
                        val evaluated = ExpressionEvaluator.evaluate(inputValueStr, variables)
                        variables[varName] = evaluated
                        explanationEn = "Feather 𓋖 received input '$inputValueStr' and weighed into '$varName'."
                        explanationAr = "استقبلت ريشة ماعت 𓋖 المدخل '$inputValueStr' وحفظته في '$varName'."
                        pc++
                    }

                    BlockType.IF -> {
                        val leftExpr = block.params["left"] ?: "true"
                        val op = block.params["op"] ?: "=="
                        val rightExpr = block.params["right"] ?: "true"

                        val conditionExpr = if (block.params.containsKey("op")) {
                            "$leftExpr $op $rightExpr"
                        } else {
                            leftExpr
                        }
                        val condResult = ExpressionEvaluator.evaluate(conditionExpr, variables).isTruthy()

                        if (condResult) {
                            explanationEn = "Eye of Horus 𓂀 evaluated condition as TRUE; entering branch."
                            explanationAr = "أقرت عين حورس 𓂀 صحة الشرط (صواب)؛ تنفيذ فرع الأوامر."
                            pc++
                        } else {
                            explanationEn = "Eye of Horus 𓂀 evaluated condition as FALSE; skipping to ELSE / END_IF."
                            explanationAr = "أقرت عين حورس 𓂀 عدم تحقق الشرط (خطأ)؛ الانتقال إلى فرع وإلا أو نهاية الشرط."
                            pc = findMatchingElseOrEndIf(blocks, pc)
                        }
                    }

                    BlockType.ELSE -> {
                        // If we hit ELSE during regular execution of the true-branch, skip to END_IF
                        explanationEn = "Condition was already fulfilled; skipping ELSE block."
                        explanationAr = "تم تنفيذ الشرط سابقاً؛ تجاوز كتلة وإلا (ELSE)."
                        pc = findMatchingEndIf(blocks, pc)
                    }

                    BlockType.END_IF -> {
                        explanationEn = "End of conditional decision."
                        explanationAr = "نهاية نطاق القرار الشرطي."
                        pc++
                    }

                    BlockType.REPEAT -> {
                        val timesExpr = block.params["times"] ?: "3"
                        val timesVal = ExpressionEvaluator.evaluate(timesExpr, variables).asNumber().toInt().coerceAtLeast(0)

                        if (timesVal > 0) {
                            loopStack.addLast(LoopFrame(loopStartPc = pc, currentIteration = 1, totalIterations = timesVal))
                            explanationEn = "Scarab 𓆣 began loop cycle 1 of $timesVal."
                            explanationAr = "بدأ الجعران 𓆣 دورة التكرار 1 من أصل $timesVal."
                            pc++
                        } else {
                            explanationEn = "Scarab 𓆣 count was $timesVal; skipping loop."
                            explanationAr = "عدد تكرار الجعران 𓆣 كان $timesVal؛ تجاوز الحلقة."
                            pc = findMatchingEndRepeat(blocks, pc)
                        }
                    }

                    BlockType.END_REPEAT -> {
                        if (loopStack.isNotEmpty()) {
                            val frame = loopStack.last()
                            if (frame.currentIteration < frame.totalIterations) {
                                frame.currentIteration++
                                explanationEn = "Scarab 𓆣 cycling to iteration ${frame.currentIteration} of ${frame.totalIterations}."
                                explanationAr = "تدوير حلقة الجعران 𓆣 للدورة ${frame.currentIteration} من ${frame.totalIterations}."
                                pc = frame.loopStartPc + 1
                            } else {
                                loopStack.removeLast()
                                explanationEn = "Scarab 𓆣 completed all ${frame.totalIterations} iterations."
                                explanationAr = "أتم الجعران 𓆣 جميع دورات التكرار (${frame.totalIterations})."
                                pc++
                            }
                        } else {
                            pc++
                        }
                    }

                    BlockType.ACTION -> {
                        val actionName = block.params["action"] ?: "MOVE_FORWARD"
                        actions.add(actionName)
                        explanationEn = "Pyramidion 𓊗 dispatched action: '$actionName'."
                        explanationAr = "أطلق الهريم 𓊗 أمر التنفيذ: '$actionName'."
                        pc++
                    }
                }

                recordTrace(steps, pc - 1, block, explanationEn, explanationAr, outputProduced)

            } catch (e: Exception) {
                val errorMsg = e.message ?: "Runtime error during instruction execution"
                return InterpreterResult(
                    isSuccess = false,
                    outputs = outputs,
                    trace = trace,
                    variables = variables,
                    actions = actions,
                    stepsCount = steps,
                    errorMessageEn = "Error at block #${pc + 1} (${block.type.name}): $errorMsg",
                    errorMessageAr = "خطأ في الكتلة #${pc + 1} (${block.type.labelAr}): $errorMsg"
                )
            }
        }

        return InterpreterResult(
            isSuccess = true,
            outputs = outputs,
            trace = trace,
            variables = variables,
            actions = actions,
            stepsCount = steps
        )
    }

    private fun recordTrace(
        stepNumber: Int,
        blockIndex: Int,
        block: HorusBlock,
        explanationEn: String,
        explanationAr: String,
        outputProduced: String?
    ) {
        val varsSnapshot = variables.mapValues { it.value.toString() }
        trace.add(
            TraceStep(
                stepNumber = stepNumber,
                blockIndex = blockIndex,
                instruction = block.toCodeString(),
                explanationEn = explanationEn,
                explanationAr = explanationAr,
                variablesSnapshot = varsSnapshot,
                outputProduced = outputProduced
            )
        )
    }

    private fun findMatchingElseOrEndIf(blocks: List<HorusBlock>, currentIfPc: Int): Int {
        var depth = 0
        for (i in (currentIfPc + 1) until blocks.size) {
            val b = blocks[i]
            if (b.type == BlockType.IF) {
                depth++
            } else if (b.type == BlockType.ELSE && depth == 0) {
                return i + 1 // execute next block inside else
            } else if (b.type == BlockType.END_IF) {
                if (depth == 0) return i + 1
                depth--
            }
        }
        return blocks.size
    }

    private fun findMatchingEndIf(blocks: List<HorusBlock>, currentPc: Int): Int {
        var depth = 0
        for (i in (currentPc + 1) until blocks.size) {
            val b = blocks[i]
            if (b.type == BlockType.IF) depth++
            else if (b.type == BlockType.END_IF) {
                if (depth == 0) return i + 1
                depth--
            }
        }
        return blocks.size
    }

    private fun findMatchingEndRepeat(blocks: List<HorusBlock>, currentRepeatPc: Int): Int {
        var depth = 0
        for (i in (currentRepeatPc + 1) until blocks.size) {
            val b = blocks[i]
            if (b.type == BlockType.REPEAT) depth++
            else if (b.type == BlockType.END_REPEAT) {
                if (depth == 0) return i + 1
                depth--
            }
        }
        return blocks.size
    }
}
