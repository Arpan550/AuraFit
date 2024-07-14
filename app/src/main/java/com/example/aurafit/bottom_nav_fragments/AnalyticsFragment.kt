package com.example.aurafit.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aurafit.R
import com.example.aurafit.databinding.FragmentAnalyticsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide

class AnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentAnalyticsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var userId: String = "" // Initialize with the user's actual ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Replace with your actual user ID retrieval logic
        userId = auth.currentUser?.uid ?: ""

        // Fetch user details from Firestore
        fetchUserDetails()

        // Fetch BMI details from Firestore
        fetchBMIDetails()
    }

    private fun fetchUserDetails() {
        val userRef = firestore.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val email = document.getString("email") ?: ""
                    val name = document.getString("name") ?: ""
                    val photoUrl = document.getString("photoUrl") ?: ""

                    // Update UI with user details
                    binding.userEmailTextView.text = email
                    binding.userNameTextView.text = name
                    // Load user photo using Glide
                    Glide.with(requireContext())
                        .load(photoUrl)
                        .circleCrop()
                        .placeholder(R.drawable.img) // Placeholder image while loading
                        .error(R.drawable.img) // Error image if loading fails
                        .into(binding.userPhotoImageView)
                } else {
                    // Document doesn't exist
                    // Handle this case based on your app's requirements
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                // For example, log the error or show an error message to the user
            }
    }

    private fun fetchBMIDetails() {
        val bmiRef = firestore.collection("bmi_info").document(userId).collection("latest").document("details")

        bmiRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val weight = document.getDouble("weight") ?: 0.0
                    val height = document.getDouble("height") ?: 0.0
                    val bmi = document.getDouble("bmi") ?: 0.0
                    val category = document.getString("category") ?: ""

                    // Update UI with BMI details
                    binding.bmiWeightTextView.text = String.format("%.2f kg", weight)
                    binding.bmiHeightTextView.text = String.format("%.2f m", height)
                    binding.bmiValueTextView.text = String.format("%.2f", bmi)
                    binding.bmiCategoryTextView.text = category
                } else {
                    // Document doesn't exist
                    // Handle this case based on your app's requirements
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                // For example, log the error or show an error message to the user
            }
    }
}
