package com.helpquest.chat.data.service


import com.helpquest.chat.data.dto.ChatMessageDto
import com.helpquest.chat.data.mappers.toChatMessage
import com.helpquest.chat.data.message.ChatMessageConstants
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.service.ChatMessageService
import com.helpquest.core.data.networking.get
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatMessageService(
    private val httpClient: HttpClient
) : ChatMessageService {
    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError.Remote> {
        return httpClient.get<List<ChatMessageDto>>(
            route = "/chat/$chatId/messages",
            queryParams = buildMap {
                this["pageSize"] = ChatMessageConstants.PAGE_SIZE
                if (before != null) {
                    this["before"] = before
                }
            }
        ).map {
            it.map { messageDto ->
                messageDto.toChatMessage()
            }
        }
    }
}