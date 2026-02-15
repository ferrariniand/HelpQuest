@file:OptIn(ExperimentalTime::class)

package com.helpquest.quests.data.service


import com.helpquest.core.data.networking.hqDelete
import com.helpquest.core.data.networking.hqGet
import com.helpquest.core.data.networking.hqPost
import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.models.GeoLocation
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.map
import com.helpquest.quests.data.dto.QuestDto
import com.helpquest.quests.data.dto.requests.CreateQuestRequest
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.models.QuestStatus
import com.helpquest.quests.domain.service.QuestService
import io.ktor.client.HttpClient
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class KtorQuestService(
    private val httpClient: HttpClient
) : QuestService {

    override suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String,
    ): Result<Quest, DataError.Remote> {
        return httpClient.hqPost<CreateQuestRequest, QuestDto>(
            route = "/quest",
            body = CreateQuestRequest(
                questTitle = questTitle,
                questDescription = questDescription,
                questCategory = questCategory.name,
                questCreatorId = questCreatorId
            )
        ).map { it.toQuest() }
    }

    override suspend fun getQuestBoard(): Result<List<Quest>, DataError.Remote> {
        return httpClient.hqGet<List<QuestDto>>(
            route = "/questboard"
        ).map { questDtos ->
            questDtos.map { it.toQuest() }
        }
    }

    override suspend fun fetchQuestBoard(
        before: String?
    ): Result<List<Quest>, DataError.Remote> {
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
        val questId2 = Random.nextInt().toString()
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

        val quest = Quest(
            questId = questId,
            questTitle = "test quest1 title",
            questDescription = "test quest1 description",
            questCreatorId = participant.userId,
            createdAt = Clock.System.now(),
            location = GeoLocation(0.0, 0.0),
            questCategory = Category.GENERIC,
            participants = listOf(participant, participant2),
            questStatus = QuestStatus.OPEN,
            lastActivity = activity,
            lastUpdateAt = Clock.System.now()
        )
        val quest2 = Quest(
            questId = questId2,
            questTitle = "test quest2 title",
            questDescription = "test quest2 description",
            questCreatorId = participant.userId,
            createdAt = Clock.System.now(),
            location = GeoLocation(0.0, 0.0),
            questCategory = Category.GENERIC,
            participants = listOf(
                participant, participant3
            ),
            questStatus = QuestStatus.OPEN,
            lastActivity = null,
            lastUpdateAt = Clock.System.now()
        )
        var questList = mutableListOf(quest, quest2)

        return Result.Success(questList)
        //TODO MOCK
//        return httpClient.get<List<QuestDto>>(
//            route = "/questboard",
//            queryParams = buildMap {
//                this["pageSize"] = QuestDtoConstants.PAGE_SIZE
//                if (before != null) {
//                    this["before"] = before
//                }
//            }
//        ).map {
//            it.map { questDto ->
//                questDto.toQuest()
//            }
//        }
    }

    override suspend fun getQuestLog(): Result<List<Quest>, DataError.Remote> {
        return httpClient.hqGet<List<QuestDto>>(
            route = "/questlog"
        ).map { chatDtos ->
            chatDtos.map { it.toQuest() }
        }
    }

    override suspend fun getQuestById(questId: String): Result<Quest, DataError.Remote> {
        return httpClient.hqGet<QuestDto>(
            route = "/quest/$questId"
        ).map { it.toQuest() }
    }

    override suspend fun leaveQuest(questId: String): EmptyResult<DataError.Remote> {
        return httpClient.hqDelete<Unit>(
            route = "/quest/$questId/leave"
        ).asEmptyResult()
    }
}