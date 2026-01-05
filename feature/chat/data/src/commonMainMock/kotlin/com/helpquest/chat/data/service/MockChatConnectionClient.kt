package com.helpquest.chat.data.service

import com.helpquest.chat.data.dto.websocket.IncomingChatWebSocketDto
import com.helpquest.chat.data.dto.websocket.IncomingChatWebSocketType
import com.helpquest.chat.data.mappers.toChatMessage
import com.helpquest.chat.data.mappers.toChatMessageEntity
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json

class MockChatConnectionClient : ChatConnectionClient {

    override val chatMessages = flowOf<ChatMessage>()

    override val connectionState = MutableStateFlow(ConnectionState.CONNECTED)
}