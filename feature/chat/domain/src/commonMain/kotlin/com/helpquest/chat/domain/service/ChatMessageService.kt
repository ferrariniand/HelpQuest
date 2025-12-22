package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result


interface ChatMessageService {
    suspend fun fetchMessages(
        chatId: String,
        before: String? = null
    ): Result<List<ChatMessage>, DataError.Remote>
}