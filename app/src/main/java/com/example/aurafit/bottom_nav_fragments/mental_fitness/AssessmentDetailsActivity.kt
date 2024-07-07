package com.example.aurafit.bottom_nav_fragments.mental_fitness

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.QuestionsAdapter
import com.example.aurafit.databinding.ActivityAssessmentDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssessmentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssessmentDetailsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var assessmentName: String
    private val questionsAdapter = QuestionsAdapter()
    private lateinit var progressDialog: ProgressDialog

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

    data class QuestionModel(val id: String, val questionText: String, val options: List<String>) {
        var selectedOptionIndex: Int? = null
    }

    data class UserAnswer(val questionId: String, val selectedOption: String)
}
