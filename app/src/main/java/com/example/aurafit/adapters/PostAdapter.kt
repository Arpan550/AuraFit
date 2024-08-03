package com.example.aurafit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.model.PostModel

class PostAdapter(private val context: Context, private var postList: MutableList<PostModel>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postModel = postList[position]

        // Bind data to UI elements using Glide for image loading
        holder.usernameTextView.text = postModel.username
        holder.captionTextView.text = postModel.caption
        Glide.with(context)
            .load(postModel.profile_img)
            .placeholder(R.drawable.img) // Optional placeholder image while loading
            .error(R.drawable.img) // Optional error image if loading fails
            .into(holder.profileImageView)
        Glide.with(context)
            .load(postModel.post_img)
            .placeholder(R.drawable.img) // Optional placeholder image while loading
            .error(R.drawable.img) // Optional error image if loading fails
            .into(holder.postImageView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun updatePosts(newPosts: List<PostModel>) {
        postList.clear()
        postList.addAll(newPosts)
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profile_image)
        val postImageView: ImageView = itemView.findViewById(R.id.post_image)
        val usernameTextView: TextView = itemView.findViewById(R.id.post_username)
        val captionTextView: TextView = itemView.findViewById(R.id.post_caption)
    }
}
