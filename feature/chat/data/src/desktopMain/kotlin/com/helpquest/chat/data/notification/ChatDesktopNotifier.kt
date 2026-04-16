package com.helpquest.chat.data.notification

import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.notification.DesktopNotifier
import com.helpquest.core.domain.notification.NotificationPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class ChatDesktopNotifier(
    private val chatConnectionClient: ChatConnectionClient,
    private val sessionStorage: SessionStorage,
    private val chatRepository: ChatRepository
) : DesktopNotifier {

    override fun observeNewNotifications(): Flow<NotificationPayload> {
        return combine(
            chatConnectionClient.chatMessages,
            sessionStorage.observeAuthInfo()
        ) { chatMessage, authInfo ->
            val currentUserId = authInfo?.user?.id
            if (chatMessage.senderId != currentUserId) {
                (chatMessage to currentUserId)
            } else null
        }
            .filterNotNull()
            .distinctUntilChangedBy { (message, _) -> message.id }
            .map { (message, currentUserId) ->
                val chatInfo = chatRepository.getChatInfoById(message.chatId).firstOrNull()

                val senderName = chatInfo?.chat?.participants?.find {
                    it.userId == message.senderId
                }?.username

                val notificationTitle = chatInfo?.chat?.participants?.let { participants ->
                    participants
                        .filter { it.userId != currentUserId }
                        .sortedBy { it.username }
                        .joinToString(", ") { it.username }
                }

                NotificationPayload(
                    title = notificationTitle ?: "Unknown",
                    message = "$senderName: ${message.content}"
                )
            }
    }
}