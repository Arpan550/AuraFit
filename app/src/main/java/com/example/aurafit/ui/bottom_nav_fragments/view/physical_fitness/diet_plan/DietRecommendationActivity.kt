package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.diet_plan

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.databinding.ActivityDietRecommendationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class DietRecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDietRecommendationBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var progressDialog: ProgressDialog // Progress dialog declaration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        progressDialog = ProgressDialog(this) // Initialize progress dialog

        // Set click listener for submit button
        binding.btnSubmit.setOnClickListener {
            // Validate inputs (for simplicity, validation logic can be expanded)
            val age = binding.etAge.text.toString().toIntOrNull()
            val weight = binding.etWeight.text.toString().toDoubleOrNull()
            val height = binding.etHeight.text.toString().toDoubleOrNull()
            val bmiLabel = binding.etBmiLabel.text.toString()
            val activityLevel = binding.etActivityLevel.text.toString()
            val healthConditions = binding.etHealthConditions.text.toString()
            val fitnessGoals = binding.etFitnessGoals.text.toString()

            if (age == null || weight == null || height == null ||
                bmiLabel.isBlank() || activityLevel.isBlank() ||
                healthConditions.isBlank() || fitnessGoals.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Calculate BMI if not fetched from Firestore
                val bmi = binding.etBmiLabel.text.toString().toDoubleOrNull() ?: calculateBMI(weight, height)
                val bmiCategory = getBMICategory(bmi)

                // Show progress dialog
                progressDialog.setMessage("Generating diet recommendation...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                // Process the inputs (e.g., suggest diet)
                processInputs(age, weight, height, bmiLabel, bmiCategory, activityLevel, healthConditions, fitnessGoals)
            }
        }

        // Set click listener for check BMI button
        binding.btnCheckBmi.setOnClickListener {
            fetchLatestBMIDetails()
        }
    }

    private fun processInputs(
        age: Int,
        weight: Double,
        height: Double,
        bmiLabel: String,
        bmiCategory: String,
        activityLevel: String,
        healthConditions: String,
        fitnessGoals: String
    ) {
        // Example: Generate diet recommendation based on inputs
        val dietRecommendation = generateDietRecommendation(age, bmiLabel.toDouble(), activityLevel, healthConditions, fitnessGoals)

        // Example: Use Gemini API to get additional information based on user inputs
        getGeminiApiResponse(dietRecommendation)
    }

    private fun generateDietRecommendation(
        age: Int,
        bmi: Double,
        activityLevel: String,
        healthConditions: String,
        fitnessGoals: String
    ): String {
        // Example logic to generate diet recommendation based on inputs
        val recommendation = "Diet recommendation based on:\n" +
                "Age: $age\n" +
                "BMI: $bmi\n" +
                "Activity Level: $activityLevel\n" +
                "Health Conditions: $healthConditions\n" +
                "Fitness Goals: $fitnessGoals"

        return recommendation
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        return weight / (height * height / 10000)  // BMI formula with height in cm
    }

    private fun getBMICategory(bmi: Double): String {
        // Example logic to determine BMI category based on BMI value
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 24.9 -> "Normal weight"
            bmi < 29.9 -> "Overweight"
            else -> "Obese"
        }
    }

    private fun fetchLatestBMIDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val bmiRef = firestore.collection("bmi_info").document(userId).collection("bmi_records")

            bmiRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents.first()
                        val weight = document.getDouble("weight") ?: 0.0
                        val height = document.getDouble("height") ?: 0.0
                        val bmi = document.getDouble("bmi") ?: 0.0

                        // Update UI with fetched BMI details
                        binding.etWeight.setText(String.format("%.2f", weight))
                        binding.etHeight.setText(String.format("%.2f", height))
                        binding.etBmiLabel.setText(String.format("%.2f", bmi))
                    } else {
                        Log.d("DietRecommendationActivity", "No BMI records found")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DietRecommendationActivity", "Error fetching BMI details: ", exception)
                }
        } else {
            Log.d("DietRecommendationActivity", "User not authenticated.")
        }
    }

    private fun getGeminiApiResponse(userInput: String) {
        val apiKey = readApiKey(this)

        if (apiKey != null) {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = apiKey
            )

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // Constructing prompt for Gemini API
                    val prompt = "Give me diet recommendations tips..i konw you are not dietician..but some tips based on...\nMy details are:\n$userInput"

                    val response = generativeModel.generateContent(prompt)

                    // Update UI with Gemini API response
                    runOnUiThread {
                        binding.textViewRecommendations.text = response.text.toString()
                        binding.textViewRecommendations.visibility = View.VISIBLE
                        binding.recommendationHeaderTV.visibility=View.VISIBLE

                        // Dismiss progress dialog when response is received
                        progressDialog.dismiss()
                    }
                } catch (e: Exception) {
                    Log.e("DietRecommendationActivity", "Error fetching Gemini API response", e)
                    runOnUiThread {
                        Toast.makeText(this@DietRecommendationActivity, "Failed to fetch Gemini API response", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss() // Dismiss progress dialog on failure
                    }
                }
            }
        } else {
            Log.e("DietRecommendationActivity", "API key not found")
            Toast.makeText(this, "API key not found", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss() // Dismiss progress dialog on API key not found
        }
    }

    private fun readApiKey(context: Context): String? {
        var apiKey: String? = null
        try {
            val inputStream = context.assets.open("config.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val json = String(buffer, Charsets.UTF_8)
            val jsonObject = JSONObject(json)

            apiKey = jsonObject.getString("apiKey")
        } catch (e: IOException) {
            Log.e("DietRecommendationActivity", "Error reading API key file", e)
        } catch (e: Exception) {
            Log.e("DietRecommendationActivity", "Error parsing JSON", e)
        }

        return apiKey
    }

}
