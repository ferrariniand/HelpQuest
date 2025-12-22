package com.helpquest.quests.data.service

import com.helpquest.core.data.networking.get
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import com.helpquest.quests.data.dto.QuestActivityDto
import com.helpquest.quests.data.mappers.toQuestActivity
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.service.QuestActivityService
import io.ktor.client.HttpClient

class KtorQuestActivityService(
    private val httpClient: HttpClient
) : QuestActivityService {
    override suspend fun fetchActivities(
        questId: String,
        before: String?
    ): Result<List<QuestActivity>, DataError.Remote> {
        return httpClient.get<List<QuestActivityDto>>(
            route = "/quest/$questId/activities",
            queryParams = buildMap {
                if (before != null) {
                    this["before"] = before
                }
            }
        ).map {
            it.map { activityDto ->
                activityDto.toQuestActivity()
            }
        }
    }
}