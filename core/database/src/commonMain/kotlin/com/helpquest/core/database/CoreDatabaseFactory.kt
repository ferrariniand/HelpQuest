package com.helpquest.core.database

import androidx.room.RoomDatabase


expect class CoreDatabaseFactory {
    fun create(): RoomDatabase.Builder<CoreDatabase>
}