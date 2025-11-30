@file:OptIn(ExperimentalTime::class)

package quest.data.service

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.models.QuestStatus
import com.helpquest.quests.domain.service.QuestLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeQuestRepository : QuestLogRepository {

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
    val quest = Quest(
        questId = questId,
        questTitle = "test quest1 title",
        questDescription = "test quest1 description",
        questCreatorId = participant.userId,
        createdAt = Clock.System.now(),
        questCategory = Category.GENERIC,
        participants = listOf(participant, participant2),
        questStatus = QuestStatus.IN_PROGRESS,
        lastActivity = QuestActivity(
            activityId = activityId,
            questId = questId,
            actorId = participant2.userId,
            content = "test activity content",
            activityStatus = QuestActivityStatus.IN_PROGRESS,
            startActivityAt = Clock.System.now(),
            endActivityAt = null
        )
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

    var questList = listOf(quest, quest2)

    var fetchQuestsResult: Result<List<Quest>, DataError.Remote> =
        Result.Success(questList)

    override fun getQuestLog(): Flow<List<Quest>> {
        return flowOf(questList)
    }

    override suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote> {
        return fetchQuestsResult
    }
}