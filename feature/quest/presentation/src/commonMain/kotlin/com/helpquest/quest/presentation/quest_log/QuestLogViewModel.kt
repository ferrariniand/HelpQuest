package com.helpquest.quest.presentation.quest_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.util.toUiText
import com.helpquest.quest.domain.service.QuestRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestLogViewModel(
    private val repository: QuestRepository
) : ViewModel() {

    private val eventChannel = Channel<QuestLogEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(QuestLogState())
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
            initialValue = QuestLogState()
        )

    fun onAction(action: QuestLogAction) {
        when (action) {
            is QuestLogAction.OnQuestClick -> {
                _state.update {
                    it.copy(
                        selectedQuestId = action.quest.questId
                    )
                }
            }

            is QuestLogAction.OnLeaveQuestClick -> onLeaveQuestClick(action.questId)
            is QuestLogAction.OnDeleteQuestClick -> onDeleteQuestClick(action.questId)
            else -> Unit
        }
    }

    private fun onLeaveQuestClick(questId: String) {

        //TODO add loading state for the single item "leave quest"
        _state.update {
            it.copy(
                isQuestOptionsOpen = false
            )
        }

        viewModelScope.launch {
            repository
                .leaveQuest(questId)
                .onSuccess {
                    //TODO update quest Log List??? maybe is automatically updated from DB
                    eventChannel.send(QuestLogEvent.OnQuestLeftOrDeleted)
                }
                .onFailure { error ->
                    eventChannel.send(
                        QuestLogEvent.OnError(
                            error.toUiText()
                        )
                    )
                }
        }
    }

    private fun onDeleteQuestClick(questId: String) {

        _state.update {
            it.copy(
                isQuestOptionsOpen = false
            )
        }

        viewModelScope.launch {
//            repository
//                .deleteQuest(questId)
//                .onSuccess {
//
//                    eventChannel.send(QuestLogEvent.OnQuestLeftOrDeleted)
//                }
//                .onFailure { error ->
//                    eventChannel.send(
//                        QuestLogEvent.OnError(
//                            error.toUiText()
//                        )
//                    )
//                }
        }
    }
}