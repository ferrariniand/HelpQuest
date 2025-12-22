package com.helpquest.core.domain.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ConnectionClient {
    val connectionState: StateFlow<ConnectionState>
}

interface CoreConnectionClient : ConnectionClient {
    val updatedParticipants: Flow<Participant>
}