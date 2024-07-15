package com.example.aurafit.bottom_nav_fragments.physical_fitness.exercise

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aurafit.R
import com.example.aurafit.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var progressDialog: ProgressDialog? = null
    private var programDuration: Int = 0
    private var userId: String = ""
    private var programAlreadyRegistered: Boolean = false
    private lateinit var programDetailsDocRef: DocumentReference
    private var updateCurrentDayHandler: Handler? = null
    private var updateCurrentDayRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set up click listeners for navigation actions
        binding.btnStart30.setOnClickListener {
            startProgram("Fitness Challenge", 30)
        }

        binding.btnStart60.setOnClickListener {
            startProgram("Strength Journey", 60)
        }
    }

    private fun startProgram(programName: String, durationDays: Int) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userId = currentUser.uid
            programDuration = durationDays

            val currentTime = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentTime

            val dateFormat = SimpleDateFormat("dd MMM,HH:mm:ss", Locale.getDefault())
            val startDateFormatted = dateFormat.format(calendar.time)

            val endDate = Calendar.getInstance()
            endDate.timeInMillis = currentTime
            endDate.add(Calendar.DAY_OF_MONTH, durationDays)
            val endDateFormatted = dateFormat.format(endDate.time)

            val programDetails = mapOf(
                "programName" to programName,
                "startDate" to startDateFormatted,
                "endDate" to endDateFormatted,
                "currentDay" to 1 // Initialize current day as 1
            )

            // Check if user has already registered for this program
            checkProgramRegistration(userId, durationDays, programDetails)
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkProgramRegistration(userId: String, durationDays: Int, programDetails: Map<String, Any>) {
        // Check if user has already registered a program
        firestore.collection("user_programs").document(userId)
            .collection("$durationDays-day_program")
            .document("details")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // User has already registered a program
                    activity?.runOnUiThread {
                        Toast.makeText(context, "You have already enrolled in this program", Toast.LENGTH_SHORT).show()
                    }
                    programAlreadyRegistered = true
                    navigateToNextFragment()
                } else {
                    // User has not registered a program, save details
                    saveProgramAndNavigate(programDetails, durationDays)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error checking program details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProgramAndNavigate(programDetails: Map<String, Any>, durationDays: Int) {
        // Show progress dialog
        showProgressDialog()

        // Save program details
        firestore.collection("user_programs").document(userId)
            .collection("$durationDays-day_program")
            .document("details")
            .set(programDetails)
            .addOnSuccessListener {
                // Delay to ensure progress dialog is shown for at least 4 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    hideProgressDialog()
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Program details saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    navigateToNextFragment()
                    startCurrentDayUpdateHandler()
                }, 4000) // 4000 milliseconds = 4 seconds delay
            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Toast.makeText(context, "Error saving program details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToNextFragment() {
        // Use Safe Args to pass programDuration to the next fragment
        val action = if (programDuration == 30) R.id.action_FirstFragment_to_SecondFragment else R.id.action_FirstFragment_to_SecondFragment2
        findNavController().navigate(action, Bundle().apply {
            putInt("programDuration", programDuration)
        })
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage("Enrolling you for this program...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun startCurrentDayUpdateHandler() {
        updateCurrentDayHandler = Handler(Looper.getMainLooper())
        updateCurrentDayRunnable = object : Runnable {
            override fun run() {
                // Calculate current day
                calculateCurrentDay()
                // Repeat every day
                updateCurrentDayHandler?.postDelayed(this, 24 * 60 * 60 * 1000) // 24 hours delay
            }
        }
        // Start the handler immediately
        updateCurrentDayHandler?.post(updateCurrentDayRunnable!!)
    }

    private fun calculateCurrentDay() {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM,HH:mm:ss", Locale.getDefault())

        // Retrieve program start date from Firestore
        programDetailsDocRef = firestore.collection("user_programs").document(userId)
            .collection("$programDuration-day_program")
            .document("details")

        programDetailsDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val startDateString = documentSnapshot.getString("startDate")
                    if (startDateString != null) {
                        val startDate = Calendar.getInstance()
                        startDate.time = dateFormat.parse(startDateString)!!

                        // Calculate current day
                        val startDayOfYear = startDate.get(Calendar.DAY_OF_YEAR)
                        val currentDayOfYear = today.get(Calendar.DAY_OF_YEAR)
                        val currentDay = currentDayOfYear - startDayOfYear + 1

                        // Update current day in Firestore
                        programDetailsDocRef.update("currentDay", currentDay)
                            .addOnSuccessListener {
                                // Update local cache or UI if needed
                                // For example, update UI with currentDay
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to update current day: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error retrieving program details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        hideProgressDialog()

        // Stop the update handler if running
        updateCurrentDayHandler?.removeCallbacks(updateCurrentDayRunnable!!)
    }
}
