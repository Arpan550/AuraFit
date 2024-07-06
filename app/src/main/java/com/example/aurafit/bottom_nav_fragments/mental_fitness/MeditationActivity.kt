package com.example.aurafit.bottom_nav_fragments.mental_fitness

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.R
import com.example.aurafit.adapters.MeditationAdapter
import com.example.aurafit.model.MeditationSession

class MeditationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation)

        val sessions = listOf(
            MeditationSession("Morning Meditation", "Start your day with positive energy.", "https://www.pexels.com/video/a-woman-meditating-on-a-platform-overseeing-the-rice-field-3209148/"),
            MeditationSession("Evening Relaxation", "Wind down and relax before sleep.", "https://example.com/media2.mp3"),
            // Add more sessions here
        )

        val adapter = MeditationAdapter(this, sessions)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
    }
}
