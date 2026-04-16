package com.helpquest.quest.data.service

import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.data.networking.hqDelete
import com.helpquest.core.data.networking.hqGet
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import com.helpquest.quest.data.dto.QuestActivityDto
import com.helpquest.quest.data.dto.websocket.OutgoingQuestWebSocketDto
import com.helpquest.quest.data.mappers.toQuestActivity
import com.helpquest.quest.data.mappers.toWebSocketNewActivityDto
import com.helpquest.quest.domain.models.OutgoingNewActivity
import com.helpquest.quest.domain.models.QuestActivity
import com.helpquest.quest.domain.service.QuestActivityService
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class KtorQuestActivityService(
    private val httpClient: HttpClient,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
) : QuestActivityService {
    override suspend fun fetchActivities(
        questId: String,
        before: String?
    ): Result<List<QuestActivity>, DataError.Remote> {
        return httpClient.hqGet<List<QuestActivityDto>>(
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

    override suspend fun addActivity(activity: OutgoingNewActivity): EmptyResult<DataError.Connection> {
        val dto = activity.toWebSocketNewActivityDto()
        return webSocketConnector.sendMessage(dto.toJsonPayload())
    }

    override suspend fun deleteActivity(activityId: String): EmptyResult<DataError.Remote> {
        return httpClient.hqDelete(
            route = "/activities/$activityId"
        )
    }

    private fun OutgoingQuestWebSocketDto.NewActivity.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)
    }
}