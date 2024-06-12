package com.example.aurafit.authentication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.MainActivity
import com.example.aurafit.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        // Initialize FirebaseAuth instance and progress dialog
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        progressDialog = ProgressDialog(this)

        if (auth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        signUpBinding.btnSignup.setOnClickListener {
            val username = signUpBinding.inputNameET.text.toString()
            val email = signUpBinding.inputEmailET.text.toString().trim()
            val password = signUpBinding.inputPasswordET.text.toString().trim()

            if (TextUtils.isEmpty(username)) {
                signUpBinding.inputNameET.error = "Username is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                signUpBinding.inputEmailET.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                signUpBinding.inputPasswordET.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < 6) {
                signUpBinding.inputPasswordET.error = "Password must be greater than or equal to 6 characters"
                return@setOnClickListener
            }

            progressDialog.setMessage("Creating account...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "Account created successfully", Toast.LENGTH_SHORT).show()

                    val userID = auth.currentUser?.uid
                    val user = hashMapOf(
                        "name" to username,
                        "email" to email,
                        "password" to password
                    )
                    userID?.let {
                        firestore.collection("users").document(it)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "User profile is created for $userID")
                            }
                    }

                    val intent = Intent(applicationContext, DataConfirmationActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signUpBinding.loginTV.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        signUpBinding.inputNameET.setText("")
        signUpBinding.inputEmailET.setText("")
        signUpBinding.inputPasswordET.setText("")
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}
