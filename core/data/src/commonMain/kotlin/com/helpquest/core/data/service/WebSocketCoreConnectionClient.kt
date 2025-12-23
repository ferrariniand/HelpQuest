package com.helpquest.core.data.service


import com.helpquest.core.data.dto.websocket.IncomingCoreWebSocketDto
import com.helpquest.core.data.dto.websocket.IncomingCoreWebSocketType
import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.CoreConnectionClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class WebSocketCoreConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val database: HelpQuestDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val applicationScope: CoroutineScope
) : CoreConnectionClient {

    override val updatedParticipants = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(it) }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingCoreWebSocketDto.ProfilePictureUpdated>()
        .mapNotNull {
            database.participantDao.getParticipantById(it.userId)?.toParticipant()
        }
        .shareIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000)
        )

    override val connectionState = webSocketConnector.connectionState

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingCoreWebSocketDto? {
        return when (message.type) {
            IncomingCoreWebSocketType.PROFILE_PICTURE_UPDATED.name -> {
                json.decodeFromString<IncomingCoreWebSocketDto.ProfilePictureUpdated>(message.payload)
            }

            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingCoreWebSocketDto) {
        when (message) {
            is IncomingCoreWebSocketDto.ProfilePictureUpdated -> updateProfilePicture(message)
        }
    }

    private suspend fun updateProfilePicture(message: IncomingCoreWebSocketDto.ProfilePictureUpdated) {
        database.participantDao.updateProfilePictureUrl(
            userId = message.userId,
            newUrl = message.newUrl
        )

        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
        if (authInfo != null && authInfo.user.id == message.userId) {
            sessionStorage.setAuthInfo(
                info = authInfo.copy(
                    user = authInfo.user.copy(
                        profilePictureUrl = message.newUrl
                    )
                )
            )
        }
    }
}