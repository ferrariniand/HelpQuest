package com.helpquest.quests.data.service


import com.helpquest.core.data.networking.get
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import com.helpquest.quests.data.dto.QuestDto
import com.helpquest.quests.data.mappers.toQuest
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.service.QuestLogService
import io.ktor.client.HttpClient

class KtorQuestLogService(
    private val httpClient: HttpClient
) : QuestLogService {

    override suspend fun getQuestLog(): Result<List<Quest>, DataError.Remote> {
        return httpClient.get<List<QuestDto>>(
            route = "/questlog"
        ).map { chatDtos ->
            chatDtos.map { it.toQuest() }
        }
    }
}