package com.example.aurafit.bottom_nav_fragments.mental_fitness

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aurafit.R
import com.example.aurafit.adapters.SelfAssessmentAdapter
import com.example.aurafit.model.SelfAssessment

class SelfAssessmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelfAssessmentAdapter

    private val assessments = listOf(
        SelfAssessment("Depression Test", "Assess your level of depression."),
        SelfAssessment("Anxiety Test", "Assess your level of anxiety."),
        SelfAssessment("Stress Test", "Assess your level of stress."),
        SelfAssessment("Self Esteem Test", "Evaluate your self-esteem."),
        SelfAssessment("Social Anxiety Test", "Assess your level of social anxiety."),
        SelfAssessment("PTSD Test", "Assess symptoms of PTSD.")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_assessment)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SelfAssessmentAdapter(assessments) { assessment ->
            // Handle the click event for each assessment
            val intent = Intent(this, AssessmentDetailsActivity::class.java)
            intent.putExtra("ASSESSMENT_NAME", assessment.name)
            intent.putExtra("ASSESSMENT_DETAILS", assessment.details)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

}
