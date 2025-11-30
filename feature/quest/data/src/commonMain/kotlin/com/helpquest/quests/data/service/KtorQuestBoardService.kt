package com.helpquest.quests.data.service

import com.helpquest.core.data.networking.get
import com.helpquest.core.data.networking.post
import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import com.helpquest.quests.data.dto.QuestDto
import com.helpquest.quests.data.dto.requests.CreateQuestRequest
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.service.QuestBoardService
import io.ktor.client.HttpClient

class KtorQuestBoardService(
    private val httpClient: HttpClient
) : QuestBoardService {

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
        ).map { chatDtos ->
            chatDtos.map { it.toQuest() }
        }
    }
}