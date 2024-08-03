package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aurafit.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Day30ProgramDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_day30exercise, container, false)
    }
}
