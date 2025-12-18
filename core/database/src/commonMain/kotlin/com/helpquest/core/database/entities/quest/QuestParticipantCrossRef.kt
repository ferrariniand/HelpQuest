package com.helpquest.core.database.entities.quest

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.helpquest.core.database.entities.ParticipantEntity

@Entity(
    primaryKeys = ["questId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = QuestEntity::class,
            parentColumns = ["questId"],
            childColumns = ["questId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParticipantEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["questId"]),
        Index(value = ["userId"]),
    ]
)
data class QuestParticipantCrossRef(
    val questId: String,
    val userId: String,
    val isActive: Boolean
)