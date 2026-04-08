package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.core.domain.util.ConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class MockChatConnectionClient : ChatConnectionClient {

    override val chatMessages = flowOf<ChatMessage>()

    override val connectionState = MutableStateFlow(ConnectionState.CONNECTED)
}