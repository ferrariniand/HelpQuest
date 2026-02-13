package com.helpquest.core.database.entities.quest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestEntity(
    @PrimaryKey
    val questId: String,
    val questTitle: String,
    val questDescription: String,
    val questCreatorId: String,
    val createdTimestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val questCategory: String?,
    val questStatus: String?,
    val lastUpdateTimestamp: Long
)
