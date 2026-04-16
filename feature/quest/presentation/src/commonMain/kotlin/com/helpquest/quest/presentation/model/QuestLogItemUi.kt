package com.helpquest.quest.presentation.model


import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.quest.domain.models.QuestActivity

data class QuestLogItemUi(
    val questId: String,
    val localParticipant: ParticipantUi,
    val otherParticipants: List<ParticipantUi>,
    val lastActivity: QuestActivity?,
    val lastActivityActorUsername: String?
)
