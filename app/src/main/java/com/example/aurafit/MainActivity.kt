package com.example.aurafit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aurafit.authentication.LoginActivity
import com.example.aurafit.bottom_nav_fragments.AnalyticsFragment
import com.example.aurafit.bottom_nav_fragments.MentalHealthFragment
import com.example.aurafit.bottom_nav_fragments.PhysicalHealthFragment
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
            this,
            mainBinding.drawerLayout,
            mainBinding.appBarMain.toolbar,
            R.string.OpenDrawer,
            R.string.CloseDrawer
        )

        mainBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val currentUser = Firebase.auth.currentUser


        currentUser?.uid?.let { userId ->
            db.collection("users").document(userId).get().addOnSuccessListener { document ->
                    val name = document.getString("name")
                    val email = document.getString("email")
                    val photoUrl = document.getString("photoUrl")


                    val headerView = mainBinding.navigationView.getHeaderView(0)
                    val profileImage = headerView.findViewById<ImageView>(R.id.profile_image)
                    val usernameTV = headerView.findViewById<TextView>(R.id.usernameTV)
                    val emailTV = headerView.findViewById<TextView>(R.id.emailTV)


                    usernameTV.text = name
                    emailTV.text = email


                    Glide.with(this).load(photoUrl)
                        .placeholder(R.drawable.img)
                        .error(R.drawable.img)
                        .into(profileImage)
                }.addOnFailureListener { e ->

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



        if (savedInstanceState == null) {
            loadFragment(AnalyticsFragment())
            mainBinding.appBarMain.contentMain.bottomNavigation.selectedItemId=R.id.nav_analytics
        }

        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
       mainBinding.appBarMain.contentMain.bottomNavigation.setOnItemSelectedListener { menuItem ->
           when(menuItem.itemId) {
               R.id.nav_physical_health -> {
                   loadFragment(PhysicalHealthFragment())
                   true
               }
               R.id.nav_mental_health -> {
                   loadFragment(MentalHealthFragment())
                   true
               }
               R.id.nav_analytics -> {
                   loadFragment(AnalyticsFragment())
                   true
               }
               else -> false
           }
       }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(mainBinding.appBarMain.contentMain.navHostFragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun clearUserDataAndStartLoginActivity() {

        FirebaseAuth.getInstance().signOut()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

