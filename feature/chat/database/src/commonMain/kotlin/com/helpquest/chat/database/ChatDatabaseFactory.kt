package com.helpquest.chat.database

import androidx.room.RoomDatabase

expect class ChatDatabaseFactory {
    fun create(): RoomDatabase.Builder<ChatDatabase>
}