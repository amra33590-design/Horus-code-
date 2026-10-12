package com.example.horuscode.engine.model

sealed class HorusValue {
    data class NumberVal(val value: Double) : HorusValue() {
        override fun toString(): String {
            return if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
        }
    }

    data class StringVal(val value: String) : HorusValue() {
        override fun toString(): String = value
    }

    data class BooleanVal(val value: Boolean) : HorusValue() {
        override fun toString(): String = if (value) "TRUE" else "FALSE"
    }

    object NullVal : HorusValue() {
        override fun toString(): String = "NULL"
    }

    fun isTruthy(): Boolean = when (this) {
        is BooleanVal -> value
        is NumberVal -> value != 0.0
        is StringVal -> value.isNotEmpty() && value != "0" && value.lowercase() != "false"
        NullVal -> false
    }

    fun asNumber(): Double = when (this) {
        is NumberVal -> value
        is StringVal -> value.toDoubleOrNull() ?: 0.0
        is BooleanVal -> if (value) 1.0 else 0.0
        NullVal -> 0.0
    }
}
