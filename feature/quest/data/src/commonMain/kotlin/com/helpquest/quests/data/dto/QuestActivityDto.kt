package com.helpquest.quests.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestActivityDto(
    val activityId: String,
    val questId: String,
    val creatorId: String,
    val actorId: String?,
    val content: String,
    val activityStatus: String,
    val startTimestamp: String,
    val endTimestamp: String?
)