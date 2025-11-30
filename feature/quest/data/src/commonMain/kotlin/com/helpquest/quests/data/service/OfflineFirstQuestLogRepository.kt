package com.helpquest.quests.data.service


import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quest.database.QuestLogDatabase
import com.helpquest.quest.database.entities.QuestWithParticipants
import com.helpquest.quests.data.mappers.toLastActivityView
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.data.mappers.toQuestEntity
import com.helpquest.quests.data.mappers.toQuestParticipantEntity
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.service.QuestLogRepository
import com.helpquest.quests.domain.service.QuestLogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstQuestLogRepository(
    private val questLogService: QuestLogService,
    private val db: QuestLogDatabase
) : QuestLogRepository {
    override fun getQuestLog(): Flow<List<Quest>> {
        return db.questLogDao.getQuestsWithActiveParticipants()
            .map { questWithParticipantsList ->
                questWithParticipantsList.map { it.toQuest() }
            }
    }

    override suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote> {
        return questLogService
            .getQuestLog()
            .onSuccess { quests ->
                val questsWithParticipants = quests.map { quest ->
                    QuestWithParticipants(
                        quest = quest.toQuestEntity(),
                        participants = quest.participants.map { it.toQuestParticipantEntity() },
                        lastActivity = quest.lastActivity?.toLastActivityView()
                    )
                }

                db.questLogDao.upsertQuestsWithParticipantsAndCrossRefs(
                    quests = questsWithParticipants,
                    participantDao = db.questParticipantDao,
                    crossRefDao = db.questParticipantsCrossRefDao,
                    activityDao = db.questActivityDao
                )
            }
    }
}