package com.helpquest.chat.presentation.create_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.core.designsystem.components.selection_sections.SearchResult
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText


data class CreateChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val selectedChatParticipants: List<ParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val canAddParticipant: Map<ParticipantUi, Boolean> = emptyMap(),
    val currentSearchResult: SearchResult<List<ParticipantUi>>? = null,
    val searchError: UiText? = null,
    val isCreatingChat: Boolean = false,
    val createChatError: UiText? = null
)