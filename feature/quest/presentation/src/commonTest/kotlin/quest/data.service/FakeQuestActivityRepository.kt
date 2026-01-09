@file:OptIn(ExperimentalTime::class)

package quest.data.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.quests.domain.models.ActivityWithCreator
import com.helpquest.quests.domain.models.OutgoingNewActivity
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.service.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeQuestActivityRepository : ActivityRepository {

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

    val participant3 = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = "test",
    )
    val questId = Random.nextInt().toString()
    val activityId1 = Random.nextInt().toString()
    val activityId2 = Random.nextInt().toString()

    val activity1 = QuestActivity(
        activityId = activityId1,
        questId = questId,
        creatorId = participant2.userId,
        actorId = participant2.userId,
        content = "test first activity content",
        activityStatus = QuestActivityStatus.IN_PROGRESS,
        startActivityAt = Clock.System.now(),
        endActivityAt = null
    )
    val activity2 = QuestActivity(
        activityId = activityId2,
        questId = questId,
        creatorId = participant.userId,
        actorId = participant.userId,
        content = "test second activity content",
        activityStatus = QuestActivityStatus.OPEN,
        startActivityAt = Clock.System.now(),
        endActivityAt = null
    )

    val activityList = mutableListOf(
        activity1,
        activity2
    )

    val activityWithCreator1 = ActivityWithCreator(
        activity = activity1,
        creator = participant2,
        activityStatus = QuestActivityStatus.IN_PROGRESS,
    )

    val activityWithCreator2 = ActivityWithCreator(
        activity = activity2,
        creator = participant,
        activityStatus = QuestActivityStatus.OPEN,
    )

    val activityWithActorList = mutableListOf(
        activityWithCreator1,
        activityWithCreator2
    )


    var fetchActivitiesResult: Result<List<QuestActivity>, DataError.Remote> =
        Result.Success(
            activityList.filter { it.questId == questId }
        )

    var updateActivityStatusResult: EmptyResult<DataError.Local> =
        Result.Success(activity1.activityStatus).asEmptyResult()

    var addActivityResult: EmptyResult<DataError> =
        Result.Success(activity1).asEmptyResult()

    var retryAddActivityResult: EmptyResult<DataError> =
        Result.Success(activity1).asEmptyResult()

    var deleteActivityResult: EmptyResult<DataError> =
        Result.Success(activity1).asEmptyResult()

    override suspend fun updateActivityStatus(
        activityId: String,
        status: QuestActivityStatus
    ): EmptyResult<DataError.Local> {
        return updateActivityStatusResult
    }

    override suspend fun fetchActivities(
        questId: String,
        before: String?
    ): Result<List<QuestActivity>, DataError> {
        return fetchActivitiesResult
    }

    override fun getActivitiesForQuest(questId: String): Flow<List<ActivityWithCreator>> {
        return flowOf(activityWithActorList)
    }

    override suspend fun addActivity(activity: OutgoingNewActivity): EmptyResult<DataError> {
        return addActivityResult
    }

    override suspend fun retryAddActivity(activityId: String): EmptyResult<DataError> {
        return retryAddActivityResult
    }

    override suspend fun deleteActivity(
        activityId: String,
        activityStatus: QuestActivityStatus
    ): EmptyResult<DataError> {
        return deleteActivityResult
    }

}