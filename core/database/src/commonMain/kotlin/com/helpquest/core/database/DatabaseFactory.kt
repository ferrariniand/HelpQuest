package com.helpquest.core.database

import androidx.room.RoomDatabase


expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<HelpQuestDatabase>
}