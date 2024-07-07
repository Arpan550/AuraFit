import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aurafit.R
import com.example.aurafit.model.SelfAssessment

class SelfAssessmentAdapter(private val assessments: List<SelfAssessment>, private val listener: (SelfAssessment) -> Unit) :
    RecyclerView.Adapter<SelfAssessmentAdapter.AssessmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_self_assessment_test, parent, false)
        return AssessmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssessmentViewHolder, position: Int) {
        val assessment = assessments[position]
        holder.bind(assessment)
        holder.itemView.setOnClickListener {
            listener.invoke(assessment)
        }
    }

    override fun getItemCount(): Int {
        return assessments.size
    }

    inner class AssessmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.assessment_name)
        private val detailsTextView: TextView = itemView.findViewById(R.id.assessment_description)

        fun bind(assessment: SelfAssessment) {
            nameTextView.text = assessment.name
            detailsTextView.text = assessment.details
        }
    }
}
