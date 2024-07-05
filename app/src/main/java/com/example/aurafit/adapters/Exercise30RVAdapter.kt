package com.example.aurafit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.model.Exercises

class Exercise30RVAdapter(
    private val context: Context,
    private val exercises: List<Exercises>,
    private val onItemClickListener: (Exercises) -> Unit
) : RecyclerView.Adapter<Exercise30RVAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_exercise30, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.exercise_name)
        private val exerciseGif: ImageView = itemView.findViewById(R.id.exercise_gif)

        fun bind(exercise: Exercises) {
            exerciseName.text = exercise.name

            Glide.with(context)
                .load(exercise.gifUrl)
                .into(exerciseGif)

            itemView.setOnClickListener { onItemClickListener(exercise) }
        }
    }
}
