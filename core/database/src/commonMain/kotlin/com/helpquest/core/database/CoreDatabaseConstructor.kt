package com.helpquest.core.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CoreDatabaseConstructor : RoomDatabaseConstructor<CoreDatabase> {
    override fun initialize(): CoreDatabase
}