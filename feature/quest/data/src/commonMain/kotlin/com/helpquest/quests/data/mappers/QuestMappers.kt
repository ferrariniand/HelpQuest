@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.data.mappers

import com.helpquest.core.data.mappers.toGeoLocation
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.database.entities.quest.ActivityWithCreatorEntity
import com.helpquest.core.database.entities.quest.QuestEntity
import com.helpquest.core.database.entities.quest.QuestInfoEntity
import com.helpquest.core.database.entities.quest.QuestWithParticipants
import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.GeoLocation
import com.helpquest.core.domain.models.Participant
import com.helpquest.quests.data.dto.QuestDto
import com.helpquest.quests.domain.models.ActivityWithCreator
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.models.QuestInfo
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
        location = location.toGeoLocation(),
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

fun QuestEntity.toQuest(
    participants: List<Participant>,
    lastActivity: QuestActivity? = null
): Quest {
    return Quest(
        questId = questId,
        questTitle = questTitle,
        questDescription = questDescription,
        questCreatorId = questCreatorId,
        createdAt = Instant.fromEpochMilliseconds(createdTimestamp),
        location = GeoLocation(latitude, longitude),
        participants = participants,
        questCategory = questCategory?.let {
            Category.valueOf(it)
        },
        questStatus = questStatus?.let {
            QuestStatus.valueOf(it)
        },
        lastActivity = lastActivity
    )
}

fun QuestWithParticipants.toQuest(): Quest {
    return Quest(
        questId = quest.questId,
        questTitle = quest.questTitle,
        questDescription = quest.questDescription,
        questCreatorId = quest.questCreatorId,
        createdAt = Instant.fromEpochMilliseconds(quest.createdTimestamp),
        location = GeoLocation(quest.latitude, quest.longitude),
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
        latitude = location.latitude,
        longitude = location.longitude,
        createdTimestamp = createdAt.toEpochMilliseconds(),
        questCategory = questCategory?.name,
        questStatus = questStatus?.name
    )
}

fun ActivityWithCreatorEntity.toActivityWitCreator(): ActivityWithCreator {
    return ActivityWithCreator(
        activity = activity.toQuestActivity(),
        creator = creator.toParticipant(),
        activityStatus = QuestActivityStatus.valueOf(activity.activityStatus)
    )
}

fun QuestInfoEntity.toQuestInfo(): QuestInfo {
    return QuestInfo(
        quest = quest.toQuest(
            participants = participants.map { it.toParticipant() }
        ),
        activities = activitiesWithCreators.map { it.toActivityWitCreator() }
    )
}