package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityExerciseBinding
import com.example.aurafit.ui.drawer.view.ChatSupportActivity

class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_exercise)

        binding.bot.setOnClickListener { view ->
            Snackbar.make(view, "Chat With AuraFit Bot", Snackbar.LENGTH_LONG)
                .setAction("Chat Now") {
                    val intent = Intent(this, ChatSupportActivity::class.java)
                    startActivity(intent)
                }
                .setAnchorView(R.id.bot)
                .show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_exercise)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
