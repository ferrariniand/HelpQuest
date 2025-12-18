package com.helpquest.core.database

import androidx.room.RoomDatabase

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<HelpQuestDatabase> =
        createIosDatabase(HelpQuestDatabase.DB_NAME)

}