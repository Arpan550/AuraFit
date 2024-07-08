package com.example.aurafit.adapters

import android.app.Activity
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.model.music.Data

class MusicRecyclerViewAdapter(
    private val context: Activity,
    private val dataList: List<Data>,
    private val mediaPlayer: MediaPlayer
) : RecyclerView.Adapter<MusicRecyclerViewAdapter.MyViewHolder>() {

    private var currentPlayingPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = dataList[position]
        holder.bind(currentData)

        // Check if this item is currently playing
        if (position == currentPlayingPosition) {
            // Highlight or animate the currently playing item
            holder.itemView.setBackgroundResource(R.drawable.bg_playing_item) // Example: Change background
        } else {
            holder.itemView.setBackgroundResource(android.R.color.transparent) // Reset background
        }

        // Play button click listener
        holder.play.setOnClickListener {
            playMusic(currentData.preview)
            currentPlayingPosition = holder.adapterPosition // Update current playing position
            notifyDataSetChanged() // Update UI to reflect the change
        }

        // Pause button click listener
        holder.pause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                currentPlayingPosition = -1 // Reset current playing position
                notifyDataSetChanged() // Update UI to reflect the change
            }
        }
    }


    private fun playMusic(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicImage: ImageView = itemView.findViewById(R.id.music_image)
        val musicTitle: TextView = itemView.findViewById(R.id.music_title)
        val musicDescription: TextView = itemView.findViewById(R.id.music_description)
        val play: ImageButton = itemView.findViewById(R.id.btn_play)
        val pause: ImageButton = itemView.findViewById(R.id.btn_pause)

        fun bind(data: Data) {
            musicTitle.text = data.title
            musicDescription.text = data.artist.name

            // Load image using Glide
            Glide.with(context)
                .load(data.album.cover_medium)
                .into(musicImage)
        }
    }
}
