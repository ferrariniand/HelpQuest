package com.helpquest.chat.data.service

import com.helpquest.chat.domain.service.ChatParticipantService
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

class MockChatParticipantService() : ChatParticipantService {

    val participantFull = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",
        showParticipantIdentity = true,
        classImageUrl = "test",
    )

    val participantNoClass = Participant(
        userId = "id2",
        username = "secondo",
        profilePictureUrl = "test",
        showParticipantIdentity = true,
        classImageUrl = null,
    )

    val participantNoImage = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = null,
        showParticipantIdentity = true,
        classImageUrl = "test",
    )

    val participantDontShowID = Participant(
        userId = "id4",
        username = "quarto",
        profilePictureUrl = "test",
        showParticipantIdentity = false,
        classImageUrl = "test",
    )

    val participantNoImageDontShowID = Participant(
        userId = "id5",
        username = "quinto",
        profilePictureUrl = null,
        showParticipantIdentity = false,
        classImageUrl = "test",
    )

    val participantList = listOf(
        participantFull,
        participantNoClass,
        participantNoImage,
    )

    val allPossibleParticipants = listOf(
        participantFull,
        participantNoClass,
        participantNoImage,
        participantDontShowID,
        participantNoImageDontShowID
    )


    var savedChatParticipantList: List<Participant>? = null

    override suspend fun searchParticipant(query: String): Result<List<Participant>, DataError.Remote> {
        if (query.contains("!")) {
            return Result.Failure(DataError.Remote.UNKNOWN)
        }

        val resultList =
            allPossibleParticipants.filter { (it.username == query) || it.username.contains(query) }
        return if (resultList.isEmpty()) {
            Result.Failure(DataError.Remote.NOT_FOUND)
        } else {
            Result.Success(resultList)
        }
    }
}