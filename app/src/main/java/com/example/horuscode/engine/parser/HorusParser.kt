package com.example.horuscode.engine.parser

import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock

data class ParseError(
    val lineNumber: Int,
    val lineText: String,
    val messageEn: String,
    val messageAr: String
)

object HorusParser {

    fun parseTextToBlocks(code: String): Pair<List<HorusBlock>, List<ParseError>> {
        val blocks = mutableListOf<HorusBlock>()
        val errors = mutableListOf<ParseError>()
        val lines = code.lines()

        for ((index, rawLine) in lines.withIndex()) {
            val lineNum = index + 1
            val trimmed = rawLine.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed.startsWith("//")) {
                continue
            }

            try {
                when {
                    // Ankh ☥ (Life Start / Life Stop)
                    trimmed.startsWith("☥") -> {
                        val rest = trimmed.removePrefix("☥").trim().lowercase()
                        if (rest.contains("ختام") || rest.contains("stop") || rest.contains("توقف") || rest.contains("نهاية")) {
                            blocks.add(HorusBlock(type = BlockType.STOP))
                        } else {
                            blocks.add(HorusBlock(type = BlockType.START))
                        }
                    }
                    trimmed.equals("START", ignoreCase = true) || trimmed == "بدء" || trimmed == "بداية" -> {
                        blocks.add(HorusBlock(type = BlockType.START))
                    }
                    trimmed.equals("STOP", ignoreCase = true) || trimmed == "ختام" || trimmed == "نهاية" || trimmed == "توقف" -> {
                        blocks.add(HorusBlock(type = BlockType.STOP))
                    }

                    // Djed 𓋹 (Stability, Memory, Variables)
                    trimmed.startsWith("𓋹") || trimmed.startsWith("SET", ignoreCase = true) || trimmed.startsWith("تثبيت") || trimmed.startsWith("تعيين") -> {
                        val cleanLine = when {
                            trimmed.startsWith("𓋹") -> trimmed.removePrefix("𓋹").trim()
                            trimmed.startsWith("SET", ignoreCase = true) -> trimmed.substring(3).trim()
                            trimmed.startsWith("تثبيت") -> trimmed.removePrefix("تثبيت").trim()
                            trimmed.startsWith("تعيين") -> trimmed.removePrefix("تعيين").trim()
                            else -> trimmed
                        }
                        val eqIdx = cleanLine.indexOf('=')
                        if (eqIdx == -1) {
                            errors.add(ParseError(
                                lineNum, trimmed,
                                "Missing '=' in Djed 𓋹 variable instruction. Format: 𓋹 var = value",
                                "علامة '=' مفقودة في أمر عمود جد 𓋹 لتثبيت المتغير. الصيغة: 𓋹 المتغير = القيمة"
                            ))
                        } else {
                            val varName = cleanLine.substring(0, eqIdx).trim()
                            val expr = cleanLine.substring(eqIdx + 1).trim()
                            blocks.add(HorusBlock(type = BlockType.SET, params = mapOf("var" to varName, "val" to expr)))
                        }
                    }

                    // Papyrus 𓏛 (Wisdom Inscription, Output)
                    trimmed.startsWith("𓏛") || trimmed.startsWith("PRINT", ignoreCase = true) || trimmed.startsWith("تدوين") || trimmed.startsWith("اطبع") -> {
                        val cleanLine = when {
                            trimmed.startsWith("𓏛") -> trimmed.removePrefix("𓏛").trim()
                            trimmed.startsWith("PRINT", ignoreCase = true) -> trimmed.substring(5).trim()
                            trimmed.startsWith("تدوين") -> trimmed.removePrefix("تدوين").trim()
                            trimmed.startsWith("اطبع") -> trimmed.removePrefix("اطبع").trim()
                            else -> trimmed
                        }
                        if (cleanLine.isEmpty()) {
                            errors.add(ParseError(
                                lineNum, trimmed,
                                "Papyrus 𓏛 requires an expression or text to inscribe",
                                "أمر لفافة البردي 𓏛 يتطلب نصاً أو قيمة لتدوينها"
                            ))
                        } else {
                            blocks.add(HorusBlock(type = BlockType.PRINT, params = mapOf("expr" to cleanLine)))
                        }
                    }

                    // Feather of Ma'at 𓋖 (Weighing, Input)
                    trimmed.startsWith("𓋖") || trimmed.startsWith("INPUT", ignoreCase = true) || trimmed.startsWith("وزن") || trimmed.startsWith("إدخال") -> {
                        val cleanLine = when {
                            trimmed.startsWith("𓋖") -> trimmed.removePrefix("𓋖").trim()
                            trimmed.startsWith("INPUT", ignoreCase = true) -> trimmed.substring(5).trim()
                            trimmed.startsWith("وزن") -> trimmed.removePrefix("وزن").trim()
                            trimmed.startsWith("إدخال") -> trimmed.removePrefix("إدخال").trim()
                            else -> trimmed
                        }
                        val intoIdx = if (cleanLine.contains("INTO", ignoreCase = true)) {
                            cleanLine.indexOf("INTO", ignoreCase = true)
                        } else if (cleanLine.contains("في")) {
                            cleanLine.indexOf("في")
                        } else -1

                        if (intoIdx != -1) {
                            val prompt = cleanLine.substring(0, intoIdx).trim().trim('"')
                            val afterInto = if (cleanLine.substring(intoIdx).startsWith("INTO", ignoreCase = true)) 4 else 2
                            val varName = cleanLine.substring(intoIdx + afterInto).trim()
                            blocks.add(HorusBlock(type = BlockType.INPUT, params = mapOf("prompt" to prompt, "var" to varName)))
                        } else {
                            blocks.add(HorusBlock(type = BlockType.INPUT, params = mapOf("prompt" to "أدخل القيمة", "var" to cleanLine.ifEmpty { "المدخل" })))
                        }
                    }

                    // Eye of Horus 𓂀 (Insight, Condition)
                    trimmed.startsWith("𓂀") || trimmed.startsWith("IF", ignoreCase = true) || trimmed.startsWith("إذا") || trimmed.startsWith("فحص") -> {
                        val cleanLine = when {
                            trimmed.startsWith("𓂀") -> trimmed.removePrefix("𓂀").trim()
                            trimmed.startsWith("IF", ignoreCase = true) -> trimmed.substring(2).trim()
                            trimmed.startsWith("إذا") -> trimmed.removePrefix("إذا").trim()
                            trimmed.startsWith("فحص") -> trimmed.removePrefix("فحص").trim()
                            else -> trimmed
                        }
                        val condParts = cleanLine.split(Regex("\\s+"))
                        if (condParts.size >= 3) {
                            val left = condParts[0]
                            val op = condParts[1]
                            val right = condParts.subList(2, condParts.size).joinToString(" ")
                            blocks.add(HorusBlock(type = BlockType.IF, params = mapOf("left" to left, "op" to op, "right" to right)))
                        } else {
                            blocks.add(HorusBlock(type = BlockType.IF, params = mapOf("left" to cleanLine, "op" to "==", "right" to "true")))
                        }
                    }

                    // Else / وإلا
                    trimmed.equals("وإلا", ignoreCase = true) || trimmed.equals("ELSE", ignoreCase = true) -> {
                        blocks.add(HorusBlock(type = BlockType.ELSE))
                    }

                    // End If
                    trimmed.equals("ختام_الشرط", ignoreCase = true) || trimmed.equals("نهاية_الشرط", ignoreCase = true) || trimmed.equals("END_IF", ignoreCase = true) || trimmed.equals("ENDIF", ignoreCase = true) -> {
                        blocks.add(HorusBlock(type = BlockType.END_IF))
                    }

                    // Scarab 𓆣 (Cycles, Loops)
                    trimmed.startsWith("𓆣") || trimmed.startsWith("REPEAT", ignoreCase = true) || trimmed.startsWith("كرر") -> {
                        val cleanLine = when {
                            trimmed.startsWith("𓆣") -> trimmed.removePrefix("𓆣").trim()
                            trimmed.startsWith("REPEAT", ignoreCase = true) -> trimmed.substring(6).trim()
                            trimmed.startsWith("كرر") -> trimmed.removePrefix("كرر").trim()
                            else -> trimmed
                        }
                        val times = cleanLine.ifEmpty { "3" }
                        blocks.add(HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to times)))
                    }

                    // End Repeat
                    trimmed.equals("ختام_التكرار", ignoreCase = true) || trimmed.equals("نهاية_التكرار", ignoreCase = true) || trimmed.equals("END_REPEAT", ignoreCase = true) || trimmed.equals("ENDREPEAT", ignoreCase = true) -> {
                        blocks.add(HorusBlock(type = BlockType.END_REPEAT))
                    }

                    // Pyramidion 𓊗 (Actions, Movement)
                    trimmed.startsWith("𓊗") || trimmed.startsWith("ACTION", ignoreCase = true) || trimmed.startsWith("أمر") || trimmed.startsWith("انطلاق") -> {
                        val cleanLine = when {
                            trimmed.startsWith("𓊗") -> trimmed.removePrefix("𓊗").trim()
                            trimmed.startsWith("ACTION", ignoreCase = true) -> trimmed.substring(6).trim()
                            trimmed.startsWith("أمر") -> trimmed.removePrefix("أمر").trim()
                            trimmed.startsWith("انطلاق") -> trimmed.removePrefix("انطلاق").trim()
                            else -> trimmed
                        }
                        val action = when (cleanLine) {
                            "تقدم", "أمام" -> "MOVE_FORWARD"
                            "يسار", "انعطف_يسار" -> "TURN_LEFT"
                            "يمين", "انعطف_يمين" -> "TURN_RIGHT"
                            "التقط", "التقط_المفتاح" -> "COLLECT_KEY"
                            else -> cleanLine.ifEmpty { "MOVE_FORWARD" }
                        }
                        blocks.add(HorusBlock(type = BlockType.ACTION, params = mapOf("action" to action)))
                    }

                    trimmed in listOf("MOVE_FORWARD", "TURN_LEFT", "TURN_RIGHT", "COLLECT_KEY") -> {
                        blocks.add(HorusBlock(type = BlockType.ACTION, params = mapOf("action" to trimmed)))
                    }

                    else -> {
                        errors.add(ParseError(
                            lineNum,
                            trimmed,
                            "Unknown Horus hieroglyph or instruction '$trimmed'. Check symbol reference.",
                            "رمز أو أمر هيروغليفي غير معروف '$trimmed'. راجع مرجع رموز شفرة حورس."
                        ))
                    }
                }
            } catch (e: Exception) {
                errors.add(ParseError(lineNum, trimmed, e.message ?: "Syntax error", "خطأ في الصياغة: ${e.message}"))
            }
        }

        // Structural validation
        validateNesting(blocks, errors)

        return Pair(blocks, errors)
    }

    private fun validateNesting(blocks: List<HorusBlock>, errors: MutableList<ParseError>) {
        var ifDepth = 0
        var loopDepth = 0

        for ((idx, block) in blocks.withIndex()) {
            when (block.type) {
                BlockType.IF -> ifDepth++
                BlockType.ELSE -> {
                    if (ifDepth == 0) {
                        errors.add(ParseError(idx + 1, "وإلا", "ELSE without a preceding Eye of Horus 𓂀 IF block", "أمر 'وإلا' بدون بداية شرط عين حورس 𓂀 سابقة"))
                    }
                }
                BlockType.END_IF -> {
                    if (ifDepth <= 0) {
                        errors.add(ParseError(idx + 1, "ختام_الشرط", "Unexpected END_IF without matching Eye of Horus 𓂀", "ختام_الشرط إضافي بدون شرط عين حورس 𓂀 مفتوح"))
                    } else {
                        ifDepth--
                    }
                }
                BlockType.REPEAT -> loopDepth++
                BlockType.END_REPEAT -> {
                    if (loopDepth <= 0) {
                        errors.add(ParseError(idx + 1, "ختام_التكرار", "Unexpected END_REPEAT without matching Scarab 𓆣", "ختام_التكرار إضافي بدون حلقة الجعران 𓆣 مفتوحة"))
                    } else {
                        loopDepth--
                    }
                }
                else -> {}
            }
        }

        if (ifDepth > 0) {
            errors.add(ParseError(blocks.size, "Missing END_IF", "Unclosed Eye of Horus 𓂀 condition! Add 'ختام_الشرط'", "شرط عين حورس 𓂀 غير مغلق! أضف 'ختام_الشرط' لإغلاق ميزان القرار"))
        }
        if (loopDepth > 0) {
            errors.add(ParseError(blocks.size, "Missing END_REPEAT", "Unclosed Scarab 𓆣 loop! Add 'ختام_التكرار'", "حلقة الجعران 𓆣 غير مغلقة! أضف 'ختام_التكرار' لإغلاق دورة التكرار"))
        }
    }

    fun blocksToCode(blocks: List<HorusBlock>): String {
        val sb = StringBuilder()
        var indent = 0

        for (block in blocks) {
            if (block.type == BlockType.END_IF || block.type == BlockType.END_REPEAT || block.type == BlockType.ELSE) {
                indent = (indent - 1).coerceAtLeast(0)
            }

            val prefix = "  ".repeat(indent)
            sb.append(prefix).append(block.toCodeString(useHieroglyphs = true)).append("\n")

            if (block.type == BlockType.IF || block.type == BlockType.REPEAT || block.type == BlockType.ELSE) {
                indent++
            }
        }
        return sb.toString().trimEnd()
    }
}
