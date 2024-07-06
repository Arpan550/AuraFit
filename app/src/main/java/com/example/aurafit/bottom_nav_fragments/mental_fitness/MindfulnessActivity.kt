package com.example.aurafit.bottom_nav_fragments.mental_fitness

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.adapters.MindfulnessExerciseAdapter
import com.example.aurafit.model.MindfulnessExercise
import com.example.aurafit.R
import com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise.ExerciseDetailsActivity

class MindfulnessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mindfulness)

        // Create a list of mindfulness exercises with steps, GIF URLs, and descriptions
        val exercises = listOf(
            MindfulnessExercise(
                "Deep Breathing Exercise",
                "android.resource://${packageName}/${R.raw.deep_breathing}",
                listOf(
                    "Sit or lie down comfortably.",
                    "Close your eyes and relax your body.",
                    "Inhale deeply through your nose..."
                ),
                "A simple yet effective exercise to help you relax and focus on your breathing."
            ),
            MindfulnessExercise(
                "Body Scan Meditation",
                "android.resource://${packageName}/${R.raw.body_scanning}",
                listOf(
                    "Lie down in a comfortable position.",
                    "Start with your toes and gradually move upward..."
                ),
                "A guided meditation that helps you become more aware of your body and sensations."
            ),
            // Add more exercises similarly
            MindfulnessExercise(
                "Visualization Exercise",
                "android.resource://${packageName}/${R.raw.visualization}",
                listOf(
                    "Find a quiet place to sit comfortably.",
                    "Close your eyes and imagine a peaceful scene..."
                ),
                "Use the power of your imagination to create a calm and relaxing mental image."
            ),
            MindfulnessExercise(
                "Mindful Walking",
                "android.resource://${packageName}/${R.raw.mindfulness}",
                listOf(
                    "Choose a quiet path for walking.",
                    "Start walking slowly, focusing on each step..."
                ),
                "Combine the benefits of walking with mindfulness to enhance your awareness."
            ),
            MindfulnessExercise(
                "Gratitude Practice",
                "android.resource://${packageName}/${R.raw.gratitude}",
                listOf(
                    "Sit quietly and think about things you're grateful for..."
                ),
                "Cultivate a sense of gratitude by reflecting on positive aspects of your life."
            ),
            MindfulnessExercise(
                "Progressive Muscle Relaxation",
                "android.resource://${packageName}/${R.raw.progressive_muscle_relaxation}",
                listOf(
                    "Sit or lie down comfortably.",
                    "Tense a group of muscles (e.g., fists, arms) for 5-10 seconds..."
                ),
                "Relieve tension by sequentially tensing and relaxing different muscle groups."
            ),
            MindfulnessExercise(
                "Focused Attention Meditation",
                "android.resource://${packageName}/${R.raw.focused_attention_meditation}",
                listOf(
                    "Find a quiet place to sit comfortably.",
                    "Choose a focal point (e.g., breath, object)..."
                ),
                "Improve concentration and mental clarity by focusing on a single point."
            ),
            MindfulnessExercise(
                "Loving-Kindness Meditation",
                "android.resource://${packageName}/${R.raw.love}",
                listOf(
                    "Sit quietly and close your eyes.",
                    "Repeat phrases of kindness towards yourself..."
                ),
                "Foster positive emotions and compassion through guided phrases of kindness."
            ),
            MindfulnessExercise(
                "Nature Sounds Meditation",
                "android.resource://${packageName}/${R.raw.nature}",
                listOf(
                    "Find a peaceful outdoor or quiet indoor space.",
                    "Close your eyes and focus on natural sounds..."
                ),
                "Enhance relaxation by immersing yourself in the soothing sounds of nature."
            ),
            MindfulnessExercise(
                "Mindful Eating Exercise",
                "android.resource://${packageName}/${R.raw.mindfulness}",
                listOf(
                    "Choose a meal or snack to eat mindfully.",
                    "Sit down without distractions..."
                ),
                "Bring mindfulness to your eating experience by savoring each bite."
            )
        )

        // Create an adapter
        val adapter = MindfulnessExerciseAdapter(this, exercises)

        // Get the ListView and set the adapter
        val exerciseListView = findViewById<ListView>(R.id.exerciseListView)
        exerciseListView.adapter = adapter

        // Set item click listener to open details activity
        exerciseListView.setOnItemClickListener { parent, view, position, id ->
            val exercise = exercises[position]
            val intent = Intent(this, ExerciseDetailsActivity::class.java).apply {
                putExtra("exercise_name", exercise.title)
                putExtra("exercise_steps", ArrayList(exercise.steps))
            }
            startActivity(intent)
        }
    }
}
