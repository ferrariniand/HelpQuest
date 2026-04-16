@file:OptIn(ExperimentalTime::class)

package com.helpquest.quest.data.mappers

import com.helpquest.core.data.mappers.toGeoLocation
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.database.entities.quest.ActivityWithCreatorEntity
import com.helpquest.core.database.entities.quest.QuestEntity
import com.helpquest.core.database.entities.quest.QuestInfoEntity
import com.helpquest.core.database.entities.quest.QuestWithParticipants
import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.GeoLocation
import com.helpquest.core.domain.models.Participant
import com.helpquest.quest.data.dto.QuestDto
import com.helpquest.quest.domain.models.ActivityWithCreator
import com.helpquest.quest.domain.models.Quest
import com.helpquest.quest.domain.models.QuestActivity
import com.helpquest.quest.domain.models.QuestActivityStatus
import com.helpquest.quest.domain.models.QuestInfo
import com.helpquest.quest.domain.models.QuestStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun QuestDto.toQuest(): Quest {
    val lastActivityActorUsername = lastActivity?.let { activity ->
        participants.find { it.userId == activity.actorId }?.username
    }
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
        lastActivityActorUsername = lastActivityActorUsername,
        lastUpdateAt = Instant.parse(lastUpdateAt)
    )
}

fun QuestEntity.toQuest(
    participants: List<Participant>,
    lastActivity: QuestActivity? = null
): Quest {
    val lastActivityActorUsername = lastActivity?.let { activity ->
        participants.find { it.userId == activity.actorId }?.username
    }
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
        lastActivity = lastActivity,
        lastActivityActorUsername = lastActivityActorUsername,
        lastUpdateAt = Instant.fromEpochMilliseconds(lastUpdateTimestamp),
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
        lastActivityActorUsername = lastActivity?.actorUsername,
        lastUpdateAt = Instant.fromEpochMilliseconds(quest.lastUpdateTimestamp),
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
        questStatus = questStatus?.name,
        lastUpdateTimestamp = lastUpdateAt.toEpochMilliseconds()
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