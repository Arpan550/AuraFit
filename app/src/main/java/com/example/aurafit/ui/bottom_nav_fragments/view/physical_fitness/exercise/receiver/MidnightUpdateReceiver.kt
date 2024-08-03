package com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise.FirstFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MidnightUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == FirstFragment.ACTION_UPDATE_CURRENT_DAY) {
            updateCurrentDay(context)
        }
    }

    private fun updateCurrentDay(context: Context?) {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM,HH:mm:ss", Locale.getDefault())
        val startDateString = sharedPref?.getString("programStartDate", null)
        val programDuration = sharedPref?.getInt("programDuration", 0)

        if (startDateString != null && programDuration != null && programDuration > 0) {
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
                            Toast.makeText(context, "Updated current day", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to update current day: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
