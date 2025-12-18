package com.helpquest.core.database

import android.content.Context
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<HelpQuestDatabase> =
        createAndroidDatabase(context, HelpQuestDatabase.DB_NAME)
}