package com.example.aurafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aurafit.databinding.ItemExercise60Binding
import com.example.aurafit.model.Exercise
import com.example.aurafit.model.ExerciseGroup

class Exercise60RVAdapter(private val exerciseGroups: List<ExerciseGroup>, private val listener: ExerciseClickListener) : RecyclerView.Adapter<Exercise60RVAdapter.ExerciseGroupViewHolder>() {

    interface ExerciseClickListener {
        fun onExerciseClicked(exercise: Exercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseGroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemExercise60Binding.inflate(inflater, parent, false)
        return ExerciseGroupViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ExerciseGroupViewHolder, position: Int) {
        val exerciseGroup = exerciseGroups[position]
        holder.bind(exerciseGroup)
    }

    override fun getItemCount(): Int {
        return exerciseGroups.size
    }

    inner class ExerciseGroupViewHolder(private val binding: ItemExercise60Binding, private val listener: ExerciseClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(exerciseGroup: ExerciseGroup) {
            binding.exerciseTitle.text = exerciseGroup.title

            // Assuming you have exercises in a list
            val exercises = exerciseGroup.exercises

            // Display exercise names and load GIFs using Glide
            for (i in exercises.indices) {
                val exercise = exercises[i]
                when (i) {
                    0 -> {
                        binding.exerciseName.text = exercise.name
                        Glide.with(binding.root.context).asGif().load(exercise.gifUrl).into(binding.exerciseGif)
                        binding.exerciseLayout.setOnClickListener { onExerciseClicked(i) }
                    }
                    1 -> {
                        binding.exercise2Name.text = exercise.name
                        Glide.with(binding.root.context).asGif().load(exercise.gifUrl).into(binding.exercise2Gif)
                        binding.exercise2Layout.setOnClickListener { onExerciseClicked(i) }
                    }
                    2 -> {
                        binding.exercise3Name.text = exercise.name
                        Glide.with(binding.root.context).asGif().load(exercise.gifUrl).into(binding.exercise3Gif)
                        binding.exercise3Layout.setOnClickListener { onExerciseClicked(i) }
                    }
                    // Add more cases as needed
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // You can implement a default behavior here if needed
            }
        }

        private fun onExerciseClicked(index: Int) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && index < exerciseGroups[position].exercises.size) {
                val exercise = exerciseGroups[position].exercises[index]
                listener.onExerciseClicked(exercise)
            }
        }
    }

}
