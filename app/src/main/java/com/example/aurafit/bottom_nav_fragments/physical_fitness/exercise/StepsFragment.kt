package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.aurafit.R

class StepsFragment : Fragment() {

    private lateinit var stepsTextView: TextView
    private var formattedSteps: String? = null

    companion object {
        private const val ARG_STEPS = "formatted_steps"

        @JvmStatic
        fun newInstance(formattedSteps: String): StepsFragment {
            val fragment = StepsFragment()
            val args = Bundle()
            args.putString(ARG_STEPS, formattedSteps)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_steps, container, false)
        stepsTextView = view.findViewById(R.id.stepsTextView)

        // Retrieve arguments and set UI
        formattedSteps = arguments?.getString(ARG_STEPS)
        formattedSteps?.let {
            stepsTextView.text = it
        }

        return view
    }
}
