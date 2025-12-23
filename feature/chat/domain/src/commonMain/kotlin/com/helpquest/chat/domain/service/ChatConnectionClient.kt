package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.core.domain.service.ConnectionClient
import kotlinx.coroutines.flow.Flow

interface ChatConnectionClient : ConnectionClient {
    val chatMessages: Flow<ChatMessage>
}