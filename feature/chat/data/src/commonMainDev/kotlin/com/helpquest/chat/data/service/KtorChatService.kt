package com.helpquest.chat.data.service

import com.helpquest.chat.data.dto.ChatDto
import com.helpquest.chat.data.dto.requests.CreateChatRequest
import com.helpquest.chat.data.mappers.toChat
import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.data.networking.get
import com.helpquest.core.data.networking.post
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatService(
    private val httpClient: HttpClient
) : ChatService {

    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map { it.toChat() }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDto>>(
            route = "/chat"
        ).map { chatDtos ->
            chatDtos.map { it.toChat() }
        }
    }
}