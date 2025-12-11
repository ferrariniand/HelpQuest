package com.helpquest.quest.database

import android.content.Context
import androidx.room.RoomDatabase
import com.helpquest.core.database.createAndroidDatabase

actual class QuestDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<QuestDatabase> =
        createAndroidDatabase(context, QuestDatabase.DB_NAME)

}