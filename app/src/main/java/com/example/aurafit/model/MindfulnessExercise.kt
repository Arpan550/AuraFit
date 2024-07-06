package com.example.aurafit.model

import java.io.Serializable

data class MindfulnessExercise(
    val title: String,
    val gifUrl: String,
    val steps: List<String>,
    val description: String
) : Serializable
