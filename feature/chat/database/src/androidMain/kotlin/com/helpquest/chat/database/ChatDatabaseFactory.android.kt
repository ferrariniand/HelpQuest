package com.helpquest.chat.database

import android.content.Context
import androidx.room.RoomDatabase
import com.helpquest.core.database.createAndroidDatabase

actual class ChatDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<ChatDatabase> =
        createAndroidDatabase(context, ChatDatabase.DB_NAME)
}