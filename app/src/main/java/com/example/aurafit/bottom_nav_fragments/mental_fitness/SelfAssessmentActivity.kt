package com.example.aurafit.bottom_nav_fragments.mental_fitness


import SelfAssessmentAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aurafit.R
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
        SelfAssessment("PTSD Test", "Assess symptoms of PTSD."),
        SelfAssessment("Eating Disorder Test", "Identify symptoms of eating disorders."),
        SelfAssessment("OCD Test", "Assess obsessive-compulsive disorder."),
        SelfAssessment("Sleep Quality Test", "Evaluate the quality of your sleep."),
        SelfAssessment("Substance Abuse Test", "Assess substance abuse tendencies."),
        SelfAssessment("Bipolar Disorder Test", "Identify symptoms of bipolar disorder."),
        SelfAssessment("ADHD Test", "Assess symptoms of ADHD."),
        SelfAssessment("Phobia Test", "Identify specific phobias."),
        SelfAssessment("Body Image Test", "Evaluate your body image."),
        SelfAssessment("Burnout Test", "Assess your level of burnout."),
        SelfAssessment("Mood Disorder Test", "Identify symptoms of mood disorders."),
        SelfAssessment("Panic Disorder Test", "Assess symptoms of panic disorder."),
        SelfAssessment("Work Stress Test", "Evaluate your work-related stress.")
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
            finish()
        }
        recyclerView.adapter = adapter
    }
}
