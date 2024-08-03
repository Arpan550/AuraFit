package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise

import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aurafit.R
import com.example.aurafit.databinding.FragmentTimerBinding
import com.example.aurafit.ui.customviews.ProgressCircle

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var vibrator: Vibrator
    private var soundPool: SoundPool? = null
    private var soundId: Int = 0
    private var isTimerRunning = false
    private val timerStartTimeInMillis: Long = 60000 // Fixed 1 minute
    private var timeLeftInMillis = timerStartTimeInMillis
    private var initialBorderColor: Int = Color.GREEN // Default initial color

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        updateTimerUI(timeLeftInMillis)
        setupCustomViews()

        // Initialize vibrator
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Initialize SoundPool for sound feedback
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundId = soundPool?.load(requireContext(), R.raw.clock_ticking, 1) ?: 0

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
        // Store the initial border color before changing
        initialBorderColor = (binding.progressCircle as ProgressCircle).getCurrentBorderColor()

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI(timeLeftInMillis)
                playTickSound()
            }

            override fun onFinish() {
                isTimerRunning = false
                binding.startButton.text = getString(R.string.start)
                updateTimerUI(0)
                vibratePhone()
                showAlert()
                resetTimer()
            }
        }.start()

        isTimerRunning = true
        binding.startButton.text = getString(R.string.pause)
        // Change the border color to deep blue during the countdown
        val deepBlueColor = ContextCompat.getColor(requireContext(), R.color.deepblue)
        (binding.progressCircle as ProgressCircle).setBorderColor(deepBlueColor)

        // Start the tick-tick sound
        playTickSound()
    }

    private fun pauseTimer() {
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
            isTimerRunning = false
            binding.startButton.text = getString(R.string.start)
        }
        // Pause the sound
        soundPool?.autoPause()
    }

    private fun resetTimer() {
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        isTimerRunning = false
        timeLeftInMillis = timerStartTimeInMillis
        updateTimerUI(timeLeftInMillis)
        binding.startButton.text = getString(R.string.start)
        // Reset the sound
        soundPool?.autoPause()
        soundPool?.unload(soundId)
        soundId = soundPool?.load(requireContext(), R.raw.clock_ticking, 1) ?: 0
        // Revert to the initial border color
        (binding.progressCircle as ProgressCircle).setBorderColor(initialBorderColor)
    }

    private fun updateTimerUI(millisUntilFinished: Long) {
        val seconds = (millisUntilFinished / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, remainingSeconds)
        binding.timer.text = timeLeftFormatted

        val progressPercentage = (millisUntilFinished.toFloat() / timerStartTimeInMillis.toFloat() * 100).toInt()
        val borderColor = when {
            millisUntilFinished > timerStartTimeInMillis / 2 -> R.color.black
            else -> R.color.deepblue
        }
        binding.progressCircle.setProgress(progressPercentage, ContextCompat.getColor(requireContext(), borderColor))
    }

    private fun playTickSound() {
        soundPool?.play(soundId, 1f, 1f, 0, -1, 1f) // Loop indefinitely
    }

    private fun vibratePhone() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500) // Vibrate for 500 milliseconds
        }
    }

    private fun showAlert() {
        // Show an alert or notification here
    }

    private fun setupCustomViews() {
        // Custom view setup if necessary
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool?.release()
    }
}
