package com.helpquest.quests.presentation.model

import com.helpquest.core.domain.models.Category
import com.helpquest.core.presentation.modelsUi.Location
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestStatus
import kotlin.time.Instant

data class QuestUi(
    val questId: String,
    val questTitle: String,
    val questDescription: String,
    val questCreatorId: String,
    val createdAt: Instant,
    val location: Location,
    val questCategory: Category?,
    val participants: List<ParticipantUi>,
    val questStatus: QuestStatus?,
    val lastActivity: QuestActivity?,
)
