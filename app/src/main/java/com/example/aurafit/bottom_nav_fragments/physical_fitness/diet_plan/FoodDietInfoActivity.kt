package com.example.aurafit.bottom_nav_fragments.physical_fitness.diet_plan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityFoodDietInfoBinding
import com.example.aurafit.model.Food
import com.example.aurafit.model.NutritionalInfo

class FoodDietInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDietInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDietInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example food (replace with actual data retrieval logic)
        val food = Food(
            name = "Salmon",
            description = "Salmon is a popular oily fish with many nutritional benefits.",
            nutritionalInfo = NutritionalInfo(
                calories = 206,
                protein = 22.0,
                carbohydrates = 0.0,
                fats = 13.4,
                vitamins = listOf("Vitamin A", "Vitamin D", "Vitamin B12"),
                minerals = listOf("Selenium", "Potassium")
            ),
            healthBenefits = listOf(
                "Rich source of omega-3 fatty acids",
                "Promotes heart health",
                "Supports brain function"
            ),
            imageResourceId = R.drawable.img // Replace with actual image resource ID
        )

        // Populate UI with food information
        binding.foodName.text = food.name
        binding.foodDescription.text = food.description
        binding.caloriesValue.text = food.nutritionalInfo.calories.toString()
        binding.proteinValue.text = "${food.nutritionalInfo.protein} g"
        binding.carbsValue.text = "${food.nutritionalInfo.carbohydrates} g"
        binding.fatsValue.text = "${food.nutritionalInfo.fats} g"
        binding.vitaminsValue.text = food.nutritionalInfo.vitamins.joinToString(", ")
        binding.mineralsValue.text = food.nutritionalInfo.minerals.joinToString(", ")
        binding.healthBenefits.text = food.healthBenefits.joinToString("\n")

        // Load food image
        binding.foodImage.setImageResource(food.imageResourceId)
    }
}
