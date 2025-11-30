package com.helpquest.quest.database

import androidx.room.RoomDatabase

expect class QuestLogDatabaseFactory {
    fun create(): RoomDatabase.Builder<QuestLogDatabase>
}