package com.helpquest.quests.data.service


import com.helpquest.core.data.networking.delete
import com.helpquest.core.data.networking.get
import com.helpquest.core.data.networking.post
import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.map
import com.helpquest.quests.data.dto.QuestDto
import com.helpquest.quests.data.dto.QuestDtoConstants
import com.helpquest.quests.data.dto.requests.CreateQuestRequest
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.service.QuestService
import io.ktor.client.HttpClient

class KtorQuestService(
    private val httpClient: HttpClient
) : QuestService {

    override suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String,
    ): Result<Quest, DataError.Remote> {
        return httpClient.post<CreateQuestRequest, QuestDto>(
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
        return httpClient.get<List<QuestDto>>(
            route = "/questboard"
        ).map { questDtos ->
            questDtos.map { it.toQuest() }
        }
    }

    override suspend fun fetchQuestBoard(
        before: String?
    ): Result<List<Quest>, DataError.Remote> {
        return httpClient.get<List<QuestDto>>(
            route = "/questboard",
            queryParams = buildMap {
                this["pageSize"] = QuestDtoConstants.PAGE_SIZE
                if (before != null) {
                    this["before"] = before
                }
            }
        ).map {
            it.map { questDto ->
                questDto.toQuest()
            }
        }
    }

    override suspend fun getQuestLog(): Result<List<Quest>, DataError.Remote> {
        return httpClient.get<List<QuestDto>>(
            route = "/questlog"
        ).map { chatDtos ->
            chatDtos.map { it.toQuest() }
        }
    }

    override suspend fun getQuestById(questId: String): Result<Quest, DataError.Remote> {
        return httpClient.get<QuestDto>(
            route = "/quest/$questId"
        ).map { it.toQuest() }
    }

    override suspend fun leaveQuest(questId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/quest/$questId/leave"
        ).asEmptyResult()
    }
}