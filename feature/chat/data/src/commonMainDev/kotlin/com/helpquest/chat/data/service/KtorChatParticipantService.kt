package com.helpquest.chat.data.service


import com.helpquest.chat.domain.service.ChatParticipantService
import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.data.networking.get
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatParticipantService(
    private val httpClient: HttpClient
) : ChatParticipantService {

    override suspend fun searchParticipant(query: String): Result<List<Participant>, DataError.Remote> {
        return httpClient.get<List<ParticipantDto>>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { list ->
            list.map {
                it.toParticipant()
            }
        }
    }
}