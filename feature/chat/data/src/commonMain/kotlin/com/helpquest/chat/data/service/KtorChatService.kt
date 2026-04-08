package com.helpquest.chat.data.service

import com.helpquest.chat.data.dto.ChatDto
import com.helpquest.chat.data.dto.requests.CreateChatRequest
import com.helpquest.chat.data.dto.requests.ParticipantsRequest
import com.helpquest.chat.data.mappers.toChat
import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.data.networking.hqDelete
import com.helpquest.core.data.networking.hqGet
import com.helpquest.core.data.networking.hqPost
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatService(
    private val httpClient: HttpClient
) : ChatService {

    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.hqPost<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map { it.toChat() }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.hqGet<List<ChatDto>>(
            route = "/chat"
        ).map { chatDtos ->
            chatDtos.map { it.toChat() }
        }
    }


    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.hqGet<ChatDto>(
            route = "/chat/$chatId"
        ).map { it.toChat() }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return httpClient.hqDelete<Unit>(
            route = "/chat/$chatId/leave"
        ).asEmptyResult()
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> {
        return httpClient.hqPost<ParticipantsRequest, ChatDto>(
            route = "/chat/$chatId/add",
            body = ParticipantsRequest(
                userIds = userIds
            )
        ).map { it.toChat() }
    }
}