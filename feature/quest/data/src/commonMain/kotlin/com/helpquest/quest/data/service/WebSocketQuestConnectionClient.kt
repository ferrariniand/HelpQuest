package com.helpquest.quest.data.service


import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.quest.data.dto.websocket.IncomingQuestWebSocketDto
import com.helpquest.quest.data.dto.websocket.IncomingQuestWebSocketType
import com.helpquest.quest.data.mappers.toQuestActivity
import com.helpquest.quest.data.mappers.toQuestActivityEntity
import com.helpquest.quest.domain.service.QuestConnectionClient
import com.helpquest.quest.domain.service.QuestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class WebSocketQuestConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val questRepository: QuestRepository,
    private val database: HelpQuestDatabase,
    private val json: Json,
    private val applicationScope: CoroutineScope
) : QuestConnectionClient {

    override val questActivities = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(it) }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingQuestWebSocketDto.NewActivityDto>()
        .mapNotNull {
            database.questActivityDao.getActivityById(it.id)?.toQuestActivity()
        }
        .shareIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000)
        )

    override val connectionState = webSocketConnector.connectionState

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingQuestWebSocketDto? {
        return when (message.type) {
            IncomingQuestWebSocketType.NEW_ACTIVITY.name -> {
                json.decodeFromString<IncomingQuestWebSocketDto.NewActivityDto>(message.payload)
            }

            IncomingQuestWebSocketType.ACTIVITY_DELETED.name -> {
                json.decodeFromString<IncomingQuestWebSocketDto.ActivityDeletedDto>(message.payload)
            }

            IncomingQuestWebSocketType.QUEST_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingQuestWebSocketDto.QuestParticipantsChangedDto>(message.payload)
            }

            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingQuestWebSocketDto) {
        when (message) {
            is IncomingQuestWebSocketDto.QuestParticipantsChangedDto -> refreshQuest(message)
            is IncomingQuestWebSocketDto.ActivityDeletedDto -> deleteActivity(message)
            is IncomingQuestWebSocketDto.NewActivityDto -> handleNewActivity(message)
        }
    }

    private suspend fun refreshQuest(message: IncomingQuestWebSocketDto.QuestParticipantsChangedDto) {
        questRepository.fetchQuestById(message.questId)
    }

    private suspend fun deleteActivity(message: IncomingQuestWebSocketDto.ActivityDeletedDto) {
        database.questActivityDao.deleteActivityById(message.activityId)
    }

    private suspend fun handleNewActivity(message: IncomingQuestWebSocketDto.NewActivityDto) {
        val questExists = database.questLogDao.getQuestById(message.questId) != null
        if (!questExists) {
            questRepository.fetchQuestById(message.questId)
        }

        val entity = message.toQuestActivityEntity()
        database.questLogDao.updateLastUpdateTimestamp(
            message.questId,
            entity.lastActivityUpdateTimestamp
        )
        database.questActivityDao.upsertActivity(entity)
    }
}