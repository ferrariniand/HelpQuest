package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result


interface ChatService {
    suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote>

    suspend fun getChats(): Result<List<Chat>, DataError.Remote>
    suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote>
}