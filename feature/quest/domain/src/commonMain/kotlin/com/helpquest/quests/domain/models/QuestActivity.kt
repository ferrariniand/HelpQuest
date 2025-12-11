@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


//TODO TO DEFINE THE USAGE AND THE PROPERTIES OF THE CLASS
enum class QuestActivityStatus {
    PENDING,
    IN_PROGRESS,
    ON_HOLD,
    COMPLETED,
    DECLINED
}

data class QuestActivity(
    val activityId: String,
    val questId: String,
    val actorId: String,
    val content: String,
    val activityStatus: QuestActivityStatus,
    val startActivityAt: Instant,
    val endActivityAt: Instant? = null
)