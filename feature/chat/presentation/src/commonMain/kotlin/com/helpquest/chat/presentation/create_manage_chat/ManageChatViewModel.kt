@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.helpquest.chat.presentation.create_manage_chat

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.core.designsystem.components.selection_sections.SearchResult
import com.helpquest.core.domain.service.ParticipantService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toUiText
import helpquest.feature.chat.presentation.generated.resources.Res
import helpquest.feature.chat.presentation.generated.resources.error_participant_already_added
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageChatViewModel(
    private val participantService: ParticipantService,
    private val repository: ChatRepository,
    initialState: ManageChatState = ManageChatState()
) : ViewModel() {

    private val _chatId = MutableStateFlow<String?>(null)

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<ManageChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private val activeParticipantsFlow = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                repository
                    .getActiveParticipantsByChatId(chatId)
            } else flowOf(emptyList())
        }

    private val _state = MutableStateFlow(initialState)
    val state = combine(
        _state,
        activeParticipantsFlow,
    ) { currentState, participants ->
        currentState.copy(
            existingChatParticipants = participants.map { it.toParticipantUi() },
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManageChatState()
        )


    fun onAction(action: ManageChatAction) {
        when (action) {
            ManageChatAction.OnDebounceSearchTextField -> performSearch()
            is ManageChatAction.OnAddClick -> addParticipant(action.participant)
            ManageChatAction.OnPrimaryActionClick -> addParticipantsToChat()
            is ManageChatAction.OnSelectChat -> {
                _chatId.update { action.chatId }
            }

            ManageChatAction.OnDismissDialog -> {
                _state.value.queryTextState.clearText()
                _state.update {
                    it.copy(
                        isSearching = false,
                        searchError = null,
                        canAddParticipant = emptyMap(),
                        currentSearchResult = null
                    )
                }
            }
        }
    }

    private fun performSearch() {
        val query = state.value.queryTextState.text.toString()
        if (query.isBlank()) {
            _state.update {
                it.copy(
                    currentSearchResult = null,
                    canAddParticipant = emptyMap(),
                    searchError = null
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSearching = true,
                    canAddParticipant = emptyMap()
                )
            }

            participantService
                .searchParticipants(query)
                .onSuccess { participants ->
                    val participantUiList = participants.map { participant ->
                        participant.toParticipantUi()
                    }
                    _state.update {
                        it.copy(
                            currentSearchResult = SearchResult.Success(
                                participantUiList
                            ),
                            isSearching = false,
                            canAddParticipant = checkParticipantAvailability(participantUiList),
                            searchError = null
                        )
                    }
                }
                .onFailure { error ->
                    when (error) {
                        DataError.Remote.NOT_FOUND -> {
                            _state.update {
                                it.copy(
                                    currentSearchResult = SearchResult.NotFound,
                                    isSearching = false,
                                    canAddParticipant = emptyMap(),
                                    searchError = null
                                )
                            }
                        }

                        else -> {
                            val errorMessage = error.toUiText()
                            _state.update {
                                it.copy(
                                    currentSearchResult = null,
                                    isSearching = false,
                                    canAddParticipant = emptyMap(),
                                    searchError = errorMessage,
                                )
                            }
                        }
                    }

                }
        }
    }

    private fun checkParticipantAvailability(
        participants: List<ParticipantUi>
    ): Map<ParticipantUi, Boolean> = participants.associateWith {
        !state.value.selectedChatParticipants.contains(it) && !state.value.existingChatParticipants.contains(
            it
        )
    }

    private fun updateParticipantAvailability(
        searchedParticipants: List<ParticipantUi>?,
        existingParticipants: List<ParticipantUi>,
        selectedParticipants: List<ParticipantUi>
    ): Map<ParticipantUi, Boolean> {
        return searchedParticipants?.let {
            it.associateWith { participant ->
                !selectedParticipants.contains(participant) && !existingParticipants.contains(
                    participant
                )
            }
        } ?: emptyMap()
    }

    private fun addParticipant(participant: ParticipantUi) {
        if (
            state.value.canAddParticipant.isEmpty() ||
            state.value.canAddParticipant[participant] == false
        ) {
            return
        }

        val isAlreadyInChat = state.value.existingChatParticipants.any {
            it.id == participant.id
        }
        val isAlreadySelected = state.value.selectedChatParticipants.any {
            it.id == participant.id
        }
        if (isAlreadyInChat || isAlreadySelected) {
            _state.update {
                it.copy(
                    searchError = UiText.Resource(Res.string.error_participant_already_added),
                )
            }
        } else {
            val searchedParticipants = state.value.currentSearchResult?.getSearchResultOrNull()
            val selectedParticipants = state.value.selectedChatParticipants + participant
            val participantAvailabilityMap =
                updateParticipantAvailability(
                    searchedParticipants,
                    state.value.existingChatParticipants,
                    selectedParticipants
                )


            //if all the searched participants are already added (all the elements of the map are false)
            if (participantAvailabilityMap.filter { it.value }.isEmpty()) {
                _state.update {
                    it.copy(
                        selectedChatParticipants = selectedParticipants,
                        canAddParticipant = emptyMap(),
                        currentSearchResult = null
                    )
                }
                _state.value.queryTextState.clearText()
            } else {
                _state.update {
                    it.copy(
                        selectedChatParticipants = selectedParticipants,
                        canAddParticipant = participantAvailabilityMap,
                    )
                }
            }

        }
    }

    private fun addParticipantsToChat() {
        val chatId = _chatId.value ?: return
        val selectedParticipants = state.value.selectedChatParticipants
        if (selectedParticipants.isEmpty()) {
            return
        }
        val selectedUserIds = selectedParticipants.map { it.id }


        val previousCanAddParticipant = state.value.canAddParticipant

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSubmitting = true,
                    canAddParticipant = emptyMap(),
                    submitError = null
                )
            }

            repository
                .addParticipantsToChat(chatId, selectedUserIds)
                .onSuccess { chat ->
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            currentSearchResult = null,
                            isSearching = false,
                            searchError = null,
                            selectedChatParticipants = emptyList()
                        )
                    }
                    _state.value.queryTextState.clearText()

                    eventChannel.send(ManageChatEvent.OnMembersAdded)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            canAddParticipant = previousCanAddParticipant,
                            submitError = error.toUiText(),
                        )
                    }
                }
        }
    }
}