package com.helpquest.quest.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object QuestDatabaseConstructor : RoomDatabaseConstructor<QuestDatabase> {
    override fun initialize(): QuestDatabase
}