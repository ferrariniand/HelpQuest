package com.helpquest.quests.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingQuestWebSocketType {
    NEW_ACTIVITY,
    ACTIVITY_DELETED,
    QUEST_PARTICIPANTS_CHANGED,
}

@Serializable
sealed class IncomingQuestWebSocketDto(
    val type: IncomingQuestWebSocketType
) {

    @Serializable
    data class NewActivityDto(
        val id: String,
        val questId: String,
        val content: String,
        val actorId: String,
        val activityStatus: String,
        val startActivityAt: String,
        val endActivityAt: String? = null
    ) : IncomingQuestWebSocketDto(IncomingQuestWebSocketType.NEW_ACTIVITY)

    @Serializable
    data class ActivityDeletedDto(
        val activityId: String,
        val questId: String
    ) : IncomingQuestWebSocketDto(IncomingQuestWebSocketType.ACTIVITY_DELETED)

    @Serializable
    data class QuestParticipantsChangedDto(
        val questId: String
    ) : IncomingQuestWebSocketDto(IncomingQuestWebSocketType.QUEST_PARTICIPANTS_CHANGED)
}