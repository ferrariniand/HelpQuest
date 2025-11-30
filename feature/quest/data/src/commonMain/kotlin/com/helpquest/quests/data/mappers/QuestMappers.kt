@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.data.mappers

import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.domain.models.Category
import com.helpquest.quest.database.entities.QuestEntity
import com.helpquest.quest.database.entities.QuestWithParticipants
import com.helpquest.quests.data.dto.QuestDto
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun QuestDto.toQuest(): Quest {
    return Quest(
        questId = questId,
        questTitle = questTitle,
        questDescription = questDescription,
        questCreatorId = questCreatorId,
        createdAt = Instant.parse(createdAt),
        participants = participants.map { it.toParticipant() },
        questCategory = questCategory?.let {
            Category.valueOf(it)
        },
        questStatus = questStatus?.let {
            QuestStatus.valueOf(it)
        },
        lastActivity = lastActivity?.toQuestActivity(),
    )
}

fun QuestWithParticipants.toQuest(): Quest {
    return Quest(
        questId = quest.questId,
        questTitle = quest.questTitle,
        questDescription = quest.questDescription,
        questCreatorId = quest.questCreatorId,
        createdAt = Instant.fromEpochMilliseconds(quest.createdTimestamp),
        participants = participants.map { it.toParticipant() },
        questCategory = quest.questCategory?.let {
            Category.valueOf(it)
        },
        questStatus = quest.questStatus?.let {
            QuestStatus.valueOf(it)
        },
        lastActivity = lastActivity?.toQuestActivity(),

        )
}

fun Quest.toQuestEntity(): QuestEntity {
    return QuestEntity(
        questId = questId,
        questTitle = questTitle,
        questDescription = questDescription,
        questCreatorId = questCreatorId,
        createdTimestamp = createdAt.toEpochMilliseconds(),
        questCategory = questCategory?.name,
        questStatus = questStatus?.name
    )
}