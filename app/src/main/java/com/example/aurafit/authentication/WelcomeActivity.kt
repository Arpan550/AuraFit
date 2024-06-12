package com.example.aurafit.authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val imageIds = arrayOf(
        R.drawable.cleanse_your_aura,
//        R.drawable.image2,
//        R.drawable.image3,
//        R.drawable.image4,
//        R.drawable.image5
    )
    private val texts = arrayOf(
        "Text 1",
        "Text 2",
        "Text 3",
        "Text 4",
        "Text 5"
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionButton.setOnClickListener {
            loadImageAndText()
        }
    }

    private fun loadImageAndText() {
        if (currentIndex < imageIds.size) {
            // Show progress bar
            binding.progressBar.visibility = View.VISIBLE

            // Simulate loading delay
            Handler().postDelayed({
                // Load image
                binding.appLogo.setImageResource(imageIds[currentIndex])

                // Load text
                binding.psfsText.text = texts[currentIndex]

                // Increment progress
                val progress = ((currentIndex + 1) * 100) / imageIds.size
                binding.progressBar.progress = progress

                // Update index
                currentIndex++

                // Hide progress bar if all images/texts are loaded
                if (currentIndex >= imageIds.size) {
                    binding.progressBar.visibility = View.GONE
                    binding.actionButton.setImageResource(R.drawable.circle_arrow) // Change button icon to login
                    binding.actionButton.setOnClickListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // Close this activity after opening login activity
                    }
                }
            }, 1000) // Simulate loading delay of 1 second
        }
    }
}
