package com.example.horuscode.engine.model

data class TraceStep(
    val stepNumber: Int,
    val blockIndex: Int,
    val instruction: String,
    val explanationEn: String,
    val explanationAr: String,
    val variablesSnapshot: Map<String, String>,
    val outputProduced: String? = null
)
