package com.example.aurafit.model

data class Food(
    val name: String,
    val description: String,
    val nutritionalInfo: NutritionalInfo,
    val healthBenefits: List<String>,
    val imageResourceId: Int // Resource ID of the food image
)

data class NutritionalInfo(
    val calories: Int,
    val protein: Double,
    val carbohydrates: Double,
    val fats: Double,
    val vitamins: List<String>,
    val minerals: List<String>
)
