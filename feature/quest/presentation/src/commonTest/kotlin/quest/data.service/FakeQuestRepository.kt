@file:OptIn(ExperimentalTime::class)

package quest.data.service

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.quests.domain.models.ActivityWithActor
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.models.QuestInfo
import com.helpquest.quests.domain.models.QuestStatus
import com.helpquest.quests.domain.service.QuestRepository
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
        actorId = participant2.userId,
        content = "test activity content",
        activityStatus = QuestActivityStatus.IN_PROGRESS,
        startActivityAt = Clock.System.now(),
        endActivityAt = null
    )

    val activityWithActor = ActivityWithActor(
        activity = activity,
        actor = participant2,
        activityStatus = QuestActivityStatus.IN_PROGRESS,
    )
    val quest = Quest(
        questId = questId,
        questTitle = "test quest1 title",
        questDescription = "test quest1 description",
        questCreatorId = participant.userId,
        createdAt = Clock.System.now(),
        questCategory = Category.GENERIC,
        participants = listOf(participant, participant2),
        questStatus = QuestStatus.IN_PROGRESS,
        lastActivity = activity
    )

    val questInfo = QuestInfo(
        quest = quest,
        activities = listOf(activityWithActor, activityWithActor, activityWithActor)
    )

    val quest2 = Quest(
        questId = Random.nextInt().toString(),
        questTitle = "test quest2 title",
        questDescription = "test quest2 description",
        questCreatorId = participant.userId,
        createdAt = Clock.System.now(),
        questCategory = Category.GENERIC,
        participants = listOf(
            participant,
        ),
        questStatus = QuestStatus.OPEN,
        lastActivity = null
    )

    val questInfo2 = QuestInfo(
        quest = quest2,
        activities = emptyList()
    )


    var questList = listOf(quest, quest2)
    var questInfoList = listOf(questInfo, questInfo2)

    var fetchQuestLogResult: Result<List<Quest>, DataError.Remote> =
        Result.Success(questList)

    var fetchQuestBoardResult: Result<List<Quest>, DataError.Remote> =
        Result.Success(questList)

    var fetchQuestByIdResult: EmptyResult<DataError.Remote> =
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

    override suspend fun fetchQuestBoard(): Result<List<Quest>, DataError.Remote> {
        return fetchQuestBoardResult
    }

    override suspend fun fetchQuestById(questId: String): EmptyResult<DataError.Remote> {
        return fetchQuestByIdResult
    }
}