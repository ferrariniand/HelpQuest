package com.helpquest.core.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object DatabaseConstructor : RoomDatabaseConstructor<HelpQuestDatabase> {
    override fun initialize(): HelpQuestDatabase
}