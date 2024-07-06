package com.example.aurafit.model

data class MeditationSession(
    val name: String,
    val description: String,
    val filePath: String // Local file path to the downloaded meditation file
)
