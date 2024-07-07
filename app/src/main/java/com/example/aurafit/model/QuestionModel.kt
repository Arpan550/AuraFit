package com.example.aurafit.model

data class QuestionModel(
    val questionText: String,
    val options: List<String>? = null, // Optional for multiple-choice questions
    val severityScale: String? = null,
    val difficultyImpact: String? = null,
    val selectedFrequency: String? = null
)
