@file:OptIn(ExperimentalForeignApi::class)

package com.helpquest.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

inline fun <reified T : RoomDatabase> createIosDatabase(
    dbName: String
): RoomDatabase.Builder<T> {

    val dbFile = documentDirectory() + "/$dbName"

    return Room.databaseBuilder<T>(dbFile)
}

fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return requireNotNull(documentDirectory?.path)
}