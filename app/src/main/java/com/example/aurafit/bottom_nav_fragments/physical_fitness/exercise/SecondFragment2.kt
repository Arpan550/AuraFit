package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.Exercise60RVAdapter
import com.example.aurafit.databinding.FragmentSecond2Binding
import com.example.aurafit.model.Exercise
import com.example.aurafit.model.ExerciseGroup

class SecondFragment2 : Fragment(), Exercise60RVAdapter.ExerciseClickListener {

    private var _binding: FragmentSecond2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecond2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and set adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = Exercise60RVAdapter(getExerciseGroups(), this)
    }

    override fun onExerciseClicked(exercise: Exercise) {
        // Handle exercise item click here
        val intent = Intent(context, ExerciseDetailsActivity::class.java).apply {
            putExtra("exercise_name", exercise.name)
            putExtra("exercise_gif_url", exercise.gifUrl)
            putExtra("exercise_steps", ArrayList(exercise.steps))
        }
        startActivity(intent)
    }

    fun getExerciseGroups(): List<ExerciseGroup> {
        return listOf(
            ExerciseGroup(
                "Chest Exercises", listOf(
                    Exercise(
                        "Push Up Shuffle",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/push-up-shuffle.gif?w=1110",
                        listOf(
                            "Start on your hands and knees on a yoga mat on the floor, with your hands resting slightly wider than the width of your shoulders.",
                            "Complete a push-up (either on your knees or with your legs extended behind you, depending on your fitness level).",
                            "Keep your core engaged and shuffle your right hand and right foot a step to the right side.",
                            "Bring your left hand and foot to meet them.",
                            "Complete another push-up there.",
                            "Return to center and repeat on the opposite side."
                        )
                    ),
                    Exercise(
                        "Push-Ups",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/push-up.gif?w=1110",
                        listOf(
                            "Start in a plank position with your hands placed directly under your shoulders.",
                            "Lower your body until your chest almost touches the floor.",
                            "Keep your elbows close to your body as you lower yourself.",
                            "Push yourself back up to the starting position.",
                            "Keep your core tight and your body in a straight line throughout the movement."
                        )
                    ),
                    Exercise(
                        "Isometric Chest Squeeze",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/isometric-chest-squeeze.gif?w=1110",
                        listOf(
                            "Stand with your feet shoulder-width apart.",
                            "Hold a medicine ball or similar object between your hands, arms extended in front of you.",
                            "Squeeze the ball with both hands as hard as you can.",
                            "Hold the squeeze for a set amount of time (e.g., 10-30 seconds).",
                            "Release and repeat."
                        )
                    )
                )
            ),
            ExerciseGroup(
                "Arm Exercises", listOf(
                    Exercise(
                        "Triceps Dip",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/tricep-dip.gif?w=1110",
                        listOf(
                            "Sit on the edge of a sturdy chair or bench.",
                            "Place your hands on the edge of the seat, fingers pointing forward, and slide your butt off the seat.",
                            "Lower your body until your elbows are at a 90-degree angle.",
                            "Push back up to the starting position.",
                            "Keep your elbows close to your body throughout the movement."
                        )
                    ),
                    Exercise(
                        "Triangle Push-Ups",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/triangle-pushup.gif?w=1110",
                        listOf(
                            "Start in a plank position with your hands close together, forming a triangle shape with your thumbs and index fingers.",
                            "Lower your body until your chest almost touches your hands.",
                            "Keep your elbows close to your body as you lower yourself.",
                            "Push yourself back up to the starting position.",
                            "Maintain a straight body line and engaged core throughout the exercise."
                        )
                    ),
                    Exercise(
                        "Plank Up Down",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/plank-up-down.gif?w=1110",
                        listOf(
                            "Start in a forearm plank position with your elbows directly under your shoulders.",
                            "Push up onto your right hand, then your left hand, into a high plank position.",
                            "Lower back down to your right forearm, then your left forearm, returning to the forearm plank.",
                            "Keep your core tight and hips stable throughout the movement.",
                            "Alternate the lead arm with each repetition."
                        )
                    )
                )
            ),
            ExerciseGroup(
                "Back Exercises", listOf(
                    Exercise(
                        "Superman Y",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/superman-y.gif?w=1110",
                        listOf(
                            "Lie face down on a mat with your arms extended overhead in a 'Y' shape.",
                            "Lift your arms, chest, and legs off the ground simultaneously.",
                            "Hold the top position briefly, squeezing your back muscles.",
                            "Lower back down to the starting position.",
                            "Repeat the movement, keeping your neck neutral and core engaged."
                        )
                    ),
                    Exercise(
                        "Bird Dog",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/bird-dog.gif?w=1110",
                        listOf(
                            "Start on your hands and knees with your wrists under your shoulders and knees under your hips.",
                            "Extend your right arm forward and left leg backward simultaneously.",
                            "Hold the position for a moment, keeping your core tight.",
                            "Return to the starting position.",
                            "Repeat with the left arm and right leg."
                        )
                    ),
                    Exercise(
                        "Superman T",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/superman-t.gif?w=1110",
                        listOf(
                            "Lie face down on a mat with your arms extended out to the sides in a 'T' shape.",
                            "Lift your arms, chest, and legs off the ground simultaneously.",
                            "Hold the top position briefly, squeezing your shoulder blades together.",
                            "Lower back down to the starting position.",
                            "Repeat the movement, keeping your neck neutral and core engaged."
                        )
                    )
                )
            ),
            ExerciseGroup(
                "Core and Abdominal Muscles Exercises", listOf(
                    Exercise(
                        "Bicycle Crunches",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/bicycle-crunch.gif?w=1110",
                        listOf(
                            "Lie on your back with your hands behind your head and legs lifted, knees bent at 90 degrees.",
                            "Bring your right elbow towards your left knee while extending your right leg.",
                            "Switch sides, bringing your left elbow towards your right knee while extending your left leg.",
                            "Continue alternating sides in a pedaling motion.",
                            "Keep your core engaged and avoid pulling on your neck."
                        )
                    ),
                    Exercise(
                        "Plank",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/plank.gif?w=1110",
                        listOf(
                            "Start in a forearm plank position with your elbows directly under your shoulders.",
                            "Keep your body in a straight line from head to heels.",
                            "Engage your core and hold the position.",
                            "Avoid letting your hips sag or rise.",
                            "Hold for the desired amount of time."
                        )
                    ),
                    Exercise(
                        "Side Plank",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/side-plank.gif?w=1110",
                        listOf(
                            "Lie on your side with your legs extended and feet stacked.",
                            "Place your elbow directly under your shoulder.",
                            "Lift your hips off the ground, forming a straight line from head to feet.",
                            "Hold the position, keeping your core tight.",
                            "Repeat on the other side."
                        )
                    )
                )
            ),
            ExerciseGroup(
                "Leg Exercises", listOf(
                    Exercise(
                        "Jump Squats",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/jump-squat.gif?w=1110",
                        listOf(
                            "Stand with your feet shoulder-width apart.",
                            "Lower into a squat position, keeping your chest up and knees behind your toes.",
                            "Explosively jump up, reaching for the ceiling.",
                            "Land softly back into the squat position.",
                            "Repeat the movement with minimal rest between jumps."
                        )
                    ),
                    Exercise(
                        "Reverse Lunges",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/reverse-lunge7032753c08e243f09d380d15e1afa9a3.gif?w=1110",
                        listOf(
                            "Stand with your feet hip-width apart.",
                            "Step back with your right foot and lower into a lunge position.",
                            "Both knees should be bent at 90 degrees.",
                            "Push through your left heel to return to the starting position.",
                            "Repeat on the other side."
                        )
                    ),
                    Exercise(
                        "Donkey Kicks",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/donkey-kicks.gif?w=1110",
                        listOf(
                            "Start on your hands and knees with your wrists under your shoulders and knees under your hips.",
                            "Lift your right leg, keeping your knee bent at 90 degrees.",
                            "Press your foot towards the ceiling, squeezing your glutes.",
                            "Lower back to the starting position.",
                            "Repeat on the other side."
                        )
                    )
                )
            ),
            ExerciseGroup(
                "Full Body Exercises", listOf(
                    Exercise(
                        "Burpee",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/burpee5e81d693ff7a401eab879388b763bf9f.gif?w=1110",
                        listOf(
                            "Stand with your feet shoulder-width apart.",
                            "Lower into a squat and place your hands on the floor.",
                            "Jump your feet back into a plank position.",
                            "Perform a push-up, then jump your feet back towards your hands.",
                            "Explosively jump up, reaching for the ceiling.",
                            "Land softly and immediately go into the next repetition."
                        )
                    ),
                    Exercise(
                        "Mountain Climbers",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/mountain-climbers59e8be018fb64b06a7b0b18149319722.gif?w=1110",
                        listOf(
                            "Start in a plank position with your hands under your shoulders.",
                            "Drive your right knee towards your chest.",
                            "Quickly switch legs, bringing your left knee towards your chest.",
                            "Continue alternating legs at a fast pace.",
                            "Keep your core tight and body in a straight line."
                        )
                    ),
                    Exercise(
                        "Bear Crawls",
                        "https://images.everydayhealth.com/images/healthy-living/fitness/bear-crawls.gif?w=1110",
                        listOf(
                            "Start on your hands and knees with your wrists under your shoulders and knees under your hips.",
                            "Lift your knees off the ground, keeping them bent.",
                            "Move forward by simultaneously stepping your right hand and left foot, then your left hand and right foot.",
                            "Continue crawling forward, keeping your core engaged.",
                            "Maintain a low position throughout the movement."
                        )
                    )
                )
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
