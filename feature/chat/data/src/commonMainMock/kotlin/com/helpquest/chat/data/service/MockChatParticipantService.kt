package com.helpquest.chat.data.service

import com.helpquest.chat.data.service.MockChatResponseElements
import com.helpquest.chat.domain.service.ChatParticipantService
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.SubClass
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

class MockChatParticipantService(
    val mockResponse: MockChatResponseElements
) : ChatParticipantService {

    var savedChatParticipantList: List<Participant>? = null

    override suspend fun searchParticipant(query: String): Result<List<Participant>, DataError.Remote> {
        if (query.contains("!")) {
            return Result.Failure(DataError.Remote.UNKNOWN)
        }

        val resultList =
            mockResponse.allPossibleParticipants.filter {
                (it.username == query) || it.username.contains(
                    query
                )
            }
        return if (resultList.isEmpty()) {
            Result.Failure(DataError.Remote.NOT_FOUND)
        } else {
            Result.Success(resultList)
        }
    }
}