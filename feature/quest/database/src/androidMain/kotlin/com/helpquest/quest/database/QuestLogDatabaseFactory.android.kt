package com.helpquest.quest.database

import android.content.Context
import androidx.room.RoomDatabase
import com.helpquest.core.database.createAndroidDatabase

actual class QuestLogDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<QuestLogDatabase> =
        createAndroidDatabase(context, QuestLogDatabase.DB_NAME)

}