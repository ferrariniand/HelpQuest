package com.helpquest.quest.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.helpquest.quest.database.dao.QuestActivityDao
import com.helpquest.quest.database.dao.QuestLogDao
import com.helpquest.quest.database.dao.QuestParticipantDao
import com.helpquest.quest.database.dao.QuestParticipantsCrossRefDao
import com.helpquest.quest.database.db_view.LastActivityView
import com.helpquest.quest.database.entities.QuestActivityEntity
import com.helpquest.quest.database.entities.QuestEntity
import com.helpquest.quest.database.entities.QuestParticipantCrossRef
import com.helpquest.quest.database.entities.QuestParticipantEntity

@Database(
    entities = [
        QuestEntity::class,
        QuestParticipantEntity::class,
        QuestActivityEntity::class,
        QuestParticipantCrossRef::class,
    ],
    views = [
        LastActivityView::class
    ],
    version = 1,
)
@ConstructedBy(QuestLogDatabaseConstructor::class)
abstract class QuestLogDatabase : RoomDatabase() {
    abstract val questLogDao: QuestLogDao
    abstract val questParticipantDao: QuestParticipantDao
    abstract val questActivityDao: QuestActivityDao
    abstract val questParticipantsCrossRefDao: QuestParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "helpquest.questlog.db"
    }
}