package com.helpquest.quest.database

import androidx.room.RoomDatabase
import com.helpquest.core.database.createIosDatabase

actual class QuestLogDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<QuestLogDatabase> =
        createIosDatabase(QuestLogDatabase.DB_NAME)
}