package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aurafit.R
import com.example.aurafit.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private lateinit var countDownTimer: CountDownTimer
    private var isTimerRunning = false
    private val timerStartTimeInMillis: Long = 60000 // 1 minute in milliseconds
    private var timeLeftInMillis = timerStartTimeInMillis

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
            if (isTimerRunning) {
                resetTimer()
            }
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
                // Reset timer automatically after completion
                resetTimer()
            }
        }.start()

        isTimerRunning = true
        binding.startButton.text = getString(R.string.pause)
    }

    private fun pauseTimer() {
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
            isTimerRunning = false
            binding.startButton.text = getString(R.string.start)
        }
    }

    private fun resetTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        timeLeftInMillis = timerStartTimeInMillis
        updateTimerUI(timeLeftInMillis)
        binding.startButton.text = getString(R.string.start)
    }

    private fun updateTimerUI(millisUntilFinished: Long) {
        val seconds = (millisUntilFinished / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, remainingSeconds)
        binding.timer.text = timeLeftFormatted
    }
}
