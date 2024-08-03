package com.example.aurafit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.aurafit.R
import com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise.ExerciseDetailsActivity
import com.example.aurafit.model.MindfulnessExercise

class MindfulnessExerciseAdapter(private val context: Context, private val exercises: List<MindfulnessExercise>) : BaseAdapter() {

    override fun getCount(): Int {
        return exercises.size
    }

    override fun getItem(position: Int): Any {
        return exercises[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_mindfulness_exercise, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val exercise = exercises[position]
        viewHolder.titleTextView.text = exercise.title
        viewHolder.descriptionTextView.text = exercise.description // Set description text

        viewHolder.cardView.setOnClickListener {
            val intent = Intent(context, ExerciseDetailsActivity::class.java).apply {
                putExtra("exercise_name", exercise.title)
                putExtra("exercise_gif_url", exercise.gifUrl) // Pass GIF URL to ExerciseDetailsActivity
                putExtra("exercise_steps", ArrayList(exercise.steps))
            }
            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView) // Add this line
    }
}
