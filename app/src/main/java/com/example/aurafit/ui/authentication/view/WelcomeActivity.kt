package com.example.aurafit.ui.authentication.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityWelcomeBinding
import com.example.aurafit.ui.authentication.viewmodel.WelcomeViewModel
import com.example.aurafit.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        binding.psfsText.setTypeface(null, Typeface.BOLD_ITALIC)
        binding.psfsText.textSize= 20F

        // Observe changes in ViewModel and update UI
        viewModel.imageId.observe(this) { imageId ->
            binding.appLogo.setImageResource(imageId)
            binding.appLogo.visibility = View.VISIBLE
        }

        viewModel.text.observe(this) { text ->
            binding.psfsText.text = text
            binding.psfsText.visibility = View.VISIBLE
        }

        viewModel.currentIndex.observe(this) { index ->
            if (index >= viewModel.imageIds.size) {
                binding.progressBar.visibility = View.GONE
                binding.actionButton.setImageResource(R.drawable.circle_arrow)
                binding.actionButton.setOnClickListener {
                    startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    finish()
                }
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBar.progress = viewModel.getProgress()
            }
        }

        binding.actionButton.setOnClickListener {
            fadeOutCurrentContent()
        }
    }

    private fun fadeOutCurrentContent() {
        val fadeOut: Animation = AlphaAnimation(1f, 0f).apply { duration = 500 }
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                viewModel.loadNextImageAndText()
                val fadeIn: Animation = AlphaAnimation(0f, 1f).apply { duration = 500 }
                binding.appLogo.startAnimation(fadeIn)
                binding.psfsText.startAnimation(fadeIn)
            }
        })
        binding.appLogo.startAnimation(fadeOut)
        binding.psfsText.startAnimation(fadeOut)
    }
}
