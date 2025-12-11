@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.quests.presentation.quest_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.util.toUiText
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.presentation.mappers.toQuestUi
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

class QuestDetailViewModel(
    private val repository: QuestRepository,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val eventChannel = Channel<QuestDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _questId = MutableStateFlow<String?>(null)

    private val questInfoFlow = _questId
        .flatMapLatest { questId ->
            if (questId != null) {
                repository
                    .getQuestInfoById(questId)
            } else emptyFlow()
        }

    private val _state = MutableStateFlow(QuestDetailState())

    private val stateWithMessages = combine(
        _state,
        questInfoFlow,
        sessionStorage.observeAuthInfo()
    ) { currentState, questInfo, authInfo ->
        if (authInfo == null) {
            return@combine QuestDetailState()
        }

        currentState.copy(
            questUi = questInfo.quest.toQuestUi(),
            localParticipant = authInfo.user.toParticipantUi()
        )
    }

    val state = _questId
        .flatMapLatest { questId ->
            if (questId != null) {
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
            initialValue = QuestDetailState()
        )

    fun onAction(action: QuestDetailAction) {
        when (action) {
            is QuestDetailAction.OnSelectQuest -> switchQuest(action.questId)
            QuestDetailAction.OnQuestDetailsOptionsClick -> updateQuestOptionsState(isOpen = true)
            QuestDetailAction.OnDismissQuestOptions -> updateQuestOptionsState(isOpen = false)
            else -> Unit
        }
    }

    private fun switchQuest(questId: String?) {
        viewModelScope.launch {
            questId?.let {
                repository.fetchQuestById(questId)
                    .onSuccess {
                        _questId.update { questId }
                    }
                    .onFailure { error ->
                        eventChannel.send(
                            QuestDetailEvent.OnError(
                                error.toUiText()
                            )
                        )
                    }
            } ?: run {
                _questId.update { questId }
            }
        }
    }

    private fun updateQuestOptionsState(isOpen: Boolean) {
        _state.update {
            it.copy(
                isQuestOptionsOpen = isOpen
            )
        }
    }

}