package com.example.aurafit.model


data class Exercise(
    val name: String,
    val gifUrl: String,
    val steps: List<String>
)

data class ExerciseGroup(
    val title: String,
    val exercises: List<Exercise>
)