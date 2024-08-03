package com.example.aurafit.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.R
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class QuestionUploadToFireStore : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call the function to upload questions for each self-assessment test
        uploadSelfAssessmentQuestions()
    }

    private fun uploadSelfAssessmentQuestions() {
        val selfAssessmentTests = listOf(
            SelfAssessment("Depression Test", "Assess your level of depression."),
            SelfAssessment("Anxiety Test", "Assess your level of anxiety."),
            SelfAssessment("Stress Test", "Assess your level of stress."),
            SelfAssessment("Self Esteem Test", "Evaluate your self-esteem."),
            SelfAssessment("Social Anxiety Test", "Assess your level of social anxiety."),
            SelfAssessment("PTSD Test", "Assess symptoms of PTSD."),
        )

        selfAssessmentTests.forEach { assessment ->
            val questions = generateQuestionsForAssessment(assessment)
            uploadQuestions(assessment.name, questions)
                .addOnSuccessListener {
                    // Handle success if needed
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }
    }

    private fun generateQuestionsForAssessment(assessment: SelfAssessment): List<QuestionModel> {
        // Generate questions for each assessment type
        return when (assessment.name) {
            "Depression Test" -> listOf(
                QuestionModel(
                    "Over the last 2 weeks, how often have you been bothered by little interest or pleasure in doing things?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "Over the last 2 weeks, how often have you been bothered by feeling down, depressed, or hopeless?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you felt tired or had little energy?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you had trouble falling or staying asleep, or sleeping too much?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you had a poor appetite or been overeating?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you felt bad about yourself - or that you are a failure or have let yourself or your family down?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you had trouble concentrating on things, such as reading the newspaper or watching television?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you moved or spoken so slowly that other people could have noticed? Or the opposite - been so fidgety or restless that you have been moving around a lot more than usual?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How often have you had thoughts that you would be better off dead, or of hurting yourself in some way?",
                    listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                ),
                QuestionModel(
                    "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                    listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                )
            )

            "Anxiety Test" -> listOf(
                QuestionModel(
                    "How often do you experience feelings of excessive worry or fear?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you had difficulty controlling your worry?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you felt restless or irritable?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you had trouble relaxing?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you had difficulty concentrating on things, such as reading the newspaper or watching television?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you been easily annoyed or irritable?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you felt afraid that something awful might happen?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you avoided situations or activities because they make you anxious?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often have you experienced physical symptoms such as a racing heart, sweating, trembling, or shaking?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                    listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                )
            )

            "Stress Test" -> listOf(
                QuestionModel(
                    "How often have you felt stressed in the last month?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you felt overwhelmed by your responsibilities?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you had difficulty relaxing?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you felt nervous or anxious?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you experienced physical symptoms of stress such as headaches, muscle tension, or stomach problems?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you found it hard to concentrate because you're thinking about things that worry you?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you found yourself feeling angry or irritable?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often have you had trouble sleeping because you're worrying about things?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How difficult have these problems made it for you to function in your daily life?",
                    listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                )
            )

            "Self Esteem Test" -> listOf(
                QuestionModel(
                    "How often do you feel confident about yourself?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you feel good about yourself?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you feel proud of your accomplishments?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you feel competent?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you believe in yourself?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you feel valued by others?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you accept yourself?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How often do you compare yourself negatively to others?",
                    listOf("Never", "Rarely", "Sometimes", "Always")
                ),
                QuestionModel(
                    "How difficult have these feelings made it for you to lead a fulfilling life?",
                    listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                )
            )

            "Social Anxiety Test" -> listOf(
                QuestionModel(
                    "How often do you feel anxious or uncomfortable in social situations?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often do you worry that others are judging you negatively?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often do you avoid social situations due to fear of embarrassment or humiliation?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How often do you experience physical symptoms such as sweating, trembling, or blushing in social situations?",
                    listOf("Never", "Occasionally", "Often", "Constantly")
                ),
                QuestionModel(
                    "How difficult have these problems made it for you to engage in social activities or relationships?",
                    listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                )
            )

            "PTSD Test" -> listOf(
                QuestionModel(
                    "Have you experienced or witnessed a traumatic event?",
                    listOf("Yes", "No")
                ),
                QuestionModel(
                    "How often do you have intrusive memories or flashbacks of the traumatic event?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often do you avoid reminders of the traumatic event?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often do you experience negative thoughts or feelings related to the traumatic event?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How often do you experience heightened alertness or reactions such as being easily startled or feeling tense?",
                    listOf("Never", "Rarely", "Sometimes", "Often")
                ),
                QuestionModel(
                    "How difficult have these symptoms made it for you to function in your daily life?",
                    listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                )
            )

            // Add more assessments and questions as needed

            else -> emptyList()
        }
    }


    private fun uploadQuestions(collectionName: String, questions: List<QuestionModel>): Task<Void> {
        val questionsMap = questions.mapIndexed { index, question ->
            "question${index + 1}" to question.toMap()
        }.toMap()

        // Upload to Firestore under the "SelfAssessmentQuestions" collection
        return db.collection("SelfAssessmentQuestions")
            .document(collectionName)
            .set(questionsMap, SetOptions.merge())
    }

    private fun QuestionModel.toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["questionText"] = questionText
        map["options"] = options
        // Add other fields as needed (severityScale, difficultyImpact, selectedFrequency, etc.)
        return map
    }

    data class SelfAssessment(val name: String, val description: String)

    data class QuestionModel(
        val questionText: String,
        val options: List<String>? = null,
        val severityScale: String? = null,
        val difficultyImpact: String? = null,
        val selectedFrequency: String? = null
    )
}