package com.example.aurafit.ui.bottom_nav_fragments.view.mental_fitness

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.R
import com.example.aurafit.adapters.MeditationAdapter
import com.example.aurafit.model.Meditation

class MeditationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation)

        // List of meditations with audio filenames
        val sessions = listOf(
            Meditation("Morning Meditation", "Start your day with positive energy.", "morning_meditation.wav"),
            Meditation("Evening Relaxation", "Wind down and relax before sleep.", "evening_meditation.wav"),
            Meditation("Guided Meditation", "Involves a guide leading through imagery or exercises.", "guided_meditation.wav"),
            Meditation("Transcendental Meditation", "Involves repeating a mantra to achieve relaxed awareness.", "transcendental_meditation.wav"),
            Meditation("Mindfulness Meditation", "Focuses on being present in the moment.", "mindfulness_meditation.wav"),
            Meditation("Body Scan Meditation", "Focuses on progressively relaxing different parts of the body.", "body_scan_meditation.wav"),
            Meditation("Loving-Kindness Meditation", "Involves developing feelings of goodwill and compassion towards oneself and others.", "loving_kindness_meditation.wav"),
            Meditation("Breath Awareness Meditation", "Focuses on observing the breath to achieve relaxation and mindfulness.", "breath_awareness_meditation.wav"),
            Meditation("Visualization Meditation", "Involves creating mental images to promote relaxation and healing.", "visualization_meditation.wav"),
            Meditation("Mantra Meditation", "Involves repeating a word or phrase to calm the mind and enhance concentration.", "mantra_meditation.wav")
        )

        // Set up the ListView with MeditationAdapter
        val adapter = MeditationAdapter(this, sessions)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
    }
}
