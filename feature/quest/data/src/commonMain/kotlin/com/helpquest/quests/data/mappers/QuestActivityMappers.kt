@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.data.mappers


import com.helpquest.quest.database.db_view.LastActivityView
import com.helpquest.quest.database.entities.QuestActivityEntity
import com.helpquest.quests.data.dto.QuestActivityDto
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun QuestActivityDto.toQuestActivity(): QuestActivity {
    return QuestActivity(
        activityId = activityId,
        questId = questId,
        content = content,
        actorId = actorId,
        activityStatus = QuestActivityStatus.valueOf(activityStatus),
        startActivityAt = Instant.parse(startTimestamp),
        endActivityAt = endTimestamp?.let {
            Instant.parse(it)
        },
    )
}

fun LastActivityView.toQuestActivity(): QuestActivity {
    return QuestActivity(
        activityId = activityId,
        questId = questId,
        content = content,
        actorId = actorId,
        activityStatus = QuestActivityStatus.valueOf(activityStatus),
        startActivityAt = Instant.fromEpochMilliseconds(startTimestamp),
        endActivityAt = endTimestamp?.let {
            Instant.fromEpochMilliseconds(it)
        },
    )
}

fun QuestActivity.toQuestActivityEntity(): QuestActivityEntity {
    return QuestActivityEntity(
        activityId = activityId,
        questId = questId,
        actorId = actorId,
        content = content,
        activityStatus = activityStatus.name,
        startTimestamp = startActivityAt.toEpochMilliseconds(),
        endTimestamp = endActivityAt?.toEpochMilliseconds()
    )
}

fun QuestActivity.toLastActivityView(): LastActivityView {
    return LastActivityView(
        activityId = activityId,
        questId = questId,
        actorId = actorId,
        content = content,
        activityStatus = activityStatus.name,
        startTimestamp = startActivityAt.toEpochMilliseconds(),
        endTimestamp = endActivityAt?.toEpochMilliseconds()
    )
}