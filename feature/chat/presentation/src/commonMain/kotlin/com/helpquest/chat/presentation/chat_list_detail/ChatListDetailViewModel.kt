package com.helpquest.chat.presentation.chat_list_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChatListDetailViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<ChatListDetailEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(ChatListDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListDetailState()
        )

    fun onAction(action: ChatListDetailAction) {
        when (action) {
            is ChatListDetailAction.OnChatClick -> {
                _state.update {
                    it.copy(
                        selectedChatId = action.chatId
                    )
                }
            }

            ChatListDetailAction.OnCreateChatClick -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.CreateChat
                    )
                }
            }

            is ChatListDetailAction.OnDismissCurrentDialog -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.Hidden
                    )
                }
                if (action.isCreateChatDialog) {
                    eventChannel.trySend(ChatListDetailEvent.CreateChatDialogDismissed)
                }
            }

            ChatListDetailAction.OnManageChatClick -> {
                state.value.selectedChatId?.let { id ->
                    _state.update {
                        it.copy(
                            dialogState = DialogState.ManageChat(id)
                        )
                    }
                }
            }

            ChatListDetailAction.OnProfileSettingsClick -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.Profile
                    )
                }
            }
        }
    }
}