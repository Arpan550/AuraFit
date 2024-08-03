package com.example.aurafit.adapters

import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import com.example.aurafit.R
import com.example.aurafit.model.Meditation
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MeditationAdapter(context: Context, private val meditations: List<Meditation>) :
    ArrayAdapter<Meditation>(context, R.layout.item_meditation, meditations) {

    private var mediaPlayer: MediaPlayer? = null
    private var lastPlayedPosition: Int = -1
    private var handler: Handler? = null
    private val updateProgressRunnable = Runnable { updateProgress() }
    private var currentlyPlayingView: View? = null
    private var progressDialog: ProgressDialog? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_meditation, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        val playPauseButton = view.findViewById<AppCompatImageButton>(R.id.playImageView)
        val container = view.findViewById<LinearLayout>(R.id.container)

        val meditation = getItem(position)
        titleTextView.text = meditation?.title
        descriptionTextView.text = meditation?.description

        playPauseButton.setOnClickListener {
            if (lastPlayedPosition == position && mediaPlayer?.isPlaying == true) {
                pauseAudio(playPauseButton, container)
            } else {
                playAudio(meditation?.audioUrl, position, playPauseButton, container)
            }
        }

        // Highlight the currently playing item
        if (lastPlayedPosition == position && mediaPlayer?.isPlaying == true) {
            container.setBackgroundResource(R.drawable.border_visualizer)
            playPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            container.setBackgroundResource(0)
            playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }

        return view
    }

    private fun playAudio(audioFileName: String?, position: Int, playPauseButton: AppCompatImageButton, container: LinearLayout) {
        if (audioFileName == null) return

        // Show progress dialog
        showProgressDialog()

        // Pause currently playing audio if a new item is played
        if (lastPlayedPosition != -1 && lastPlayedPosition != position && mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            currentlyPlayingView?.findViewById<AppCompatImageButton>(R.id.playImageView)?.setImageResource(R.drawable.ic_play_arrow)
            currentlyPlayingView?.findViewById<LinearLayout>(R.id.container)?.setBackgroundResource(0)
        }

        fetchAndPrepareAudio(audioFileName, position, playPauseButton, container)
    }

    private fun fetchAndPrepareAudio(audioFileName: String, position: Int, playPauseButton: AppCompatImageButton, container: LinearLayout) {
        val storage = FirebaseStorage.getInstance()
        val audioRef: StorageReference = storage.reference.child("meditation_audios/$audioFileName")

        audioRef.downloadUrl.addOnSuccessListener { uri ->
            prepareAudio(uri.toString(), position, playPauseButton, container)
        }.addOnFailureListener {
            // Handle any errors
            it.printStackTrace()
            dismissProgressDialog()
        }
    }

    private fun prepareAudio(audioUrl: String, position: Int, playPauseButton: AppCompatImageButton, container: LinearLayout) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    it.start()
                    lastPlayedPosition = position
                    currentlyPlayingView?.findViewById<LinearLayout>(R.id.container)?.setBackgroundResource(0)
                    container.setBackgroundResource(R.drawable.border_visualizer)
                    currentlyPlayingView = container
                    playPauseButton.setImageResource(R.drawable.ic_pause)
                    handler = Handler()
                    handler?.postDelayed(updateProgressRunnable, 1000)
                    dismissProgressDialog()
                }
                setOnCompletionListener {
                    handler?.removeCallbacks(updateProgressRunnable)
                    container.setBackgroundResource(0)
                    playPauseButton.setImageResource(R.drawable.ic_play_arrow)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgressDialog()
            }
        }
    }

    private fun pauseAudio(playPauseButton: AppCompatImageButton, container: LinearLayout) {
        mediaPlayer?.pause()
        playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        container.setBackgroundResource(0) // Hide visualizer when paused
        handler?.removeCallbacks(updateProgressRunnable)
    }

    private fun updateProgress() {
        mediaPlayer?.let {
            // Update UI components related to the progress of the audio
        }
        handler?.postDelayed(updateProgressRunnable, 1000)
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context).apply {
                setMessage("Loading audio...")
                setCancelable(false)
            }
        }
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}
