@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.chat.presentation.chat_details

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.presentation.mappers.toChatUi
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.presentation.util.toUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatDetailViewModel(
    private val repository: ChatRepository,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val eventChannel = Channel<ChatDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _chatId = MutableStateFlow<String?>(null)

    private val chatInfoFlow = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                repository
                    .getChatInfoById(chatId)
            } else emptyFlow()
        }

    private val _state = MutableStateFlow(ChatDetailState())

    private val stateWithMessages = combine(
        _state,
        chatInfoFlow,
        sessionStorage.observeAuthInfo()
    ) { currentState, chatInfo, authInfo ->
        if (authInfo == null) {
            return@combine ChatDetailState()
        }

        currentState.copy(
            chatUi = chatInfo.chat.toChatUi(authInfo.user.id)
        )
    }

    val state = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                stateWithMessages
            } else _state
        }.onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatDetailState()
        )

    fun onAction(action: ChatDetailAction) {
        when (action) {
            is ChatDetailAction.OnSelectChat -> switchChat(action.chatId)
            ChatDetailAction.OnChatOptionsClick -> updateChatOptionsState(isOpen = true)
            ChatDetailAction.OnDismissChatOptions -> updateChatOptionsState(isOpen = false)
            ChatDetailAction.OnLeaveChatClick -> onLeaveChatClick()
            else -> Unit
        }
    }

    private fun switchChat(chatId: String?) {
        viewModelScope.launch {
            chatId?.let {
                repository.fetchChatById(chatId)
                    .onSuccess {
                        _chatId.update { chatId }
                    }
                    .onFailure { error ->
                        eventChannel.send(
                            ChatDetailEvent.OnError(
                                error.toUiText()
                            )
                        )
                    }
            } ?: run {
                _chatId.update { chatId }
            }
        }
    }

    private fun updateChatOptionsState(isOpen: Boolean) {
        _state.update {
            it.copy(
                isChatOptionsOpen = isOpen
            )
        }
    }

    private fun onLeaveChatClick() {
        val chatId = _chatId.value ?: return

        _state.update {
            it.copy(
                isChatOptionsOpen = false
            )
        }

        viewModelScope.launch {
            repository
                .leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    _chatId.update { null }
                    _state.update {
                        it.copy(
                            chatUi = null,
                            messages = emptyList(),
                            bannerState = BannerState()
                        )
                    }
                    eventChannel.send(ChatDetailEvent.OnChatLeft)
                }
                .onFailure { error ->
                    eventChannel.send(
                        ChatDetailEvent.OnError(
                            error.toUiText()
                        )
                    )
                }
        }
    }
}