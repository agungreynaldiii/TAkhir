package com.example.ayosehat.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayosehat.api.ApiService
import com.example.ayosehat.api.OpenAIRequestBody
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messages = mutableStateListOf<Message>()

    fun sendMessage(text: String, isUser: Boolean = true) {
        messages.add(Message(text, "user"))
        if (isUser) {
            if (containsKeyword(text)) {
                viewModelScope.launch {
                    val response = ApiService.openAIApi.generateResponse(OpenAIRequestBody(messages = messages))
                    messages.add(response.choices.first().message)
                }
            } else {
                // Jika
                // n tanggapan default atau tidak melakukan apa-apa
                println("GAGAL")
            }
        }
    }

    private fun containsKeyword(text: String): Boolean {
        val keywords = listOf("Gizi", "Makanan", "kalori", "Karbohidrat", "resep", "kandungan", "protein", "lemak", "serat", "vitamin", "mineral", "kesehatan", "penyakit", "mengatasi", "menghindari", "sehat", "sakit", "penyakit") // Tentukan keyword yang diinginkan
        return keywords.any { keyword -> text.contains(keyword, ignoreCase = true) }
    }
}

data class Message(val content: String, val role: String) {
    val isUser: Boolean
        get() = role == "user"
}

