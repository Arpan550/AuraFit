package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.adapters.ViewPagerExerciseAdapter
import com.example.aurafit.databinding.ActivityExerciseDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ExerciseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailsBinding
    private lateinit var exerciseSteps: ArrayList<String>
    private lateinit var userId: String
    private var programDuration: Int = 0
    private var currentDay: Int = 1
    private var exerciseTitle: String? = null
    private var exerciseGifUrl: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore
        val firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Retrieve data from intent extras
        val intent = intent
        exerciseTitle = intent.getStringExtra("exercise_name")
        exerciseGifUrl = intent.getStringExtra("exercise_gif_url")
        exerciseSteps = intent.getStringArrayListExtra("exercise_steps") ?: ArrayList()
        programDuration = intent.getIntExtra("program_duration", 0)
        currentDay = intent.getIntExtra("current_day", 1)
        userId = auth.currentUser?.uid.toString() // Replace with actual user ID

        Log.d("Testing", "$exerciseTitle\n$programDuration\n$currentDay")

        // Populate UI elements with exercise details
        binding.exerciseTitleTextView.text = exerciseTitle

        // Load GIF using Glide
        Glide.with(this)
            .asGif()
            .load(exerciseGifUrl)
            .into(binding.exerciseGifImageView)

        // Initialize ViewPager
        val myAdapter = ViewPagerExerciseAdapter(supportFragmentManager, formatSteps(exerciseSteps))
        binding.viewpager.adapter = myAdapter

        // Setup tab layout
        binding.tab.setupWithViewPager(binding.viewpager)

        // Handle Switch State Change
        binding.markDoneSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show progress dialog
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Saving...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                // Prepare the exercise data to be updated
                val exerciseData = hashMapOf(
                    "exercise_title" to exerciseTitle,
                    "duration" to programDuration, // Example duration, replace with actual value
                    "is_done" to true
                )

                // Firestore references
                val firestore = FirebaseFirestore.getInstance()
                val userProgramsRef = firestore.collection("user_programs")
                val programCollectionRef = userProgramsRef.document(userId)
                    .collection("$programDuration-day_program")
                    .document("exercise")
                    .collection("Day $currentDay") // Assuming currentDay is the day number
                    .document("$exerciseTitle")

                // Update the exercise details in Firestore
                programCollectionRef.set(exerciseData)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Log.d("Firestore", "Exercise data for current day updated successfully.")
                        // Optionally, you can show a success message or navigate somewhere
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Log.e("Firestore", "Error updating exercise data for current day: $e")
                        // Handle error condition, e.g., show an error message
                    }
            }
        }

    }

    private fun formatSteps(steps: ArrayList<String>?): String {
        val stringBuilder = StringBuilder()
        steps?.forEachIndexed { index, step ->
            stringBuilder.append("${index + 1}. $step\n\n")
        }
        return stringBuilder.toString()
    }
}
