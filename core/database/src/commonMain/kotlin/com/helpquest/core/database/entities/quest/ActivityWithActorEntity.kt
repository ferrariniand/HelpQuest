package com.helpquest.core.database.entities.quest

import androidx.room.Embedded
import androidx.room.Relation
import com.helpquest.core.database.entities.ParticipantEntity

data class ActivityWithActorEntity(
    @Embedded
    val activity: QuestActivityEntity,
    @Relation(
        parentColumn = "actorId",
        entityColumn = "userId"
    )
    val actor: ParticipantEntity
)
