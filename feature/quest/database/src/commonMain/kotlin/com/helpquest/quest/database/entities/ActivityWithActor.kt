package com.helpquest.quest.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ActivityWithActor(
    @Embedded
    val activity: QuestActivityEntity,
    @Relation(
        parentColumn = "actorId",
        entityColumn = "userId"
    )
    val actor: QuestParticipantEntity
)
