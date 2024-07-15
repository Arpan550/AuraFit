package com.example.aurafit.bottom_nav_fragments.physical_fitness.diet_plan

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityBmidietBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class BMIDietActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmidietBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmidietBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserUid = auth.currentUser!!.uid

        // Set up header and description
        binding.headerTitle.text = getString(R.string.bmi_diet_title)
        binding.headerDescription.text = getString(R.string.bmi_diet_description)

        // Set up BMI category details
        setupBMICategories()

        // Set up calculate BMI button
        binding.calculateBmiButton.setOnClickListener {
            calculateBMI()
        }
    }

    private fun setupBMICategories() {
        val bmiCategories = listOf(
            BMICategory("Underweight", "Diet plans to help you gain weight healthily.", R.color.orange_underweight),
            BMICategory("Normal weight", "Diet plans to maintain a balanced diet.", R.color.green_normalweight),
            BMICategory("Overweight", "Diet plans to help you lose weight healthily.", R.color.yellow_overweight),
            BMICategory("Obese", "Specialized diet plans to significantly reduce weight.", R.color.red_obese)
        )

        for (category in bmiCategories) {
            val view = layoutInflater.inflate(R.layout.item_bmi_category, binding.bmiCategoryContainer, false)
            val categoryTitle = view.findViewById<TextView>(R.id.categoryTitle)
            val categoryDescription = view.findViewById<TextView>(R.id.categoryDescription)

            categoryTitle.text = category.title
            categoryDescription.text = category.description

            // Set background color based on category
            val colorInt = ContextCompat.getColor(this, category.colorRes)
            view.background.setTint(colorInt)

            // Set text color for visibility
            val textColor = if (isColorDark(colorInt)) {
                ContextCompat.getColor(this, android.R.color.white) // Use white text color for dark backgrounds
            } else {
                ContextCompat.getColor(this, android.R.color.black) // Use black text color for light backgrounds
            }
            categoryTitle.setTextColor(textColor)
            categoryDescription.setTextColor(textColor)

            view.setOnClickListener {
                val intent = Intent(this, BMICategoryDetailActivity::class.java).apply {
                    putExtra("CATEGORY_TITLE", category.title)
                    putExtra("CATEGORY_DESCRIPTION", category.description)
                }
                startActivity(intent)
            }

            binding.bmiCategoryContainer.addView(view)
        }
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * android.graphics.Color.red(color) + 0.587 * android.graphics.Color.green(color) + 0.114 * android.graphics.Color.blue(color)) / 255
        return darkness >= 0.5
    }

    private fun calculateBMI() {
        val weight = binding.inputWeight.text.toString().toFloatOrNull()
        val height = binding.inputHeight.text.toString().toFloatOrNull()

        if (weight == null || height == null || height == 0f) {
            Toast.makeText(this, "Please enter valid weight and height", Toast.LENGTH_SHORT).show()
            return
        }

        val bmi = weight / (height * height)
        val (bmiCategory, color) = when {
            bmi < 18.5 -> "Underweight" to ContextCompat.getColor(this, R.color.orange_underweight)
            bmi in 18.5..24.9 -> "Normal weight" to ContextCompat.getColor(this, R.color.green_normalweight)
            bmi in 25.0..29.9 -> "Overweight" to ContextCompat.getColor(this, R.color.yellow_overweight)
            else -> "Obese" to ContextCompat.getColor(this, R.color.red_obese)
        }

        binding.bmiResult.text = String.format("Your BMI: %.2f (%s)", bmi, bmiCategory)
        binding.bmiResult.setTextColor(color)

        // Store BMI details in Firestore
        val timestamp = Calendar.getInstance().time // Use timestamp as per your requirement
        val bmiDetails = mapOf(
            "weight" to weight,
            "height" to height,
            "bmi" to bmi,
            "category" to bmiCategory,
            "timestamp" to timestamp
        )

        val userBmiRef = firestore.collection("bmi_info").document(currentUserUid).collection("bmi_records").document()
        userBmiRef.set(bmiDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "BMI details saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving BMI details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

data class BMICategory(val title: String, val description: String, val colorRes: Int)
