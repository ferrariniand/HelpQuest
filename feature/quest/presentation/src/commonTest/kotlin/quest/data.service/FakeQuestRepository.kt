@file:OptIn(ExperimentalTime::class)

package quest.data.service

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.GeoLocation
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quest.domain.models.ActivityWithCreator
import com.helpquest.quest.domain.models.Quest
import com.helpquest.quest.domain.models.QuestActivity
import com.helpquest.quest.domain.models.QuestActivityStatus
import com.helpquest.quest.domain.models.QuestInfo
import com.helpquest.quest.domain.models.QuestStatus
import com.helpquest.quest.domain.service.QuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeQuestRepository : QuestRepository {

    val participant = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",

        )

    val participant2 = Participant(
        userId = "id2",
        username = "secondo",
        profilePictureUrl = "test",
    )
    val questId = Random.nextInt().toString()
    val activityId = Random.nextInt().toString()

    val activity = QuestActivity(
        activityId = activityId,
        questId = questId,
        creatorId = participant2.userId,
        actorId = participant2.userId,
        content = "test activity content",
        activityStatus = QuestActivityStatus.IN_PROGRESS,
        startActivityAt = Clock.System.now(),
        lastActivityUpdateAt = Clock.System.now(),
        endActivityAt = null
    )

    val activityWithCreator = ActivityWithCreator(
        activity = activity,
        creator = participant2,
        activityStatus = QuestActivityStatus.IN_PROGRESS,
    )
    val quest = Quest(
        questId = questId,
        questTitle = "test quest1 title",
        questDescription = "test quest1 description",
        questCreatorId = participant.userId,
        createdAt = Clock.System.now(),
        location = GeoLocation(0.0, 0.0),
        questCategory = Category.GENERIC,
        participants = listOf(participant, participant2),
        questStatus = QuestStatus.IN_PROGRESS,
        lastActivity = activity,
        lastActivityActorUsername = participant2.username,
        lastUpdateAt = Clock.System.now(),
    )

    val questInfo = QuestInfo(
        quest = quest,
        activities = listOf(activityWithCreator, activityWithCreator, activityWithCreator)
    )

    val quest2 = Quest(
        questId = Random.nextInt().toString(),
        questTitle = "test quest2 title",
        questDescription = "test quest2 description",
        questCreatorId = participant.userId,
        createdAt = Clock.System.now(),
        location = GeoLocation(0.0, 0.0),
        questCategory = Category.GENERIC,
        participants = listOf(
            participant,
        ),
        questStatus = QuestStatus.OPEN,
        lastActivity = null,
        lastActivityActorUsername = null,
        lastUpdateAt = Clock.System.now(),
    )

    val questInfo2 = QuestInfo(
        quest = quest2,
        activities = emptyList()
    )


    var questList = mutableListOf(quest, quest2)
    var questInfoList = mutableListOf(questInfo, questInfo2)

    var fetchQuestLogResult: Result<List<Quest>, DataError.Remote> =
        Result.Success(questList)

    var fetchQuestBoardResult: Result<List<Quest>, DataError.Remote> =
        Result.Success(questList)

    var fetchQuestByIdResult: EmptyResult<DataError.Remote> =
        Result.Success(quest).asEmptyResult()

    var createQuestResult: Result<Quest, DataError.Remote> =
        Result.Success(quest)

    var leaveQuestResult: EmptyResult<DataError.Remote> =
        Result.Success(quest).asEmptyResult()

    override fun getQuestLog(): Flow<List<Quest>> {
        return flowOf(questList)
    }

    override fun getQuestBoard(): Flow<List<Quest>> {
        return flowOf(questList)
    }

    override fun getQuestInfoById(questId: String): Flow<QuestInfo> {
        return flowOf(questInfoList).map { list -> list.find { it.quest.questId == questId } }
            .filterNotNull()
    }

    override suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote> {
        return fetchQuestLogResult
    }

    override suspend fun fetchQuestBoard(before: String?): Result<List<Quest>, DataError> {
        return fetchQuestBoardResult
    }

    override suspend fun fetchQuestById(questId: String): EmptyResult<DataError.Remote> {
        return fetchQuestByIdResult
    }

    override suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String
    ): Result<Quest, DataError.Remote> {
        return createQuestResult
    }

    override suspend fun leaveQuest(questId: String): EmptyResult<DataError.Remote> {
        return leaveQuestResult
            .onSuccess {
                questList.find { it.questId == questId }?.let {
                    questList.remove(it)
                    Result.Success(Unit)
                }?.asEmptyResult()
            }
    }
}