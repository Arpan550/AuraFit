package com.example.aurafit.bottom_nav_fragments.physical_fitness.diet_plan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.databinding.ActivityDietPlanBinding

class DietPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDietPlanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDietPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bmiDiet.setOnClickListener {
            val intent = Intent(this, BMIDietActivity::class.java)
            startActivity(intent)
        }

        binding.foodDietInfo.setOnClickListener {
            val intent = Intent(this, FoodDietInfoActivity::class.java)
            startActivity(intent)
        }

        binding.dietRecommendation.setOnClickListener {
            val intent = Intent(this, BMIDietActivity::class.java)
            startActivity(intent)
        }
    }
}
