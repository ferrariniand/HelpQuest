package com.helpquest.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.helpquest.core.database.dao.ParticipantDao
import com.helpquest.core.database.dao.chat.ChatDao
import com.helpquest.core.database.dao.chat.ChatMessageDao
import com.helpquest.core.database.dao.chat.ChatParticipantsCrossRefDao
import com.helpquest.core.database.dao.quest.QuestActivityDao
import com.helpquest.core.database.dao.quest.QuestLogDao
import com.helpquest.core.database.dao.quest.QuestParticipantsCrossRefDao
import com.helpquest.core.database.db_view.LastActivityView
import com.helpquest.core.database.db_view.LastMessageView
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.database.entities.chat.ChatEntity
import com.helpquest.core.database.entities.chat.ChatMessageEntity
import com.helpquest.core.database.entities.chat.ChatParticipantCrossRef
import com.helpquest.core.database.entities.quest.QuestActivityEntity
import com.helpquest.core.database.entities.quest.QuestEntity
import com.helpquest.core.database.entities.quest.QuestParticipantCrossRef

@Database(
    entities = [
        ParticipantEntity::class,
        ChatEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class,
        QuestEntity::class,
        QuestActivityEntity::class,
        QuestParticipantCrossRef::class,
    ],
    views = [
        LastMessageView::class,
        LastActivityView::class
    ],
    version = 1,
)
@ConstructedBy(DatabaseConstructor::class)
abstract class HelpQuestDatabase : RoomDatabase() {
    abstract val participantDao: ParticipantDao
    abstract val chatDao: ChatDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao
    abstract val questLogDao: QuestLogDao
    abstract val questActivityDao: QuestActivityDao
    abstract val questParticipantsCrossRefDao: QuestParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "helpquest.db"
    }
}