package com.helpquest.core.data.service

import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.data.networking.get
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.service.ParticipantService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import io.ktor.client.HttpClient

class KtorParticipantService(
    private val httpClient: HttpClient
) : ParticipantService {

    override suspend fun searchParticipants(query: String): Result<List<Participant>, DataError.Remote> {
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

    override suspend fun getLocalParticipant(): Result<Participant, DataError.Remote> {
        return httpClient.get<ParticipantDto>(
            route = "/localparticipant",
        ).map { it.toParticipant() }
    }
}