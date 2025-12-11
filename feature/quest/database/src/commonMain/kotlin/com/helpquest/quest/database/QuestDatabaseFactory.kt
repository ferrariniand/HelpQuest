package com.helpquest.quest.database

import androidx.room.RoomDatabase

expect class QuestDatabaseFactory {
    fun create(): RoomDatabase.Builder<QuestDatabase>
}