package com.helpquest.chat.presentation.create_manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.core.designsystem.components.selection_sections.SearchResult
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText


data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val existingChatParticipants: List<ParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val canAddParticipant: Map<ParticipantUi, Boolean> = emptyMap(),
    val currentSearchResult: SearchResult<List<ParticipantUi>>? = null,
    val searchError: UiText? = null,
    val isSubmitting: Boolean = false,
    val submitError: UiText? = null
)