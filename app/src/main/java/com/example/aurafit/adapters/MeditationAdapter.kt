package com.example.aurafit.adapters


import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.aurafit.R
import com.example.aurafit.model.Meditation
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MeditationAdapter(context: Context, private val meditations: List<Meditation>) :
    ArrayAdapter<Meditation>(context, R.layout.item_meditation, meditations) {

    private var mediaPlayer: MediaPlayer? = null
    private var lastPlayedPosition: Int = -1
    private var loadingDialog: ProgressDialog? = null
    private var playbackDialog: ProgressDialog? = null
    private var durationTextView: TextView? = null
    private var seekBar: SeekBar? = null
    private var playPauseImageView: ImageView? = null
    private var isPlaying: Boolean = false
    private var isPaused: Boolean = false
    private var playbackHandler: Handler? = null
    private val updateProgressRunnable = Runnable { updateProgress() }

    // Visualizer variables
    private var visualizer: Visualizer? = null
    private var visualizerEnabled: Boolean = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_meditation, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        val playImageView = view.findViewById<ImageView>(R.id.playImageView)

        val meditation = getItem(position)
        titleTextView.text = meditation?.title
        descriptionTextView.text = meditation?.description

        playImageView.setOnClickListener {
            // Play audio for the clicked meditation
            meditation?.let {
                showLoadingDialog()
                fetchAndPrepareAudio(it.audioUrl, position)
            }
        }

        return view
    }

    private fun fetchAndPrepareAudio(audioFileName: String, position: Int) {
        val storage = FirebaseStorage.getInstance()
        val audioRef: StorageReference = storage.reference.child("meditation_audios/$audioFileName")

        audioRef.downloadUrl.addOnSuccessListener { uri ->
            prepareAudio(uri.toString(), position)
        }.addOnFailureListener {
            // Handle any errors
            it.printStackTrace()
            dismissLoadingDialog()
        }
    }

    private fun prepareAudio(audioUrl: String, position: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    it.start()
                    this@MeditationAdapter.isPlaying = true
                    isPaused = false
                    lastPlayedPosition = position
                    dismissLoadingDialog()
                    showPlaybackDialog(meditations[position])
                    updateProgress()
                }
                setOnCompletionListener {
                    this@MeditationAdapter.isPlaying = false
                    isPaused = false
                    dismissPlaybackDialog()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissLoadingDialog()
            }
        }
    }

    private fun showLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = ProgressDialog(context)
        loadingDialog?.apply {
            setMessage("Loading Audio...")
            setCancelable(false)
            show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun showPlaybackDialog(meditation: Meditation) {
        playbackDialog?.dismiss()
        playbackDialog = ProgressDialog(context)
        playbackDialog?.apply {
            setMessage("Playing ${meditation.title}")
            setCancelable(false)
            show()
        }
    }

    private fun dismissPlaybackDialog() {
        playbackDialog?.dismiss()
        playbackDialog = null
    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
        isPaused = true
        playPauseImageView?.setImageResource(R.drawable.ic_play_arrow)
        playbackHandler?.removeCallbacks(updateProgressRunnable)
    }

    private fun resumeAudio() {
        mediaPlayer?.start()
        isPlaying = true
        isPaused = false
        playPauseImageView?.setImageResource(R.drawable.ic_pause)
        updateProgress()
    }

    private fun updateProgress() {
        mediaPlayer?.let {
            durationTextView?.text = formatDuration(it.currentPosition.toLong()) + "/" + formatDuration(it.duration.toLong())
            seekBar?.max = it.duration
            seekBar?.progress = it.currentPosition
        }
        playbackHandler = Handler()
        playbackHandler?.postDelayed(updateProgressRunnable, 1000)
    }

    private fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        // Reset last played position when data set changes
        lastPlayedPosition = -1
    }

    override fun getItem(position: Int): Meditation? {
        return super.getItem(position)
    }

    override fun getCount(): Int {
        return super.getCount()
    }
}
