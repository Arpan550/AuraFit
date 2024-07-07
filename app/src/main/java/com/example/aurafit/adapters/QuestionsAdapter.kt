package com.example.aurafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aurafit.R
import com.example.aurafit.bottom_nav_fragments.mental_fitness.AssessmentDetailsActivity

class QuestionsAdapter : ListAdapter<AssessmentDetailsActivity.QuestionModel, QuestionsAdapter.QuestionViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(question: AssessmentDetailsActivity.QuestionModel) {
            val questionTextView: TextView = itemView.findViewById(R.id.question_text_view)
            val optionsRadioGroup: RadioGroup = itemView.findViewById(R.id.options_radio_group)

            questionTextView.text = "${adapterPosition + 1}. ${question.questionText}"

            optionsRadioGroup.removeAllViews()

            question.options.forEachIndexed { index, optionText ->
                val radioButton = RadioButton(itemView.context)
                radioButton.text = optionText
                radioButton.setOnClickListener {
                    question.selectedOptionIndex = index
                }
                optionsRadioGroup.addView(radioButton)
            }
        }
    }

    // DiffUtil callback to handle list updates
    private class QuestionDiffCallback : DiffUtil.ItemCallback<AssessmentDetailsActivity.QuestionModel>() {
        override fun areItemsTheSame(oldItem: AssessmentDetailsActivity.QuestionModel, newItem: AssessmentDetailsActivity.QuestionModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AssessmentDetailsActivity.QuestionModel, newItem: AssessmentDetailsActivity.QuestionModel): Boolean {
            return oldItem == newItem
        }
    }
}
