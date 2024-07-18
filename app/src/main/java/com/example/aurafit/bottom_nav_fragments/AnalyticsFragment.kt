package com.example.aurafit.bottom_nav_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.adapters.QuestionsAdapter
import com.example.aurafit.bottom_nav_fragments.mental_fitness.AssessmentDetailsActivity.QuestionModel
import com.example.aurafit.databinding.FragmentAnalyticsBinding
import com.example.aurafit.model.SelfAssessment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.play.integrity.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class AnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentAnalyticsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var userId: String = ""
    private var programDurations: List<Int> = listOf(0, 30, 60) // Example list of program durations

    // Declare PieChart variable
    private lateinit var pieChart: PieChart

    // List to accumulate chart entries for each program duration
    private var pieEntriesList: MutableList<List<PieEntry>> = mutableListOf()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        userId = auth.currentUser?.uid ?: ""

        sharedPreferences = requireContext().getSharedPreferences("AnalyticsData", Context.MODE_PRIVATE)

        val programDuration = sharedPreferences.getInt("program_duration", 0)
        val currentDay = sharedPreferences.getInt("current_day", 1)

        Log.d("AnalyticsFragment", "Program Duration: $programDuration")
        Log.d("AnalyticsFragment", "Current Day: $currentDay")

        // Fetch and display user details
        fetchUserDetails()

        // Fetch and display latest BMI details
        fetchLatestBMIDetails()

        // Fetch and display exercise progress for each program duration
        programDurations.forEach { duration ->
            fetchExerciseProgress(duration, currentDay)
        }

        // Retrieve and log assessment results from SharedPreferences
        val savedResultsJson = sharedPreferences.getString("assessment_results", null)
        val savedResults = if (savedResultsJson.isNullOrEmpty()) {
            mutableListOf<Map<String, String>>()
        } else {
            try {
                Gson().fromJson(savedResultsJson, object : TypeToken<List<Map<String, String>>>() {}.type)
            } catch (e: JsonSyntaxException) {
                mutableListOf<Map<String, String>>()
            }
        }

        Log.d("AnalyticsFragment", "Assessment Results from SharedPreferences: $savedResults")
    }

    private fun fetchUserDetails() {
        val userRef = firestore.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val email = document.getString("email") ?: ""
                    val name = document.getString("name") ?: ""
                    val photoUrl = document.getString("photoUrl") ?: ""

                    // Update UI with user details
                    binding.userEmailTextView.text = email
                    binding.userNameTextView.text = name

                    // Load user photo using Glide
                    Glide.with(requireContext())
                        .load(photoUrl)
                        .circleCrop()
                        .placeholder(R.drawable.img) // Placeholder image while loading
                        .error(R.drawable.img) // Error image if loading fails
                        .into(binding.userPhotoImageView)
                } else {
                    Log.d("AnalyticsFragment", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("AnalyticsFragment", "get failed with ", exception)
            }
    }

    private fun fetchLatestBMIDetails() {
        val bmiRef =
            firestore.collection("bmi_info").document(userId).collection("bmi_records")

        bmiRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val weight = document.getDouble("weight") ?: 0.0
                    val height = document.getDouble("height") ?: 0.0
                    val bmi = document.getDouble("bmi") ?: 0.0
                    val category = document.getString("category") ?: ""

                    // Update UI with BMI details
                    binding.bmiWeightTextView.text = String.format("%.2f kg", weight)
                    binding.bmiHeightTextView.text = String.format("%.2f m", height)
                    binding.bmiValueTextView.text = String.format("%.2f", bmi)
                    binding.bmiCategoryTextView.text = category
                } else {
                    Log.d("AnalyticsFragment", "No BMI records found")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("AnalyticsFragment", "Error fetching BMI details: ", exception)
            }
    }

    @SuppressLint("MissingInflatedId")
    private fun fetchExerciseProgress(programDuration: Int, currentDay: Int) {
        val exerciseProgressRef = firestore.collection("user_programs").document(userId)
            .collection("${programDuration}-day_program")
            .document("exercise")
            .collection("Day $currentDay")

        exerciseProgressRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val exerciseProgressList = mutableListOf<String>()
                    for (document in querySnapshot.documents) {
                        val exerciseTitle = document.getString("exercise_title") ?: "Unknown"
                        val isDone = document.getBoolean("is_done") ?: false
                        val status = if (isDone) "Completed" else "Not Completed"
                        exerciseProgressList.add("$exerciseTitle: $status")
                    }

                    // Inflate the exercise progress card view
                    val cardView = layoutInflater.inflate(
                        R.layout.item_exercise_progress_card,
                        binding.cardsContainer,
                        false
                    )

                    // Set title based on program duration
                    val titleTextView =
                        cardView.findViewById<TextView>(R.id.exerciseProgressTitleTextView)
                    val pieChartView = cardView.findViewById<PieChart>(R.id.pieChart)

                    // Call setupPieChart with programDuration argument
                    setupPieChart(pieChartView, exerciseProgressList, programDuration)

                    when (programDuration) {
                        0 -> {
                            titleTextView.text = "Mindfulness Mental Health Exercise"
                        }
                        30 -> {
                            titleTextView.text = "Fitness Challenge for $programDuration-Day Program"
                        }
                        60 -> {
                            titleTextView.text = "Strength Journey for $programDuration-Day Program"
                        }
                        else -> {
                            titleTextView.text = "Unknown Program"
                            // Handle if necessary
                        }
                    }

                    // Bind the exercise progress data to the card view
                    val exerciseProgressTextView =
                        cardView.findViewById<TextView>(R.id.exerciseProgressTextView)
                    exerciseProgressTextView.text = exerciseProgressList.joinToString("\n")

                    // Add the card view to the container
                    binding.cardsContainer.addView(cardView)
                } else {
                    Log.d(
                        "AnalyticsFragment",
                        "No exercise progress found for $programDuration-Day Program"
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.d(
                    "AnalyticsFragment",
                    "Error fetching exercise progress for $programDuration-Day Program: ",
                    exception
                )
            }
    }

    private fun setupPieChart(
        pieChart: PieChart,
        exerciseProgressList: List<String>,
        programDuration: Int
    ) {
        val completedCount = exerciseProgressList.count { it.contains("Completed") }
        val totalCount = when (programDuration) {
            0 -> 10
            30 -> 18
            60 -> 18
            else -> 0
        }

        val completedColor = Color.rgb(67, 164, 34)  // Custom color for "Completed"
        val notCompletedColor = Color.rgb(211, 68, 55)  // Custom color for "Not Completed"

        val pieEntries = listOf(
            PieEntry(completedCount.toFloat(), "Completed"),
            PieEntry((totalCount - completedCount).toFloat(), "Not Completed")
        )

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = listOf(completedColor, notCompletedColor)  // Assigning custom colors
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 12f
        pieDataSet.valueFormatter = PercentFormatter(pieChart)

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.legend.isEnabled = false // Disable legend for PieChart
        pieChart.animateY(1000)
        pieChart.invalidate()

        // Set center text based on program duration
        pieChart.centerText = when {
            exerciseProgressList.isEmpty() -> "No Data Available"
            completedCount == 0 -> "No Completed"
            totalCount == completedCount -> "All Completed"
            else -> String.format("%.1f%%", (completedCount.toFloat() / totalCount.toFloat()) * 100)
        }

        // Refresh the chart
        pieChart.notifyDataSetChanged()
    }

}
