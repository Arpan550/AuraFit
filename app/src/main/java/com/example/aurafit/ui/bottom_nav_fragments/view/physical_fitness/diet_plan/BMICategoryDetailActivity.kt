package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.diet_plan

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityBmicategoryDetailBinding

class BMICategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmicategoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmicategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get category title from intent
        val categoryTitle = intent.getStringExtra("CATEGORY_TITLE")

        // Set category title and description
        categoryTitle?.let {
            val categoryInfo = getBMIInfo(it)
            binding.categoryTitle.text = categoryInfo.title
            binding.categoryDescription.text = categoryInfo.description
            binding.root.setBackgroundColor(ContextCompat.getColor(this, categoryInfo.backgroundColor))
            binding.categoryTitle.setTextColor(ContextCompat.getColor(this, categoryInfo.textColor))
            binding.categoryDescription.setTextColor(ContextCompat.getColor(this, categoryInfo.textColor))

            // Populate diet plan details
            val dietPlan = getDietPlanForCategory(it)
            binding.breakfastDescription.text = dietPlan.breakfast
            binding.midMorningSnackDescription.text = dietPlan.midMorningSnack
            binding.lunchDescription.text = dietPlan.lunch
            binding.afternoonSnackDescription.text = dietPlan.afternoonSnack
            binding.dinnerDescription.text = dietPlan.dinner
            binding.eveningSnackDescription.text = dietPlan.eveningSnack
            binding.bedtimeSnackDescription.text = dietPlan.bedtimeSnack
        }
    }

    private fun getBMIInfo(category: String): BMIInfo {
        return when (category) {
            "Underweight" -> BMIInfo(
                title = "Underweight",
                description = "BMI less than 18.5",
                backgroundColor = R.color.orange_underweight,
                textColor = R.color.black
            )
            "Normal weight" -> BMIInfo(
                title = "Normal weight",
                description = "BMI 18.5–24.9",
                backgroundColor = R.color.green_normalweight,
                textColor = R.color.black
            )
            "Overweight" -> BMIInfo(
                title = "Overweight",
                description = "BMI 25–29.9",
                backgroundColor = R.color.yellow_overweight,
                textColor = R.color.black
            )
            "Obese" -> BMIInfo(
                title = "Obese",
                description = "BMI 30 or greater",
                backgroundColor = R.color.red_obese,
                textColor = R.color.white
            )
            else -> BMIInfo(
                title = "Unknown",
                description = "",
                backgroundColor = android.R.color.white,
                textColor = android.R.color.black
            )
        }
    }

    private fun getDietPlanForCategory(category: String): DietPlan {
        return when (category) {
            "Underweight" -> DietPlan(
                breakfast = "Oatmeal with milk, banana, and nuts",
                midMorningSnack = "Fruit smoothie with protein powder",
                lunch = "Grilled chicken sandwich with avocado",
                afternoonSnack = "Greek yogurt with honey and almonds",
                dinner = "Salmon with quinoa and vegetables",
                eveningSnack = "Cottage cheese with pineapple",
                bedtimeSnack = "Almond milk with a handful of almonds"
            )
            "Normal weight" -> DietPlan(
                breakfast = "Whole grain toast with peanut butter and fruit",
                midMorningSnack = "Apple slices with almond butter",
                lunch = "Chicken salad with mixed greens and vinaigrette",
                afternoonSnack = "Carrot sticks with hummus",
                dinner = "Stir-fried tofu with vegetables and brown rice",
                eveningSnack = "Greek yogurt with berries",
                bedtimeSnack = "Herbal tea with a small piece of dark chocolate"
            )
            "Overweight" -> DietPlan(
                breakfast = "Smoothie with spinach, berries, and protein powder",
                midMorningSnack = "Mixed nuts and dried fruits",
                lunch = "Turkey wrap with lettuce and tomato",
                afternoonSnack = "Vegetable sticks with guacamole",
                dinner = "Grilled chicken with steamed vegetables",
                eveningSnack = "Low-fat cheese with whole grain crackers",
                bedtimeSnack = "Warm milk with turmeric"
            )
            "Obese" -> DietPlan(
                breakfast = "Avocado toast with poached eggs",
                midMorningSnack = "Chia seed pudding with berries",
                lunch = "Quinoa salad with chickpeas and olive oil dressing",
                afternoonSnack = "Whole grain crackers with tuna",
                dinner = "Baked fish with sweet potato and broccoli",
                eveningSnack = "Protein shake with almond milk",
                bedtimeSnack = "Warm herbal tea"
            )
            else -> DietPlan(
                breakfast = "",
                midMorningSnack = "",
                lunch = "",
                afternoonSnack = "",
                dinner = "",
                eveningSnack = "",
                bedtimeSnack = ""
            )
        }
    }

    data class DietPlan(
        val breakfast: String,
        val midMorningSnack: String,
        val lunch: String,
        val afternoonSnack: String,
        val dinner: String,
        val eveningSnack: String,
        val bedtimeSnack: String
    )

    data class BMIInfo(
        val title: String,
        val description: String,
        val backgroundColor: Int,
        val textColor: Int
    )
}
