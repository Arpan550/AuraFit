package com.example.aurafit.adapters

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.model.music.Data // Ensure this import is correct

class MusicRecyclerViewAdapter(
    private val context: Context,
    private val musicList: List<Data>,
    private val mediaPlayer: MediaPlayer
) : RecyclerView.Adapter<MusicRecyclerViewAdapter.MusicViewHolder>() {

    private var currentPlayingPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.bind(music, position)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val musicImage: ImageView = itemView.findViewById(R.id.music_image)
        private val musicTitle: TextView = itemView.findViewById(R.id.music_title)
        private val musicDescription: TextView = itemView.findViewById(R.id.music_description)
        private val playPauseButton: ImageButton = itemView.findViewById(R.id.btn_play_pause)
        private val container: LinearLayout = itemView.findViewById(R.id.music_container)

        fun bind(music: Data, position: Int) {
            // Load music image using Glide
            Glide.with(context)
                .load(music.album.cover)
                .placeholder(R.drawable.img)
                .error(R.drawable.img)
                .into(musicImage)

            musicTitle.text = music.title
            musicDescription.text = music.artist.name

            playPauseButton.setOnClickListener {
                if (mediaPlayer.isPlaying && currentPlayingPosition == position) {
                    mediaPlayer.pause()
                    playPauseButton.setImageResource(R.drawable.ic_play_arrow)
                    container.setBackgroundResource(0) // Hide visualizer
                } else {
                    playMusic(music.preview, position)
                    playPauseButton.setImageResource(R.drawable.ic_pause)
                    container.setBackgroundResource(R.drawable.music_border_visualizer) // Show visualizer
                }
            }

            // Update the UI based on the current playing state
            if (currentPlayingPosition == position) {
                if (mediaPlayer.isPlaying) {
                    playPauseButton.setImageResource(R.drawable.ic_pause)
                    container.setBackgroundResource(R.drawable.music_border_visualizer) // Show visualizer
                } else {
                    playPauseButton.setImageResource(R.drawable.ic_play_arrow)
                    container.setBackgroundResource(0) // Hide visualizer
                }
            } else {
                playPauseButton.setImageResource(R.drawable.ic_play_arrow)
                container.setBackgroundResource(0) // Hide visualizer
            }
        }

        private fun playMusic(url: String, position: Int) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
            currentPlayingPosition = position

            // Notify the adapter to update play/pause buttons
            notifyDataSetChanged()

            mediaPlayer.setOnCompletionListener {
                currentPlayingPosition = -1
                notifyDataSetChanged()
                container.setBackgroundResource(0) // Hide visualizer
                playPauseButton.setImageResource(R.drawable.ic_play_arrow)
            }
        }
    }
}
