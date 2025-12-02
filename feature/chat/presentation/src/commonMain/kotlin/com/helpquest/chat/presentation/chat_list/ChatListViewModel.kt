package com.helpquest.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.presentation.mappers.toChatUi
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.presentation.mappers.toParticipantUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val repository: ChatRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val eventChannel = Channel<ChatListEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        repository.getChats(),
        sessionStorage.observeAuthInfo()
    ) { currentState, chats, authInfo ->
        if (authInfo == null) {
            return@combine ChatListState()
        }

        currentState.copy(
            chats = chats.map { it.toChatUi(authInfo.user.id) },
            localParticipant = authInfo.user.toParticipantUi()
        )
    }.onStart {
        if (!hasLoadedInitialData) {
            /** Load initial data here **/
            loadChats()
            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ChatListState()
    )

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnChatClick -> {
                _state.update {
                    it.copy(
                        selectedChatId = action.chat.id
                    )
                }
            }
            else -> Unit
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }

}