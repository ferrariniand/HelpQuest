package com.helpquest.quests.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingQuestWebSocketType {
    NEW_ACTIVITY
}

@Serializable
sealed class OutgoingQuestWebSocketDto(
    val type: OutgoingQuestWebSocketType
) {

    @Serializable
    data class NewActivity(
        val questId: String,
        val activityId: String,
        val content: String
    ) : OutgoingQuestWebSocketDto(OutgoingQuestWebSocketType.NEW_ACTIVITY)
}