package com.example.aurafit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aurafit.authentication.LoginActivity
import com.example.aurafit.databinding.ActivityMainBinding
import com.example.aurafit.drawer.AboutActivity
import com.example.aurafit.drawer.CommunitySupportActivity
import com.example.aurafit.drawer.HelpSupportActivity
import com.example.aurafit.drawer.SettingsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setSupportActionBar(mainBinding.appBarMain.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, mainBinding.drawerLayout, mainBinding.appBarMain.toolbar,
            R.string.OpenDrawer, R.string.CloseDrawer
        )

        mainBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Get current user
        val currentUser = Firebase.auth.currentUser

        // Load user data from Firestore
        currentUser?.uid?.let { userId ->
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val name = document.getString("name")
                    val email = document.getString("email")
                    val photoUrl = document.getString("photoUrl")

                    // Set data to header layout
                    val headerView = mainBinding.navigationView.getHeaderView(0)
                    val profileImage = headerView.findViewById<ImageView>(R.id.profile_image)
                    val usernameTV = headerView.findViewById<TextView>(R.id.usernameTV)
                    val emailTV = headerView.findViewById<TextView>(R.id.emailTV)

                    // Update views
                    usernameTV.text = name
                    emailTV.text = email

                    // Load profile image using Glide
                    Glide.with(this)
                        .load(photoUrl) // Load profile image URL from Firestore
                        .placeholder(R.drawable.img) // Placeholder image while loading
                        .error(R.drawable.img) // Error image if loading fails
                        .into(profileImage)
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }

        mainBinding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_friends_community -> {
                    startActivity(Intent(this, CommunitySupportActivity::class.java))
                    true
                }
                R.id.nav_help_support -> {
                    startActivity(Intent(this, HelpSupportActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                R.id.nav_sign_out -> {
                    FirebaseAuth.getInstance().signOut()
                    clearUserDataAndStartLoginActivity()
                    true
                }

                else -> false
            }
        }
    }

    private fun clearUserDataAndStartLoginActivity() {
        // Clear all data related to user session
        // For example: SharedPreferences, local database, etc.

        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Sign out from Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            // Start LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

