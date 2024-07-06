package com.example.aurafit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.aurafit.R
import com.example.aurafit.bottom_nav_fragments.mental_fitness.MeditationPlayerActivity
import com.example.aurafit.model.MeditationSession

class MeditationAdapter(private val context: Context, private val sessions: List<MeditationSession>) : BaseAdapter() {

    override fun getCount(): Int = sessions.size

    override fun getItem(position: Int): Any = sessions[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_meditation_session, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val session = sessions[position]
        viewHolder.titleTextView.text = session.name
        viewHolder.descriptionTextView.text = session.description

        viewHolder.playImageView.setOnClickListener {
            val intent = Intent(context, MeditationPlayerActivity::class.java).apply {
                putExtra("media_file_path", session.filePath)
            }
            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val playImageView: ImageView = view.findViewById(R.id.playImageView)
    }
}
