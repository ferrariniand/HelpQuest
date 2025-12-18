package com.helpquest.core.database

import android.content.Context
import androidx.room.RoomDatabase

actual class CoreDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<CoreDatabase> =
        createAndroidDatabase(context, CoreDatabase.DB_NAME)
}