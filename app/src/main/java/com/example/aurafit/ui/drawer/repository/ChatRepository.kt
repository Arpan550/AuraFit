package com.example.aurafit.data

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import org.json.JSONObject
import java.io.IOException

class ChatRepository(private val context: Context) {

    private fun readApiKey(): String? {
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

    suspend fun generateBotResponse(userMessage: String): String {
        val apiKey = readApiKey()
        return if (apiKey != null) {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = apiKey
            )
            val response = generativeModel.generateContent(userMessage)
            response.text.toString()
        } else {
            "API key not found"
        }
    }
}
