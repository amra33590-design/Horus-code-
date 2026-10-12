package com.example.horuscode.engine.parser

import com.example.horuscode.engine.model.HorusValue

object ExpressionEvaluator {

    fun evaluate(expression: String, variables: Map<String, HorusValue>): HorusValue {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return HorusValue.NullVal

        // String literal enclosed in double quotes: "Hello"
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length >= 2) {
            val content = trimmed.substring(1, trimmed.length - 1)
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
            return HorusValue.StringVal(content)
        }

        // Boolean literals
        if (trimmed.equals("true", ignoreCase = true)) return HorusValue.BooleanVal(true)
        if (trimmed.equals("false", ignoreCase = true)) return HorusValue.BooleanVal(false)

        // Comparison operators: ==, !=, <=, >=, <, >
        val comparisonOps = listOf("==", "!=", "<=", ">=", "<", ">")
        for (op in comparisonOps) {
            val idx = findOperatorIndex(trimmed, op)
            if (idx != -1) {
                val leftStr = trimmed.substring(0, idx).trim()
                val rightStr = trimmed.substring(idx + op.length).trim()
                val leftVal = evaluate(leftStr, variables)
                val rightVal = evaluate(rightStr, variables)
                val result = compareValues(leftVal, op, rightVal)
                return HorusValue.BooleanVal(result)
            }
        }

        // Plus / Minus (+ and - with precedence, avoiding minus inside scientific notation or leading negative sign)
        val additiveIdx = findAdditiveOperatorIndex(trimmed)
        if (additiveIdx != -1) {
            val opChar = trimmed[additiveIdx]
            val leftStr = trimmed.substring(0, additiveIdx).trim()
            val rightStr = trimmed.substring(additiveIdx + 1).trim()
            val leftVal = evaluate(leftStr, variables)
            val rightVal = evaluate(rightStr, variables)

            if (opChar == '+') {
                if (leftVal is HorusValue.StringVal || rightVal is HorusValue.StringVal) {
                    return HorusValue.StringVal(leftVal.toString() + rightVal.toString())
                }
                return HorusValue.NumberVal(leftVal.asNumber() + rightVal.asNumber())
            } else {
                return HorusValue.NumberVal(leftVal.asNumber() - rightVal.asNumber())
            }
        }

        // Multiplication / Division / Modulo (*, /, %)
        val multIdx = findMultiplicativeOperatorIndex(trimmed)
        if (multIdx != -1) {
            val opChar = trimmed[multIdx]
            val leftStr = trimmed.substring(0, multIdx).trim()
            val rightStr = trimmed.substring(multIdx + 1).trim()
            val leftVal = evaluate(leftStr, variables)
            val rightVal = evaluate(rightStr, variables)

            return when (opChar) {
                '*' -> HorusValue.NumberVal(leftVal.asNumber() * rightVal.asNumber())
                '/' -> {
                    val r = rightVal.asNumber()
                    if (r == 0.0) throw ArithmeticException("Division by zero in Horus Code")
                    HorusValue.NumberVal(leftVal.asNumber() / r)
                }
                '%' -> {
                    val r = rightVal.asNumber()
                    if (r == 0.0) throw ArithmeticException("Modulo by zero in Horus Code")
                    HorusValue.NumberVal(leftVal.asNumber() % r)
                }
                else -> HorusValue.NullVal
            }
        }

        // Numeric literal
        trimmed.toDoubleOrNull()?.let {
            return HorusValue.NumberVal(it)
        }

        // Variable lookup
        if (variables.containsKey(trimmed)) {
            return variables[trimmed] ?: HorusValue.NullVal
        }

        // If variable is not defined, check if it's a valid identifier and throw clear educational error
        if (isValidIdentifier(trimmed)) {
            throw IllegalArgumentException("Variable '$trimmed' is not defined. Use SET 𓋹 first.")
        }

        // Default as string value if unquoted text without spaces
        return HorusValue.StringVal(trimmed)
    }

    private fun compareValues(left: HorusValue, op: String, right: HorusValue): Boolean {
        if (left is HorusValue.StringVal && right is HorusValue.StringVal) {
            val cmp = left.value.compareTo(right.value)
            return when (op) {
                "==" -> left.value == right.value
                "!=" -> left.value != right.value
                "<" -> cmp < 0
                "<=" -> cmp <= 0
                ">" -> cmp > 0
                ">=" -> cmp >= 0
                else -> false
            }
        }

        val l = left.asNumber()
        val r = right.asNumber()
        return when (op) {
            "==" -> l == r
            "!=" -> l != r
            "<" -> l < r
            "<=" -> l <= r
            ">" -> l > r
            ">=" -> l >= r
            else -> false
        }
    }

    private fun findOperatorIndex(text: String, op: String): Int {
        var inQuotes = false
        var i = 0
        while (i <= text.length - op.length) {
            val ch = text[i]
            if (ch == '"' && (i == 0 || text[i - 1] != '\\')) {
                inQuotes = !inQuotes
            }
            if (!inQuotes && text.regionMatches(i, op, 0, op.length)) {
                // Ensure not part of longer operator
                val isEq = op == "="
                val prevChar = if (i > 0) text[i - 1] else ' '
                val nextChar = if (i + op.length < text.length) text[i + op.length] else ' '
                if (isEq && (prevChar == '!' || prevChar == '<' || prevChar == '>' || prevChar == '=' || nextChar == '=')) {
                    i++
                    continue
                }
                return i
            }
            i++
        }
        return -1
    }

    private fun findAdditiveOperatorIndex(text: String): Int {
        var inQuotes = false
        // Search right-to-left for lowest precedence associativity
        for (i in text.length - 1 downTo 1) {
            val ch = text[i]
            if (ch == '"' && (i == 0 || text[i - 1] != '\\')) {
                inQuotes = !inQuotes
            }
            if (!inQuotes && (ch == '+' || ch == '-')) {
                // Ensure it's not a unary minus right after an operator or space
                val prev = text.substring(0, i).trim()
                if (prev.isNotEmpty() && !prev.endsWith("+") && !prev.endsWith("-") && !prev.endsWith("*") && !prev.endsWith("/")) {
                    return i
                }
            }
        }
        return -1
    }

    private fun findMultiplicativeOperatorIndex(text: String): Int {
        var inQuotes = false
        for (i in text.length - 1 downTo 1) {
            val ch = text[i]
            if (ch == '"' && (i == 0 || text[i - 1] != '\\')) {
                inQuotes = !inQuotes
            }
            if (!inQuotes && (ch == '*' || ch == '/' || ch == '%')) {
                return i
            }
        }
        return -1
    }

    fun isValidIdentifier(name: String): Boolean {
        if (name.isEmpty()) return false
        val first = name[0]
        if (!first.isLetter() && first != '_') return false
        return name.all { it.isLetterOrDigit() || it == '_' }
    }
}
