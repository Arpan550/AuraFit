package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.diet_plan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.databinding.ActivityDietPlanBinding

class DietPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDietPlanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBmiDiet.setOnClickListener {
            val intent = Intent(this, BMIDietActivity::class.java)
            startActivity(intent)
        }

        binding.btnFoodDietInfo.setOnClickListener {
            val intent = Intent(this, FoodDietInfoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDietRecommendation.setOnClickListener {
            val intent = Intent(this, DietRecommendationActivity::class.java)
            startActivity(intent)
        }
    }
}
