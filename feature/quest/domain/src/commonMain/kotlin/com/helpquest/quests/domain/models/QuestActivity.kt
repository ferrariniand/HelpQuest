@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


//TODO TO DEFINE THE USAGE AND THE PROPERTIES OF THE CLASS
enum class QuestActivityStatus {
    CREATING,
    OPEN,
    IN_PROGRESS,
    ON_HOLD,
    COMPLETED,
    DECLINED,
    ERROR
}

data class QuestActivity(
    val activityId: String,
    val questId: String,
    val creatorId: String,
    val actorId: String?,
    val content: String,
    val activityStatus: QuestActivityStatus,
    val startActivityAt: Instant,
    val lastActivityUpdateAt: Instant,
    val endActivityAt: Instant? = null
)