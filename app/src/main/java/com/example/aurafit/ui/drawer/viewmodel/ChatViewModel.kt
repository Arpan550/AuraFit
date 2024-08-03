package com.example.aurafit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aurafit.data.ChatRepository
import com.example.aurafit.model.MessageModel
import kotlinx.coroutines.launch
import java.util.Date

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _messages = MutableLiveData<MutableList<MessageModel>>()
    val messages: LiveData<MutableList<MessageModel>> get() = _messages

    private val _isTyping = MutableLiveData<Boolean>()
    val isTyping: LiveData<Boolean> get() = _isTyping

    init {
        _messages.value = mutableListOf(
            MessageModel("Welcome here! How can I help you?", false, Date())
        )
    }

    fun sendMessage(userMessage: String) {
        if (userMessage.isNotEmpty()) {
            val newMessage = MessageModel(userMessage, true, Date())
            _messages.value?.add(newMessage)
            _messages.postValue(_messages.value)

            _isTyping.value = true
            viewModelScope.launch {
                val botResponse = repository.generateBotResponse(userMessage)
                _isTyping.value = false

                val botMessage = MessageModel(botResponse, false, Date())
                _messages.value?.add(botMessage)
                _messages.postValue(_messages.value)
            }
        }
    }
}
