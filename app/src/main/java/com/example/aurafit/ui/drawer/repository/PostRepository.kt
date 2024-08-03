package com.example.aurafit.repository

import com.example.aurafit.model.PostModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    suspend fun getPosts(): List<PostModel> {
        val postList = mutableListOf<PostModel>()
        val querySnapshot = db.collection("userPosts").get().await()

        for (document in querySnapshot.documents) {
            val caption = document.getString("caption") ?: ""
            val userId = document.getString("userID") ?: ""
            val imageUrl = document.getString("imageUrl") ?: ""

            val userDetails = getUserDetails(userId)
            val postModel = PostModel(userDetails.name, caption, userDetails.photoUrl, imageUrl)
            postList.add(postModel)
        }

        return postList
    }

    private suspend fun getUserDetails(userId: String): UserDetails {
        val userRef = db.collection("users").document(userId).get().await()
        return if (userRef.exists()) {
            UserDetails(
                name = userRef.getString("name") ?: "",
                photoUrl = userRef.getString("photoUrl") ?: ""
            )
        } else {
            UserDetails("", "")
        }
    }

    data class UserDetails(val name: String, val photoUrl: String)
}
