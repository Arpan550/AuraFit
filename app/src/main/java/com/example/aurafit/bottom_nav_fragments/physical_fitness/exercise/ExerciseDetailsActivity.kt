package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.adapters.ViewPagerExerciseAdapter
import com.example.aurafit.databinding.ActivityExerciseDetailsBinding
import java.util.ArrayList

class ExerciseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailsBinding
    private lateinit var exerciseSteps: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Retrieve data from intent extras
        val intent = intent
        val exerciseTitle = intent.getStringExtra("exercise_name")
        val exerciseGifUrl = intent.getStringExtra("exercise_gif_url")
        exerciseSteps = intent.getStringArrayListExtra("exercise_steps") ?: ArrayList()

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
    }

    private fun formatSteps(steps: ArrayList<String>?): String {
        val stringBuilder = StringBuilder()
        steps?.forEachIndexed { index, step ->
            stringBuilder.append("${index + 1}. $step\n\n")
        }
        return stringBuilder.toString()
    }

    private fun enableEdgeToEdge() {
        // Enable edge-to-edge display if supported
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.apply {
                hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}
