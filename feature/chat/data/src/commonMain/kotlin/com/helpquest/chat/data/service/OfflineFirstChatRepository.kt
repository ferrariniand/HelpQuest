package com.helpquest.chat.data.service

import com.helpquest.chat.data.mappers.toChat
import com.helpquest.chat.data.mappers.toChatEntity
import com.helpquest.chat.data.mappers.toChatInfo
import com.helpquest.chat.data.mappers.toLastMessageView
import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatInfo
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.data.mappers.toParticipantEntity
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.database.entities.chat.ChatInfoEntity
import com.helpquest.core.database.entities.chat.ChatWithParticipants
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class OfflineFirstChatRepository(
    private val database: HelpQuestDatabase,
    private val chatService: ChatService,
) : ChatRepository {
    override fun getChats(): Flow<List<Chat>> {
        return database.chatDao.getChatsWithParticipants()
            .map { allChatWithParticipants ->
                supervisorScope {
                    allChatWithParticipants
                        .map { chatWithParticipants ->
                            async {
                                ChatWithParticipants(
                                    chat = chatWithParticipants.chat,
                                    participants = chatWithParticipants
                                        .participants
                                        .onlyActive(chatWithParticipants.chat.chatId),
                                    lastMessage = chatWithParticipants.lastMessage
                                )
                            }
                        }
                        .awaitAll()
                        .map { it.toChat() }
                }
            }
    }

    override fun getChatInfoById(chatId: String): Flow<ChatInfo> {
        return database.chatDao.getChatInfoById(chatId)
            .filterNotNull()
            .map { chatInfo ->
                ChatInfoEntity(
                    chat = chatInfo.chat,
                    participants = chatInfo.participants.onlyActive(chatInfo.chat.chatId),
                    messagesWithSenders = chatInfo.messagesWithSenders
                )
            }
            .map { it.toChatInfo() }
    }

    override fun getActiveParticipantsByChatId(chatId: String): Flow<List<Participant>> {
        return database.chatDao.getActiveParticipantsByChatId(chatId)
            .map { participants ->
                participants.map { it.toParticipant() }
            }
    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return chatService
            .getChats()
            .onSuccess { chats ->
                val chatsWithParticipants = chats.map { chat ->
                    ChatWithParticipants(
                        chat = chat.toChatEntity(),
                        participants = chat.participants.map { it.toParticipantEntity() },
                        lastMessage = chat.lastMessage?.toLastMessageView()
                    )
                }

                database.chatDao.upsertChatsWithParticipantsAndCrossRefs(
                    chats = chatsWithParticipants,
                    participantDao = database.participantDao,
                    crossRefDao = database.chatParticipantsCrossRefDao,
                    messageDao = database.chatMessageDao
                )
            }
    }

    override suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .getChatById(chatId)
            .onSuccess { chat ->
                database.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toChatEntity(),
                    participants = chat.participants.map { it.toParticipantEntity() },
                    participantDao = database.participantDao,
                    crossRefDao = database.chatParticipantsCrossRefDao
                )
            }
            .asEmptyResult()
    }


    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return chatService
            .createChat(otherUserIds)
            .onSuccess { chat ->
                database.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toChatEntity(),
                    participants = chat.participants.map { it.toParticipantEntity() },
                    participantDao = database.participantDao,
                    crossRefDao = database.chatParticipantsCrossRefDao
                )
            }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .leaveChat(chatId)
            .onSuccess {
                database.chatDao.deleteChatById(chatId)
            }
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> {
        return chatService
            .addParticipantsToChat(chatId, userIds)
            .onSuccess { chat ->
                database.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toChatEntity(),
                    participants = chat.participants.map { it.toParticipantEntity() },
                    participantDao = database.participantDao,
                    crossRefDao = database.chatParticipantsCrossRefDao
                )
            }
    }

    private suspend fun List<ParticipantEntity>.onlyActive(chatId: String): List<ParticipantEntity> {
        val activeParticipantIds = database
            .chatDao
            .getActiveParticipantsByChatId(chatId)
            .first()
            .map { it.userId }
        return this.filter { it.userId in activeParticipantIds }
    }
}