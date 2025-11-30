package com.helpquest.chat.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val lastActivityTimestamp: Long
)
