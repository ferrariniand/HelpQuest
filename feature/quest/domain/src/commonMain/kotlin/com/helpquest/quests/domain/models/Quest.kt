@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.domain.models

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.GeoLocation
import com.helpquest.core.domain.models.Participant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Quest(
    val questId: String,
    val questTitle: String,
    val questDescription: String,
    val questCreatorId: String,
    val createdAt: Instant,
    val location: GeoLocation,
    val questCategory: Category?,
    val participants: List<Participant>,
    val questStatus: QuestStatus?,
    val lastActivity: QuestActivity?,
    val lastActivityActorUsername: String? = null,
    val lastUpdateAt: Instant,
)