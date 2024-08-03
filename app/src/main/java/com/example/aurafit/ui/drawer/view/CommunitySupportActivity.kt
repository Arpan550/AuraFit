package com.example.aurafit.ui.drawer.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.PostAdapter
import com.example.aurafit.databinding.ActivityCommunitySupportBinding
import com.example.aurafit.model.PostModel
import com.example.aurafit.ui.drawer.viewmodel.CommunitySupportViewModel

class CommunitySupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunitySupportBinding
    private lateinit var postAdapter: PostAdapter
    private val viewModel: CommunitySupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize PostAdapter with an empty mutable list
        postAdapter = PostAdapter(this, mutableListOf())
        binding.verticalRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CommunitySupportActivity)
            adapter = postAdapter
        }

        // Observe ViewModel
        viewModel.posts.observe(this) { posts ->
            postAdapter.updatePosts(posts)
        }

        // Set up New Post button
        binding.btnNewPost.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }
    }
}
