package com.helpquest.quest.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = QuestEntity::class,
            parentColumns = ["questId"],
            childColumns = ["questId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("questId"),
        Index("startTimestamp"),
    ]
)
data class QuestActivityEntity(
    @PrimaryKey
    val activityId: String,
    val questId: String,
    val actorId: String,
    val content: String,
    val activityStatus: String,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
