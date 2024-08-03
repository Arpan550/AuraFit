package com.example.aurafit.ui.drawer.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.ChatAdapter
import com.example.aurafit.databinding.ActivityChatSupportBinding
import com.example.aurafit.ui.viewmodel.ChatViewModel
import com.example.aurafit.ui.viewmodel.ChatViewModelFactory

class ChatSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatSupportBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var progressDialog: ProgressDialog // Use ProgressDialog

    private val chatViewModel: ChatViewModel by viewModels { ChatViewModelFactory(this) }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerViewChat
        val editTextMessage = binding.editTextMessage
        val buttonSendMessage = binding.buttonSendMessage

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(mutableListOf())
        recyclerView.adapter = adapter

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Typing...")

        chatViewModel.messages.observe(this, Observer { messages ->
            adapter.updateMessages(messages)
            binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
        })

        chatViewModel.isTyping.observe(this, Observer { isTyping ->
            if (isTyping) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

        buttonSendMessage.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            chatViewModel.sendMessage(messageText)
            editTextMessage.setText("")
        }
    }
}
