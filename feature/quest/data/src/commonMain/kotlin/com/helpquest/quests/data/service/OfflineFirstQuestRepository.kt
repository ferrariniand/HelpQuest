package com.helpquest.quests.data.service


import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.data.mappers.toParticipantEntity
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.database.entities.quest.QuestInfoEntity
import com.helpquest.core.database.entities.quest.QuestWithParticipants
import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quests.data.dto.QuestDtoConstants
import com.helpquest.quests.data.mappers.toLastActivityView
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.data.mappers.toQuestEntity
import com.helpquest.quests.data.mappers.toQuestInfo
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestInfo
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.domain.service.QuestService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class OfflineFirstQuestRepository(
    private val questService: QuestService,
    private val database: HelpQuestDatabase
) : QuestRepository {
    override fun getQuestLog(): Flow<List<Quest>> {
        return database.questLogDao.getQuestsWithParticipants()
            .map { allQuestWithParticipants ->
                supervisorScope {
                    allQuestWithParticipants
                        .map { questWithParticipants ->
                            async {
                                QuestWithParticipants(
                                    quest = questWithParticipants.quest,
                                    participants = questWithParticipants
                                        .participants
                                        .onlyActive(questWithParticipants.quest.questId),
                                    lastActivity = questWithParticipants.lastActivity
                                )
                            }
                        }
                        .awaitAll()
                        .map { it.toQuest() }
                }
            }
    }

    //TODO ??? MAYBE TO BE MERGED WITH QUESTLOG:
    // QUESTLOG: Filter the quests assigned to the user
    // QUESTBOARD: Get all quests in state OPEN or REQUEST_MORE_HELP
    override fun getQuestBoard(): Flow<List<Quest>> {
        return database.questBoardDao.getQuestsWithParticipantsByStatus("OPEN")
            .map { allQuestWithParticipants ->
                supervisorScope {
                    allQuestWithParticipants
                        .map { questWithParticipants ->
                            async {
                                QuestWithParticipants(
                                    quest = questWithParticipants.quest,
                                    participants = questWithParticipants
                                        .participants
                                        .onlyActive(questWithParticipants.quest.questId),
                                    lastActivity = questWithParticipants.lastActivity
                                )
                            }
                        }
                        .awaitAll()
                        .map { it.toQuest() }
                }
            }
    }

    override fun getQuestInfoById(questId: String): Flow<QuestInfo> {
        return database.questLogDao.getQuestInfoById(questId)
            .filterNotNull()
            .map { questInfo ->
                QuestInfoEntity(
                    quest = questInfo.quest,
                    participants = questInfo.participants.onlyActive(questInfo.quest.questId),
                    activitiesWithCreators = questInfo.activitiesWithCreators
                )
            }
            .map { it.toQuestInfo() }
    }

    override suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote> {
        return questService
            .getQuestLog()
            .onSuccess { quests ->
                val questsWithParticipants = quests.map { quest ->
                    QuestWithParticipants(
                        quest = quest.toQuestEntity(),
                        participants = quest.participants.map { it.toParticipantEntity() },
                        lastActivity = quest.lastActivity?.toLastActivityView()
                    )
                }

                database.questLogDao.upsertQuestsWithParticipantsAndCrossRefs(
                    quests = questsWithParticipants,
                    participantDao = database.participantDao,
                    crossRefDao = database.questParticipantsCrossRefDao,
                    activityDao = database.questActivityDao
                )
            }
    }

    override suspend fun fetchQuestBoard(
        before: String?
    ): Result<List<Quest>, DataError> {
        return questService
            .fetchQuestBoard(before)
            .onSuccess { quests ->
                return safeDatabaseUpdate {
                    database.questBoardDao.upsertQuestsAndSyncIfNecessary(
                        serverQuests = quests.map { it.toQuestEntity() },
                        pageSize = QuestDtoConstants.PAGE_SIZE,
                        shouldSync = before == null // Only sync for most recent page
                    )
                    quests
                }
            }
    }

    override suspend fun fetchQuestById(questId: String): EmptyResult<DataError.Remote> {
        return questService
            .getQuestById(questId)
            .onSuccess { quest ->
                database.questLogDao.upsertQuestWithParticipantsAndCrossRefs(
                    quest = quest.toQuestEntity(),
                    participants = quest.participants.map { it.toParticipantEntity() },
                    participantDao = database.participantDao,
                    crossRefDao = database.questParticipantsCrossRefDao
                )
            }
            .asEmptyResult()
    }

    override suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String,
    ): Result<Quest, DataError.Remote> {
        return questService
            .createQuest(
                questTitle = questTitle,
                questDescription = questDescription,
                questCategory = questCategory,
                questCreatorId = questCreatorId,
            )
            .onSuccess { quest ->
                database.questLogDao.upsertQuestWithParticipantsAndCrossRefs(
                    quest = quest.toQuestEntity(),
                    participants = quest.participants.map { it.toParticipantEntity() },
                    participantDao = database.participantDao,
                    crossRefDao = database.questParticipantsCrossRefDao
                )
            }
    }

    override suspend fun leaveQuest(questId: String): EmptyResult<DataError.Remote> {
        return questService
            .leaveQuest(questId)
            .onSuccess {
                database.questLogDao.deleteQuestById(questId)
            }
    }

    private suspend fun List<ParticipantEntity>.onlyActive(questId: String): List<ParticipantEntity> {
        val activeParticipantIds = database
            .questLogDao
            .getActiveParticipantsByQuestId(questId)
            .first()
            .map { it.userId }
        return this.filter { it.userId in activeParticipantIds }
    }
}