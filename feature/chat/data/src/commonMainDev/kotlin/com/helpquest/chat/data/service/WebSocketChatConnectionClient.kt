package com.helpquest.chat.data.service

import com.helpquest.chat.data.dto.websocket.IncomingChatWebSocketDto
import com.helpquest.chat.data.dto.websocket.IncomingChatWebSocketType
import com.helpquest.chat.data.mappers.toChatMessage
import com.helpquest.chat.data.mappers.toChatMessageEntity
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class WebSocketChatConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: HelpQuestDatabase,
    private val json: Json,
    private val applicationScope: CoroutineScope
) : ChatConnectionClient {

    override val chatMessages = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(it) }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingChatWebSocketDto.NewMessageDto>()
        .mapNotNull {
            database.chatMessageDao.getMessageById(it.id)?.toChatMessage()
        }
        .shareIn(
            applicationScope,
            SharingStarted.Companion.WhileSubscribed(5000)
        )

    override val connectionState = webSocketConnector.connectionState

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingChatWebSocketDto? {
        return when (message.type) {
            IncomingChatWebSocketType.NEW_MESSAGE.name -> {
                json.decodeFromString<IncomingChatWebSocketDto.NewMessageDto>(message.payload)
            }

            IncomingChatWebSocketType.MESSAGE_DELETED.name -> {
                json.decodeFromString<IncomingChatWebSocketDto.MessageDeletedDto>(message.payload)
            }

            IncomingChatWebSocketType.CHAT_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingChatWebSocketDto.ChatParticipantsChangedDto>(message.payload)
            }

            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingChatWebSocketDto) {
        when (message) {
            is IncomingChatWebSocketDto.ChatParticipantsChangedDto -> refreshChat(message)
            is IncomingChatWebSocketDto.MessageDeletedDto -> deleteMessage(message)
            is IncomingChatWebSocketDto.NewMessageDto -> handleNewMessage(message)
        }
    }

    private suspend fun refreshChat(message: IncomingChatWebSocketDto.ChatParticipantsChangedDto) {
        chatRepository.fetchChatById(message.chatId)
    }

    private suspend fun deleteMessage(message: IncomingChatWebSocketDto.MessageDeletedDto) {
        database.chatMessageDao.deleteMessageById(message.messageId)
    }

    private suspend fun handleNewMessage(message: IncomingChatWebSocketDto.NewMessageDto) {
        val chatExists = database.chatDao.getChatById(message.chatId) != null
        if (!chatExists) {
            chatRepository.fetchChatById(message.chatId)
        }

        val entity = message.toChatMessageEntity()
        database.chatDao.updateLastActivity(entity.chatId, entity.timestamp)
        database.chatMessageDao.upsertMessage(entity)
    }
}