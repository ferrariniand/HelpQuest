package com.helpquest.quests.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingQuestWebSocketType {
    NEW_ACTIVITY
}

@Serializable
sealed interface OutgoingQuestWebSocketDto {

    @Serializable
    data class NewActivity(
        val questId: String,
        val activityId: String,
        val content: String,
        val type: OutgoingQuestWebSocketType = OutgoingQuestWebSocketType.NEW_ACTIVITY
    ) : OutgoingQuestWebSocketDto
}