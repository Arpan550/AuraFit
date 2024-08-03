package com.example.aurafit.ui.drawer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aurafit.model.PostModel
import com.example.aurafit.repository.PostRepository
import kotlinx.coroutines.launch

class CommunitySupportViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository = PostRepository()
    private val _posts = MutableLiveData<List<PostModel>>()
    val posts: LiveData<List<PostModel>> get() = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                val fetchedPosts = postRepository.getPosts()
                _posts.value = fetchedPosts
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
