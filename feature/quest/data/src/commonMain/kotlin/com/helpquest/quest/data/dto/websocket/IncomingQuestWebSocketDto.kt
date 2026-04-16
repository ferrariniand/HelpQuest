package com.helpquest.quest.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingQuestWebSocketType {
    NEW_ACTIVITY,
    ACTIVITY_DELETED,
    QUEST_PARTICIPANTS_CHANGED,
}

@Serializable
sealed interface IncomingQuestWebSocketDto {

    @Serializable
    data class NewActivityDto(
        val id: String,
        val questId: String,
        val content: String,
        val creatorId: String,
        val actorId: String?,
        val activityStatus: String,
        val startActivityAt: String,
        val lastActivityUpdateAt: String,
        val endActivityAt: String? = null,
        val type: IncomingQuestWebSocketType = IncomingQuestWebSocketType.NEW_ACTIVITY
    ) : IncomingQuestWebSocketDto

    @Serializable
    data class ActivityDeletedDto(
        val activityId: String,
        val questId: String,
        val type: IncomingQuestWebSocketType = IncomingQuestWebSocketType.ACTIVITY_DELETED
    ) : IncomingQuestWebSocketDto

    @Serializable
    data class QuestParticipantsChangedDto(
        val questId: String,
        val type: IncomingQuestWebSocketType = IncomingQuestWebSocketType.QUEST_PARTICIPANTS_CHANGED
    ) : IncomingQuestWebSocketDto
}