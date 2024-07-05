package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityExerciseDetailsBinding

class ExerciseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailsBinding
    private lateinit var countDownTimer: CountDownTimer
    private var isTimerRunning = false
    private val timerStartTimeInMillis: Long = 60000 // 1 minute in milliseconds
    private var timeLeftInMillis = timerStartTimeInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge() // Enable edge-to-edge display

        // Retrieve data from intent extras
        val exerciseTitle = intent.getStringExtra("exercise_name")
        val exerciseGifUrl = intent.getStringExtra("exercise_gif_url")
        val exerciseSteps = intent.getStringArrayListExtra("exercise_steps")

        // Populate UI elements with exercise details
        binding.exerciseTitleTextView.text = exerciseTitle
        binding.exerciseStepsTextView.text = formatSteps(exerciseSteps)

        // Load GIF using Glide
        Glide.with(this)
            .asGif()
            .load(exerciseGifUrl)
            .into(binding.exerciseGifImageView)

        // Initialize timer view
        updateTimerUI(timerStartTimeInMillis)

        // Start button click listener
        binding.startButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        // Reset button click listener
        binding.resetButton.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI(timeLeftInMillis)
            }

            override fun onFinish() {
                isTimerRunning = false
                binding.startButton.text = getString(R.string.start)
                updateTimerUI(0)
            }
        }.start()

        isTimerRunning = true
        binding.startButton.text = getString(R.string.pause)
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        binding.startButton.text = getString(R.string.start)
    }

    private fun resetTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        timeLeftInMillis = timerStartTimeInMillis
        updateTimerUI(timerStartTimeInMillis)
        binding.startButton.text = getString(R.string.start)
    }

    private fun updateTimerUI(millisUntilFinished: Long) {
        val seconds = (millisUntilFinished / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, remainingSeconds)
        binding.timer.text = timeLeftFormatted
    }

    private fun formatSteps(steps: ArrayList<String>?): String {
        val stringBuilder = StringBuilder()
        steps?.forEachIndexed { index, step ->
            stringBuilder.append("${index + 1}. $step\n\n")
        }
        return stringBuilder.toString()
    }
}
