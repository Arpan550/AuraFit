package com.example.aurafit.drawer

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.aurafit.model.MessageModel
import com.example.aurafit.adapters.ChatAdapter
import kotlinx.coroutines.runBlocking
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageButton
import com.example.aurafit.R
import com.google.ai.client.generativeai.GenerativeModel
import org.json.JSONObject
import java.io.IOException
import java.util.*
import com.example.aurafit.databinding.ActivityHelpSupportBinding

class HelpSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpSupportBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var messageList: MutableList<MessageModel>
    private lateinit var progressDialog: ProgressDialog // Use ProgressDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerViewChat
        val editTextMessage = binding.editTextMessage
        val buttonSendMessage = binding.buttonSendMessage

        recyclerView.layoutManager = LinearLayoutManager(this)
        messageList = mutableListOf()
        adapter = ChatAdapter(messageList)
        recyclerView.adapter = adapter

        messageList.add(MessageModel("Welcome here! How can I help you?", false, Date()))
        adapter.notifyDataSetChanged()


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Typing...")

        buttonSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageText = binding.editTextMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val userMessage = MessageModel(messageText, true, Date())
            messageList.add(userMessage)
            adapter.notifyItemInserted(messageList.size - 1)
            binding.recyclerViewChat.smoothScrollToPosition(messageList.size - 1)
            binding.editTextMessage.setText("")

            progressDialog.show()

            Handler().postDelayed({
                receiveBotMessage(messageText)
            }, 1000)
        }
    }

    private fun receiveBotMessage(userMessage: String) {
        val apiKey = readApiKey(this)

        if (apiKey != null) {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = apiKey
            )

            runBlocking {
                val response = generativeModel.generateContent(userMessage)

                progressDialog.dismiss()

                val botMessage = MessageModel(response.text.toString(), false, Date())
                messageList.add(botMessage)
                adapter.notifyItemInserted(messageList.size - 1)
                binding.recyclerViewChat.smoothScrollToPosition(messageList.size - 1)
            }
        } else {
            Log.e("HelpSupportActivity", "API key not found")
        }
    }

    private fun readApiKey(context: Context): String? {
        var apiKey: String? = null
        try {
            val inputStream = context.assets.open("config.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val json = String(buffer, Charsets.UTF_8)
            val jsonObject = JSONObject(json)

            apiKey = jsonObject.getString("apiKey")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return apiKey
    }
}
