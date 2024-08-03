package com.example.aurafit.ui.authentication.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.ui.main.MainActivity
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // Initialize firebase auth
        auth = FirebaseAuth.getInstance()

        // Email password authentication
        var progressDialog = ProgressDialog(this).apply {
            setMessage("Logging in...")
            setCancelable(false)
        }
        if (auth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        // Forgot Password handler
        loginBinding.forgotPasswordTV.setOnClickListener {
            val dialogView = LayoutInflater.from(it.context).inflate(R.layout.dialog_reset_password, null)
            val resetMail: EditText = dialogView.findViewById(R.id.input_emailET)

            AlertDialog.Builder(it.context).apply {
                setView(dialogView)
                setPositiveButton("Yes") { dialog, _ ->
                    val progressDialog = ProgressDialog(it.context).apply {
                        setMessage("Sending reset link...")
                        show()
                    }

                    // Extract the email and send reset link
                    // Inside the 'Forgot Password handler' block
                    val mail = resetMail.text.toString()
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener { task ->
                        progressDialog.dismiss() // Dismiss progress dialog
                        if (task.isSuccessful) {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Reset link successfully sent to your email",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            // Password reset link sent successfully, handle Firestore update
                            val user = auth.currentUser
                            user?.uid?.let { userId ->
                                val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

                                // Here, you can update any password-related information in Firestore
                                userRef.update("passwordReset", true) // For example, update a flag indicating password reset
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Password reset flag updated successfully in Firestore")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Error updating password reset flag in Firestore", e)
                                    }
                            }
                        } else {
                            val errorMessage = task.exception?.message ?: "Unknown error occurred"
                            if (task.exception is FirebaseAuthInvalidUserException) {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "User does not exist. Please check the email address.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    errorMessage,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }
                setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                create().show()
            }
        }

        loginBinding.btnLogin.setOnClickListener {
            val email = loginBinding.inputEmailET.text.toString()
            val password = loginBinding.inputPasswordET.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                loginBinding.inputEmailET.error = "Email Id is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                loginBinding.inputPasswordET.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < 6) {
                loginBinding.inputPasswordET.error = "Password must be greater than or equal to 6 characters"
                return@setOnClickListener
            }

            progressDialog.show()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this, "Logged In successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Initialize Google Authentication
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize the launcher
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                manageResults(task.result)
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }

        loginBinding.btnSigninGoogle.setOnClickListener {
            googleSignIn()
        }

        loginBinding.signupTV.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun googleSignIn() {
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private fun manageResults(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener(this) { signInTask ->
                if (signInTask.isSuccessful) {
                    val user = auth.currentUser
                    val userDetails = hashMapOf(
                        "name" to account.displayName,
                        "email" to account.email,
                        "photoUrl" to account.photoUrl?.toString()
                    )

                    // Save user details to Firestore
                    user?.uid?.let { userId ->
                        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                        userRef.set(userDetails)
                            .addOnSuccessListener {
                                Log.d(TAG, "User details saved successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error saving user details", e)
                            }
                    }

                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        loginBinding.inputEmailET.setText("")
        loginBinding.inputPasswordET.setText("")
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
