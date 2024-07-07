package com.example.aurafit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            "Depression Test" -> {
                listOf(
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
                    // Add more questions for Depression Test as needed
                )
            }
            "Anxiety Test" -> {
                listOf(
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
                    // Add more questions for Anxiety Test as needed
                )
            }
            "Stress Test" -> {
                listOf(
                    QuestionModel(
                        "In the last month, how often have you felt nervous or stressed?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you felt overwhelmed by all you had to do?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you been able to control irritations in your life?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you felt confident about your ability to handle your personal problems?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you felt that things were going your way?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you found that you could not cope with all the things that you had to do?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you been angered because of things that were outside of your control?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you been able to control the way you spend your time?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "In the last month, how often have you felt that you were on top of things?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    )
                    // Add more questions for Stress Test as needed
                )
            }
            "Self Esteem Test" -> {
                listOf(
                    QuestionModel(
                        "How often do you feel confident about yourself and your abilities?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel good about yourself as a person?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel proud of your accomplishments?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you compare yourself negatively to others?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel capable of handling challenges and problems?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel you have a lot to offer to others?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel deserving of happiness?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel respected by others?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel satisfied with yourself?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for Self Esteem Test as needed
                )
            }
            "Social Anxiety Test" -> {
                listOf(
                    QuestionModel(
                        "How often do you feel nervous or uncomfortable in social situations?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you avoid social situations due to fear or anxiety?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you worry about embarrassing yourself in front of others?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you fear being judged by others?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How often do you experience physical symptoms (e.g., sweating, trembling) in social situations?",
                        listOf("Never", "Rarely", "Sometimes", "Always")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for Social Anxiety Test as needed
                )
            }
            "PTSD Test" -> {
                listOf(
                    QuestionModel(
                        "Have you ever experienced a traumatic event?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you have upsetting memories, flashbacks, or nightmares about the traumatic event?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you often feel on edge, jittery, or easily startled?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you avoid situations or places that remind you of the traumatic event?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you have trouble sleeping or concentrating since the traumatic event?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have your feelings and relationships changed since the traumatic event?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for PTSD Test as needed
                )
            }
            "Eating Disorder Test" -> {
                listOf(
                    QuestionModel(
                        "Have you ever had periods where you eat an unusually large amount of food and feel unable to stop?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you worry a lot about your body shape or weight?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you often restrict the amount of food you eat or use other methods (e.g., fasting, excessive exercise) to control your weight?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you feel ashamed or guilty about your eating habits?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you binge eat and then try to compensate (e.g., by vomiting, using laxatives, excessive exercise)?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have others expressed concern about your eating habits or weight?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for Eating Disorder Test as needed
                )
            }
            "OCD Test" -> {
                listOf(
                    QuestionModel(
                        "Do you have unwanted thoughts, images, or impulses that seem to repeat in your mind?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you feel driven to perform certain behaviors or rituals to reduce anxiety or prevent something bad from happening?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you spend a lot of time each day thinking about or performing these thoughts or rituals?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you find these thoughts or behaviors distressing and time-consuming?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have others expressed concern about your thoughts or behaviors?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for OCD Test as needed
                )
            }
            "Sleep Quality Test" -> {
                listOf(
                    QuestionModel(
                        "How often do you have trouble falling asleep?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you wake up in the middle of the night or early in the morning?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you feel tired or not well-rested after waking up?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you have trouble staying awake during daytime activities?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often does your sleep problem interfere with your daily functioning?",
                        listOf("Not at all", "A little", "Moderately", "Severely")
                    ),
                    QuestionModel(
                        "How often do you worry about your sleep?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    )
                    // Add more questions for Sleep Quality Test as needed
                )
            }
            "Substance Abuse Test" -> {
                listOf(
                    QuestionModel(
                        "Have you ever felt you should cut down on your drinking or drug use?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have people annoyed you by criticizing your drinking or drug use?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have you ever felt guilty about your drinking or drug use?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have you ever used alcohol or drugs to relax or feel better?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have you ever blacked out or forgotten what you did while drinking or using drugs?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have you ever had legal problems because of your drinking or drug use?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "How often have you failed to do what was normally expected of you because of your drinking or drug use?",
                        listOf("Never", "Rarely", "Sometimes", "Often")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for Substance Abuse Test as needed
                )
            }
            "Bipolar Disorder Test" -> {
                listOf(
                    QuestionModel(
                        "Have you ever experienced episodes of unusually intense emotion, changes in sleep patterns, or unusual behavior that interfere with your life?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have you ever had periods of depression, where you felt sad, hopeless, or lost interest in most activities?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have you ever had periods of high energy, euphoria, or agitation?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have your mood swings caused problems at work, home, or in relationships?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you experience changes in your activity or energy levels that interfere with your daily life?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Do you often make impulsive decisions or take risks that could harm you or others?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "Have others expressed concern about your mood or behavior?",
                        listOf("Yes", "No")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                    )
                    // Add more questions for Bipolar Disorder Test as needed
                )
            }
            "ADHD Test" -> {
                listOf(
                    QuestionModel(
                        "How often do you find it difficult to focus on tasks or activities, such as schoolwork or chores?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you have trouble remaining seated when it is expected, such as in the classroom or at work?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you find yourself easily distracted by external stimuli or unrelated thoughts?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you have difficulty organizing tasks or activities?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you avoid or dislike tasks that require sustained mental effort?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you lose things necessary for tasks or activities, such as school supplies or work materials?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you forget to do tasks or activities, such as forgetting to turn in homework or pay bills on time?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you interrupt or intrude on others, such as butting into conversations or games?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How often do you have difficulty waiting your turn, such as waiting in line?",
                        listOf("Never", "Sometimes", "Often", "Always")
                    ),
                    QuestionModel(
                        "How difficult have these problems made it for you to do your work, take care of things at home, or get along with other people?",
                        listOf(
                            "Not difficult at all",
                            "Somewhat difficult",
                            "Very difficult",
                            "Extremely difficult"
                        )
                    )
                    // Add more questions for ADHD Test as needed
                )
            }
                "Phobia Test" -> {
                    listOf(
                        QuestionModel(
                            "Do you have an intense, irrational fear of specific objects, situations, or activities?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Does this fear cause you to avoid the feared object or situation?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Do you experience immediate anxiety or panic when exposed to the feared object or situation?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "How much does this fear interfere with your daily life or activities?",
                            listOf("Not at all", "A little", "Moderately", "Severely")
                        )
                        // Add more questions for Phobia Test as needed
                    )
                }
                "Body Image Test" -> {
                    listOf(
                        QuestionModel(
                            "Are you generally satisfied with your body shape and size?",
                            listOf("Very satisfied", "Satisfied", "Neutral", "Dissatisfied", "Very dissatisfied")
                        ),
                        QuestionModel(
                            "Do you often compare your body to others?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How often do you feel ashamed or self-conscious about your body?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How often do negative thoughts about your body impact your mood or daily activities?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How often do you engage in behaviors to change your body shape or size (e.g., dieting, excessive exercise)?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How difficult have these concerns made it for you to engage in social activities or relationships?",
                            listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                        )
                        // Add more questions for Body Image Test as needed
                    )
                }
                "Burnout Test" -> {
                    listOf(
                        QuestionModel(
                            "Do you feel exhausted physically or emotionally?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "Do you feel detached or cynical about your work or responsibilities?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "Do you feel ineffective or that your work doesn't matter?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "Do you feel overwhelmed by your workload or responsibilities?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How often do you experience physical symptoms such as headaches, digestive issues, or sleep disturbances due to stress?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How difficult have these feelings made it for you to perform well at work or enjoy personal activities?",
                            listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                        )
                        // Add more questions for Burnout Test as needed
                    )
                }
                "Mood Disorder Test" -> {
                    listOf(
                        QuestionModel(
                            "Over the last 2 weeks, how often have you been bothered by feeling sad, down, or hopeless?",
                            listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                        ),
                        QuestionModel(
                            "Have you lost interest or pleasure in activities you usually enjoy?",
                            listOf("Not at all", "Several days", "More than half the days", "Nearly every day")
                        ),
                        QuestionModel(
                            "Do you experience significant changes in appetite, weight, or sleep patterns?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Do you feel excessively tired or lacking energy most days?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Do you have difficulty concentrating, making decisions, or remembering things?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Have you had thoughts of harming yourself or that life is not worth living?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "How difficult have these symptoms made it for you to work, take care of things at home, or get along with other people?",
                            listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                        )
                        // Add more questions for Mood Disorder Test as needed
                    )
                }
                "Panic Disorder Test" -> {
                    listOf(
                        QuestionModel(
                            "Have you experienced sudden and repeated attacks of fear or discomfort?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "During these attacks, do you experience symptoms such as rapid heartbeat, sweating, shaking, or shortness of breath?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Do you avoid certain situations or places because you fear having a panic attack?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "Do you worry excessively about having another panic attack?",
                            listOf("Yes", "No")
                        ),
                        QuestionModel(
                            "How much have these attacks or worry about attacks interfered with your life?",
                            listOf("Not at all", "A little", "Moderately", "Severely")
                        )
                        // Add more questions for Panic Disorder Test as needed
                    )
                }
                "Work Stress Test" -> {
                    listOf(
                        QuestionModel(
                            "How often do you feel overwhelmed by your workload or responsibilities?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "Do you feel pressured to meet deadlines or perform at a high level?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "Do you have little control over your work or the decisions that affect you?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "Do you have conflicts with coworkers or supervisors?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How often do you experience physical symptoms such as headaches, digestive issues, or sleep disturbances due to work stress?",
                            listOf("Never", "Rarely", "Sometimes", "Often", "Always")
                        ),
                        QuestionModel(
                            "How difficult have these feelings made it for you to perform well at work or enjoy personal activities?",
                            listOf("Not difficult at all", "Somewhat difficult", "Very difficult", "Extremely difficult")
                        )
                        // Add more questions for Work Stress Test as needed
                    )
            }
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