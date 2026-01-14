package com.helpquest.quests.presentation.mappers

import com.helpquest.core.presentation.mappers.toLocation
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.presentation.model.QuestUi

fun List<Quest>.toQuestUiList(): List<QuestUi> {
    return this
        .sortedByDescending { it.createdAt }
        .map { it.toQuestUi() }
}

fun Quest.toQuestUi(): QuestUi {
    return QuestUi(
        questId = questId,
        questTitle = questTitle,
        questDescription = questDescription,
        questCreatorId = questCreatorId,
        createdAt = createdAt,
        location = location.toLocation(),
        questCategory = questCategory,
        participants = participants.map { it.toParticipantUi() },
        questStatus = questStatus,
        lastActivity = lastActivity
    )
}