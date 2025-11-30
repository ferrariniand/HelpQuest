package com.helpquest.chat.database

import androidx.room.RoomDatabase
import com.helpquest.core.database.createIosDatabase

actual class ChatDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChatDatabase> =
        createIosDatabase(ChatDatabase.DB_NAME)

}