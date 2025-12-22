package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.core.domain.service.ConnectionClient
import com.helpquest.core.domain.util.ConnectionError
import com.helpquest.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ChatConnectionClient : ConnectionClient {
    val chatMessages: Flow<ChatMessage>
    suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError>
}