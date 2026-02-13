@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.data.mappers


import com.helpquest.core.database.db_view.LastActivityView
import com.helpquest.core.database.entities.quest.QuestActivityEntity
import com.helpquest.quests.data.dto.QuestActivityDto
import com.helpquest.quests.data.dto.websocket.IncomingQuestWebSocketDto
import com.helpquest.quests.data.dto.websocket.OutgoingQuestWebSocketDto
import com.helpquest.quests.domain.models.OutgoingNewActivity
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun QuestActivityDto.toQuestActivity(): QuestActivity {
    return QuestActivity(
        activityId = activityId,
        questId = questId,
        content = content,
        creatorId = creatorId,
        actorId = actorId,
        activityStatus = QuestActivityStatus.valueOf(activityStatus),
        startActivityAt = Instant.parse(startTimestamp),
        lastActivityUpdateAt = Instant.parse(lastActivityUpdateTimestamp),
        endActivityAt = endTimestamp?.let {
            Instant.parse(it)
        },
    )
}

fun QuestActivityEntity.toQuestActivity(): QuestActivity {
    return QuestActivity(
        activityId = activityId,
        questId = questId,
        content = content,
        creatorId = creatorId,
        actorId = actorId,
        activityStatus = QuestActivityStatus.valueOf(activityStatus),
        startActivityAt = Instant.fromEpochMilliseconds(startTimestamp),
        lastActivityUpdateAt = Instant.fromEpochMilliseconds(lastActivityUpdateTimestamp),
        endActivityAt = endTimestamp?.let {
            Instant.fromEpochMilliseconds(it)
        },
    )
}

fun LastActivityView.toQuestActivity(): QuestActivity {
    return QuestActivity(
        activityId = activityId,
        questId = questId,
        content = content,
        creatorId = creatorId,
        actorId = actorId,
        activityStatus = QuestActivityStatus.valueOf(activityStatus),
        startActivityAt = Instant.fromEpochMilliseconds(startTimestamp),
        lastActivityUpdateAt = Instant.fromEpochMilliseconds(lastActivityUpdateTimestamp),
        endActivityAt = endTimestamp?.let {
            Instant.fromEpochMilliseconds(it)
        },
    )
}

fun QuestActivity.toQuestActivityEntity(): QuestActivityEntity {
    return QuestActivityEntity(
        activityId = activityId,
        questId = questId,
        creatorId = creatorId,
        actorId = actorId,
        content = content,
        activityStatus = activityStatus.name,
        startTimestamp = startActivityAt.toEpochMilliseconds(),
        lastActivityUpdateTimestamp = lastActivityUpdateAt.toEpochMilliseconds(),
        endTimestamp = endActivityAt?.toEpochMilliseconds()
    )
}

fun QuestActivity.toLastActivityView(): LastActivityView {
    return LastActivityView(
        activityId = activityId,
        questId = questId,
        creatorId = creatorId,
        actorId = actorId,
        content = content,
        activityStatus = activityStatus.name,
        startTimestamp = startActivityAt.toEpochMilliseconds(),
        lastActivityUpdateTimestamp = lastActivityUpdateAt.toEpochMilliseconds(),
        endTimestamp = endActivityAt?.toEpochMilliseconds()
    )
}

fun QuestActivity.toNewActivity(): OutgoingQuestWebSocketDto.NewActivity {
    return OutgoingQuestWebSocketDto.NewActivity(
        activityId = activityId,
        questId = questId,
        content = content,
    )
}

fun IncomingQuestWebSocketDto.NewActivityDto.toQuestActivityEntity(): QuestActivityEntity {
    return QuestActivityEntity(
        activityId = id,
        questId = questId,
        creatorId = creatorId,
        actorId = actorId,
        content = content,
        startTimestamp = Instant.parse(startActivityAt).toEpochMilliseconds(),
        lastActivityUpdateTimestamp = Instant.parse(lastActivityUpdateAt).toEpochMilliseconds(),
        endTimestamp = endActivityAt?.let {
            Instant.parse(it).toEpochMilliseconds()
        },
        activityStatus = activityStatus
    )
}

fun OutgoingNewActivity.toWebSocketNewActivityDto(): OutgoingQuestWebSocketDto.NewActivity {
    return OutgoingQuestWebSocketDto.NewActivity(
        questId = questId,
        activityId = activityId,
        content = content
    )
}

fun OutgoingNewActivity.toNewActivityEntity(
    creatorId: String,
    activityStatus: QuestActivityStatus
): QuestActivityEntity {
    return QuestActivityEntity(
        activityId = activityId,
        questId = questId,
        content = content,
        creatorId = creatorId,
        actorId = null,
        activityStatus = activityStatus.name,
        startTimestamp = Clock.System.now().toEpochMilliseconds(),
        lastActivityUpdateTimestamp = Clock.System.now().toEpochMilliseconds(),
        endTimestamp = null
    )
}
