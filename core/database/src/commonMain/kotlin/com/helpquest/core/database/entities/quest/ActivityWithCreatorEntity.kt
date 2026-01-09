package com.helpquest.core.database.entities.quest

import androidx.room.Embedded
import androidx.room.Relation
import com.helpquest.core.database.entities.ParticipantEntity

data class ActivityWithCreatorEntity(
    @Embedded
    val activity: QuestActivityEntity,
    @Relation(
        parentColumn = "creatorId",
        entityColumn = "userId"
    )
    val creator: ParticipantEntity
)
