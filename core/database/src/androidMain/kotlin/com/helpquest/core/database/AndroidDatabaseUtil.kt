package com.helpquest.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

inline fun <reified T : RoomDatabase> createAndroidDatabase(
    context: Context,
    dbName: String
): RoomDatabase.Builder<T> {
    val dbFile = context.applicationContext.getDatabasePath(dbName)

    return Room.databaseBuilder<T>(
        context.applicationContext,
        dbFile.absolutePath
    )
}