package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise.receiver.MidnightUpdateReceiver
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
    private lateinit var programDetailsDocRef: DocumentReference
    private var updateCurrentDayHandler: Handler? = null
    private var updateCurrentDayRunnable: Runnable? = null

    private lateinit var sharedPref: SharedPreferences
    private var programDuration: Int = 0

    companion object {
        const val ACTION_UPDATE_CURRENT_DAY = "com.example.aurafit.ACTION_UPDATE_CURRENT_DAY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.btnStart30.setOnClickListener {
            startProgram("Fitness Challenge", 30)
        }

        binding.btnStart60.setOnClickListener {
            startProgram("Strength Journey", 60)
        }

        binding.about30.setOnClickListener {
            val dialog = Day30ProgramDialogFragment()
            dialog.show(parentFragmentManager, "30DayProgramDialog")
        }

        binding.abou60.setOnClickListener {
            val dialog = Day60ProgramDialogFragment()
            dialog.show(parentFragmentManager, "60DayProgramDialog")
        }
    }

    private fun startProgram(programName: String, durationDays: Int) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

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

            with(sharedPref.edit()) {
                putString("programStartDate", startDateFormatted)
                putInt("programDuration", durationDays)
                apply()
            }

            programDuration = durationDays

            checkProgramRegistration(userId, durationDays, programDetails)
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkProgramRegistration(userId: String, durationDays: Int, programDetails: Map<String, Any>) {
        firestore.collection("user_programs").document(userId)
            .collection("$durationDays-day_program")
            .document("details")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "You have already enrolled in this program", Toast.LENGTH_SHORT).show()
                    }
                    navigateToNextFragment()
                } else {
                    saveProgramAndNavigate(programDetails, durationDays)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error checking program details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProgramAndNavigate(programDetails: Map<String, Any>, durationDays: Int) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Saving Program")
        progressDialog.setMessage("Please wait while we save your program details...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        firestore.collection("user_programs").document(auth.currentUser!!.uid)
            .collection("$durationDays-day_program")
            .document("details")
            .set(programDetails)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Program details saved successfully", Toast.LENGTH_SHORT).show()
                navigateToNextFragment()
                startCurrentDayUpdateHandler()
                setupMidnightUpdate()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "Error saving program details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun navigateToNextFragment() {
        val action = if (programDuration == 30) R.id.action_FirstFragment_to_SecondFragment else R.id.action_FirstFragment_to_SecondFragment2
        findNavController().navigate(action, Bundle().apply {
            putInt("programDuration", programDuration)
        })
    }

    private fun startCurrentDayUpdateHandler() {
        updateCurrentDayHandler = Handler(Looper.getMainLooper())
        updateCurrentDayRunnable = object : Runnable {
            override fun run() {
                updateCurrentDay()
                updateCurrentDayHandler?.postDelayed(this, 24 * 60 * 60 * 1000)
            }
        }
        updateCurrentDayHandler?.post(updateCurrentDayRunnable!!)
    }

    private fun updateCurrentDay() {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM,HH:mm:ss", Locale.getDefault())
        val startDateString = sharedPref.getString("programStartDate", null)
        val programDuration = sharedPref.getInt("programDuration", 0)

        if (startDateString != null && programDuration > 0) {
            val startDate = Calendar.getInstance()
            startDate.time = dateFormat.parse(startDateString)!!

            val startDayOfYear = startDate.get(Calendar.DAY_OF_YEAR)
            val currentDayOfYear = today.get(Calendar.DAY_OF_YEAR)
            val currentDay = currentDayOfYear - startDayOfYear + 1

            val userId = auth.currentUser?.uid

            if (userId != null) {
                firestore.collection("user_programs")
                    .document(userId)
                    .collection("$programDuration-day_program")
                    .document("details")
                    .update("currentDay", currentDay)
                    .addOnSuccessListener {
                        // Check for program completion
                        if (currentDay >= programDuration) {
                            completeProgram(userId, programDuration)
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to update current day: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun completeProgram(userId: String, programDuration: Int) {
        // Remove program details from Firestore
        firestore.collection("user_programs")
            .document(userId)
            .collection("$programDuration-day_program")
            .document("details")
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Program completed and de-enrolled successfully", Toast.LENGTH_SHORT).show()
                clearLocalData()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to de-enroll from program: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearLocalData() {
        // Clear SharedPreferences and any other local data related to the program
        with(sharedPref.edit()) {
            remove("programStartDate")
            remove("programDuration")
            apply()
        }
    }

    private fun setupMidnightUpdate() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val midnight = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1) // Set for tomorrow midnight
        }

        val intent = Intent(context, MidnightUpdateReceiver::class.java)
        intent.action = ACTION_UPDATE_CURRENT_DAY

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            midnight.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        updateCurrentDayHandler?.removeCallbacks(updateCurrentDayRunnable!!)
    }
}
