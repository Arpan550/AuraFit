package com.example.aurafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aurafit.R
import com.example.aurafit.model.MessageModel
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(private var messageList: MutableList<MessageModel>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutResId = if (viewType == MessageType.USER.ordinal) {
            R.layout.item_message_user
        } else {
            R.layout.item_message_bot
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].isUser) {
            MessageType.USER.ordinal
        } else {
            MessageType.BOT.ordinal
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        private val textViewTimestamp: TextView? = itemView.findViewById(R.id.textViewTimestamp)

        fun bind(message: MessageModel) {
            textViewMessage.text = message.message
            textViewTimestamp?.let {
                it.text = dateFormat.format(message.timestamp)
                it.visibility = View.VISIBLE
            }
        }
    }

    enum class MessageType {
        USER,
        BOT
    }

    fun updateMessages(newMessages: List<MessageModel>) {
        messageList.clear()
        messageList.addAll(newMessages)
        notifyDataSetChanged()
    }
}
