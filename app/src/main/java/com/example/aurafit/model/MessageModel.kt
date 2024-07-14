package com.example.aurafit.model

import java.util.Date

data class MessageModel(
    val message: String,
    val isUser: Boolean,
    val timestamp: Date,
    val isTyping: Boolean = false
)

