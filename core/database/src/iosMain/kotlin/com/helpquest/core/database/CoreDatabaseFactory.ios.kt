package com.helpquest.core.database

import androidx.room.RoomDatabase

actual class CoreDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<CoreDatabase> =
        createIosDatabase(CoreDatabase.DB_NAME)

}