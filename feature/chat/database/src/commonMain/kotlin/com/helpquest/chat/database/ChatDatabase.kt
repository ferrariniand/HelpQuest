package com.helpquest.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.helpquest.chat.database.dao.ChatDao
import com.helpquest.chat.database.dao.ChatMessageDao
import com.helpquest.chat.database.dao.ChatParticipantDao
import com.helpquest.chat.database.dao.ChatParticipantsCrossRefDao
import com.helpquest.chat.database.db_view.LastMessageView
import com.helpquest.chat.database.entities.ChatEntity
import com.helpquest.chat.database.entities.ChatMessageEntity
import com.helpquest.chat.database.entities.ChatParticipantCrossRef
import com.helpquest.chat.database.entities.ChatParticipantEntity

@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class,
    ],
    views = [
        LastMessageView::class
    ],
    version = 1,
)
@ConstructedBy(ChatDatabaseConstructor::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "helpquest.chat.db"
    }
}