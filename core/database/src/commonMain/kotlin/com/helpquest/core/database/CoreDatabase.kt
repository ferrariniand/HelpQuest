package com.helpquest.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.helpquest.core.database.dao.ParticipantDao
import com.helpquest.core.database.entities.ParticipantEntity

@Database(
    entities = [
        ParticipantEntity::class,
    ],
    version = 1,
)
@ConstructedBy(CoreDatabaseConstructor::class)
abstract class CoreDatabase : RoomDatabase() {
    abstract val participantDao: ParticipantDao

    companion object {
        const val DB_NAME = "helpquest.participant.db"
    }
}