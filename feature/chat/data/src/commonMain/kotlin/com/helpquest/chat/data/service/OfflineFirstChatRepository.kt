package com.helpquest.chat.data.service

import com.helpquest.chat.data.mappers.toChat
import com.helpquest.chat.data.mappers.toChatEntity
import com.helpquest.chat.data.mappers.toChatParticipantEntity
import com.helpquest.chat.data.mappers.toLastMessageView
import com.helpquest.chat.database.ChatDatabase
import com.helpquest.chat.database.entities.ChatWithParticipants
import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstChatRepository(
    private val chatService: ChatService,
    private val db: ChatDatabase
) : ChatRepository {
    override fun getChats(): Flow<List<Chat>> {
        return db.chatDao.getChatsWithActiveParticipants()
            .map { chatWithParticipantsList ->
                chatWithParticipantsList.map { it.toChat() }
            }
    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return chatService
            .getChats()
            .onSuccess { chats ->
                val chatsWithParticipants = chats.map { chat ->
                    ChatWithParticipants(
                        chat = chat.toChatEntity(),
                        participants = chat.participants.map { it.toChatParticipantEntity() },
                        lastMessage = chat.lastMessage?.toLastMessageView()
                    )
                }

                db.chatDao.upsertChatsWithParticipantsAndCrossRefs(
                    chats = chatsWithParticipants,
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantsCrossRefDao,
                    messageDao = db.chatMessageDao
                )
            }
    }
}