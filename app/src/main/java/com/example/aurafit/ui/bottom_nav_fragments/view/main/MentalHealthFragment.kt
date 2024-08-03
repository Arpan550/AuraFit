package com.example.aurafit.ui.bottom_nav_fragments.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aurafit.ui.bottom_nav_fragments.view.mental_fitness.MeditationActivity
import com.example.aurafit.ui.bottom_nav_fragments.view.mental_fitness.MindfulnessActivity
import com.example.aurafit.ui.bottom_nav_fragments.view.mental_fitness.MindCareActivity
import com.example.aurafit.databinding.FragmentMentalHealthBinding

class MentalHealthFragment : Fragment() {

    private lateinit var binding: FragmentMentalHealthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding
        binding = FragmentMentalHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for each feature card
        binding.meditationCont.setOnClickListener {
            val intent=Intent(requireContext(), MeditationActivity::class.java)
            startActivity(intent)
        }

        binding.mindfulnessCont.setOnClickListener {
           val intent= Intent(requireContext(), MindfulnessActivity::class.java)
            startActivity(intent)
        }

        binding.mindcareCont.setOnClickListener {
            val intent=Intent(requireContext(), MindCareActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToFeature(destinationId: Int) {
        findNavController().navigate(destinationId)
    }
}
