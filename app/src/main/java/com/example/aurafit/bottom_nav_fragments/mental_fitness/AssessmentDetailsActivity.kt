package com.example.aurafit.bottom_nav_fragments.mental_fitness

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.QuestionsAdapter
import com.example.aurafit.databinding.ActivityAssessmentDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssessmentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssessmentDetailsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var assessmentName: String
    private val questionsAdapter = QuestionsAdapter()
    private lateinit var progressDialog: ProgressDialog

    // Shared Preferences
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "AssessmentResults"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Submitting answers...")
        progressDialog.setCancelable(false)

        // Retrieve assessment name and details from intent
        assessmentName = intent.getStringExtra("ASSESSMENT_NAME") ?: ""
        val assessmentDetails = intent.getStringExtra("ASSESSMENT_DETAILS") ?: ""

        supportActionBar?.title = assessmentName
        binding.assessmentDetailsTextView.text = assessmentDetails

        // Set up RecyclerView for questions
        binding.questionsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.questionsRecyclerView.adapter = questionsAdapter

        // Fetch questions from Firestore
        fetchQuestionsFromFirestore(assessmentName)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Submit button click listener
        binding.submitButton.setOnClickListener {
            // Check if any answer is blank
            if (isAnyAnswerBlank()) {
                showBlankAnswerAlert()
                return@setOnClickListener
            }

            // Collect user answers
            val userAnswers = collectUserAnswers()

            // Store user answers in Firestore
            storeUserAnswers(userAnswers)

            // Determine label based on collective answers
            val label = determineOverallLabel(userAnswers)

            // Save assessment result in SharedPreferences
            saveAssessmentResult(assessmentName, label)

            // Show label dialog
            showLabelDialog(label)
        }
    }

    private fun fetchQuestionsFromFirestore(assessmentName: String) {
        db.collection("SelfAssessmentQuestions")
            .document(assessmentName)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val questions = document.data?.mapNotNull { (key, value) ->
                        val questionMap = value as? Map<*, *> ?: return@mapNotNull null
                        val questionText = questionMap["questionText"] as? String ?: return@mapNotNull null
                        val options = questionMap["options"] as? List<*> ?: return@mapNotNull null
                        QuestionModel(key, questionText, options.map { it.toString() })
                    }
                    questions?.let { questionsAdapter.submitList(it) }
                } else {
                    // Handle case where document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    private fun collectUserAnswers(): List<UserAnswer> {
        val userAnswers = mutableListOf<UserAnswer>()

        // Collect user answers from the adapter
        questionsAdapter.currentList.forEachIndexed { index, question ->
            val selectedOption = question.options.getOrNull(question.selectedOptionIndex ?: -1) ?: ""
            userAnswers.add(UserAnswer(question.id, selectedOption))
        }

        return userAnswers
    }

    private fun storeUserAnswers(userAnswers: List<UserAnswer>) {
        progressDialog.show()

        val batch = db.batch()

        // Process each user answer
        userAnswers.forEach { answer ->
            // Format timestamp as yyyy-MM-dd HH:mm:ss
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            // Create a map for this specific answer with timestamp as key
            val answerData = mapOf(
                "answer" to answer.selectedOption,
                "timestamp" to timestamp
            )

            // Add this answer to Firestore under 'Answers/questionId/date-time'
            val userAnswerRef = db.collection("UserResponses")
                .document(assessmentName)
                .collection("Answers")
                .document(answer.questionId)
                .collection("timestamps")
                .document(timestamp)

            batch.set(userAnswerRef, answerData)
        }

        batch.commit()
            .addOnSuccessListener {
                progressDialog.dismiss()
                navigateToNextActivity()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                // Handle errors
            }
    }

    private fun isAnyAnswerBlank(): Boolean {
        var isBlank = false

        questionsAdapter.currentList.forEach { question ->
            if (question.selectedOptionIndex == null) {
                isBlank = true
                return@forEach
            }
        }

        return isBlank
    }

    private fun showBlankAnswerAlert() {
        AlertDialog.Builder(this)
            .setTitle("Missing Answers")
            .setMessage("Please answer all questions before submitting.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun navigateToNextActivity() {
        // Replace PlaceholderActivity::class.java with your next activity class
        val intent = Intent(this, SelfAssessmentActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun determineOverallLabel(userAnswers: List<UserAnswer>): String {
        // Count occurrences of each answer option
        val answerCounts = mutableMapOf<String, Int>()

        userAnswers.forEach { answer ->
            val currentCount = answerCounts[answer.selectedOption] ?: 0
            answerCounts[answer.selectedOption] = currentCount + 1
        }

        // Determine the label based on the most frequent answer
        val maxCount = answerCounts.maxByOrNull { it.value }?.value ?: 0
        val mostFrequentAnswers = answerCounts.filterValues { it == maxCount }.keys.toList()

        // Example labels based on max count of most frequent answers
        return when {
            mostFrequentAnswers.contains("Never") -> "Low"
            mostFrequentAnswers.contains("Occasionally") -> "Moderate"
            mostFrequentAnswers.contains("Often") -> "High"
            mostFrequentAnswers.contains("Constantly") -> "Very High"
            else -> {
                // If none of the predefined answers match, return "Unknown" or handle differently
                // You can customize this part based on your specific assessment criteria
                "Unknown"
            }
        }
    }

    private fun saveAssessmentResult(assessmentName: String, label: String) {
        // Retrieve current saved results from SharedPreferences
        val savedResultsJson = sharedPreferences.getString("assessment_results", null)
        val savedResults = if (savedResultsJson.isNullOrEmpty()) {
            mutableListOf<Map<String, String>>()
        } else {
            try {
                Gson().fromJson(savedResultsJson, object : TypeToken<List<Map<String, String>>>() {}.type)
            } catch (e: JsonSyntaxException) {
                Log.e("AnalyticsFragment", "Error parsing JSON", e)
                mutableListOf<Map<String, String>>()
            }
        }

        // Create a new map for this assessment result
        val assessmentResultMap = mapOf("assessmentName" to assessmentName, "label" to label)

        // Add this assessment result map to the list
        savedResults.add(assessmentResultMap)

        // Convert the list to JSON string and save it back to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("assessment_results", Gson().toJson(savedResults))
        editor.apply()
    }

    private fun showLabelDialog(label: String) {
        // Use a Handler to post the dialog show action to the main thread
        Handler(Looper.getMainLooper()).post {
            if (!isFinishing && !isDestroyed) {
                AlertDialog.Builder(this)
                    .setTitle("Assessment Label")
                    .setMessage("Your overall assessment label: $label")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }


    data class QuestionModel(val id: String, val questionText: String, val options: List<String>) {
        var selectedOptionIndex: Int? = null
    }

    data class UserAnswer(val questionId: String, val selectedOption: String)
}
