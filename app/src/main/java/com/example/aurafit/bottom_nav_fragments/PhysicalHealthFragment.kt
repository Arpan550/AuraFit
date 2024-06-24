package com.example.aurafit.bottom_nav_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aurafit.MusicActivity
import com.example.aurafit.databinding.FragmentPhysicalHealthBinding

class PhysicalHealthFragment : Fragment() {

    private var physicalbinding: FragmentPhysicalHealthBinding? = null
    private val binding get() = physicalbinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        physicalbinding = FragmentPhysicalHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle click event for the music card
        binding.musicCont.setOnClickListener {
            val intent = Intent(activity, MusicActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        physicalbinding = null
    }
}
