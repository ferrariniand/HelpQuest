package com.helpquest.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.helpquest.core.database.util.appDataDirectory
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<HelpQuestDatabase> {
        val directory = appDataDirectory

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val dbFile = File(directory, HelpQuestDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}