package com.example.aurafit.drawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.PostAdapter
import com.example.aurafit.databinding.ActivityCommunitySupportBinding
import com.example.aurafit.model.PostModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunitySupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunitySupportBinding
    private lateinit var postAdapter: PostAdapter
    private val postList: ArrayList<PostModel> = ArrayList()
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        postAdapter = PostAdapter(this, postList)
        binding.verticalRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CommunitySupportActivity)
            adapter = postAdapter
        }

        // Set up New Post button
        binding.btnNewPost.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }

        // Fetch data from Firestore and populate postList
        fetchPostsFromFirestore()
    }

    private fun fetchPostsFromFirestore() {
        db.collection("userPosts")
            .get()
            .addOnSuccessListener { querySnapshot ->
                postList.clear() // Clear existing data

                for (document in querySnapshot.documents) {
                    val caption = document.getString("caption") ?: ""
                    val userId = document.getString("userID") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""

                    // Fetch user details using userId
                    fetchUserDetails(userId, caption, imageUrl)
                }
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.e("CommunitySupportActivity", "Error fetching posts: $e")
            }
    }

    private fun fetchUserDetails(userId: String, caption: String, imageUrl: String) {
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: ""
                    val photoUrl = document.getString("photoUrl") ?: ""

                    val postModel = PostModel(name, caption, photoUrl, imageUrl)
                    postList.add(postModel)

                    // Notify adapter of data changes after all updates
                    postAdapter.notifyDataSetChanged()
                } else {
                    Log.d("CommunitySupportActivity", "No such document for user ID: $userId")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CommunitySupportActivity", "Error fetching user details: $exception")
            }
    }
}
