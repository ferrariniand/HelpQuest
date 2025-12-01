package com.helpquest.quests.data.service


import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quest.database.QuestLogDatabase
import com.helpquest.quest.database.entities.QuestWithParticipants
import com.helpquest.quests.data.mappers.toLastActivityView
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.data.mappers.toQuestEntity
import com.helpquest.quests.data.mappers.toQuestInfo
import com.helpquest.quests.data.mappers.toQuestParticipantEntity
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestInfo
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.domain.service.QuestService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class OfflineFirstQuestRepository(
    private val questService: QuestService,
    private val db: QuestLogDatabase
) : QuestRepository {
    override fun getQuestLog(): Flow<List<Quest>> {
        return db.questLogDao.getQuestsWithActiveParticipants()
            .map { questWithParticipantsList ->
                questWithParticipantsList.map { it.toQuest() }
            }
    }

    //TODO ??? MAYBE TO BE MERGED WITH QUESTLOG:
    // QUESTLOG: Filter the quests assigned to the user
    // QUESTBOARD: Get all quests in state OPEN or REQUEST_MORE_HELP
    override fun getQuestBoard(): Flow<List<Quest>> {
        TODO("Not yet implemented")
    }

    override fun getQuestInfoById(questId: String): Flow<QuestInfo> {
        return db.questLogDao.getQuestInfoById(questId)
            .filterNotNull()
            .map { it.toQuestInfo() }
    }

    override suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote> {
        return questService
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

    //TODO ??? MAYBE TO BE MERGED WITH QUESTLOG
    override suspend fun fetchQuestBoard(): Result<List<Quest>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchQuestById(questId: String): EmptyResult<DataError.Remote> {
        return questService
            .getQuestById(questId)
            .onSuccess { quest ->
                db.questLogDao.upsertQuestWithParticipantsAndCrossRefs(
                    quest = quest.toQuestEntity(),
                    participants = quest.participants.map { it.toQuestParticipantEntity() },
                    participantDao = db.questParticipantDao,
                    crossRefDao = db.questParticipantsCrossRefDao
                )
            }
            .asEmptyResult()
    }
}