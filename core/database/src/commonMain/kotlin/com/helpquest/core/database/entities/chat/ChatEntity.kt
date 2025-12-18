package com.helpquest.core.database.entities.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val lastActivityTimestamp: Long
)