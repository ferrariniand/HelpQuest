package com.helpquest.quest.database

import androidx.room.RoomDatabase
import com.helpquest.core.database.createIosDatabase

actual class QuestDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<QuestDatabase> =
        createIosDatabase(QuestDatabase.DB_NAME)
}