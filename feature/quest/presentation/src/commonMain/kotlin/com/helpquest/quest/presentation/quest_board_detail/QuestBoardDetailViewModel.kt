package com.helpquest.quest.presentation.quest_board_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.quest.domain.service.QuestConnectionClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class QuestBoardDetailViewModel(
    private val connectionClient: QuestConnectionClient
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<QuestBoardDetailEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(QuestBoardDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                connectionClient.questActivities.launchIn(viewModelScope)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QuestBoardDetailState()
        )

    fun onAction(action: QuestBoardDetailAction) {
        when (action) {
            is QuestBoardDetailAction.OnSelectQuest -> {
                _state.update {
                    it.copy(
                        selectedQuestId = action.questId
                    )
                }
            }

            QuestBoardDetailAction.OnCreateQuestClick -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.CreateQuest
                    )
                }
            }

            is QuestBoardDetailAction.OnDismissCurrentDialog -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.Hidden
                    )
                }
                eventChannel.trySend(QuestBoardDetailEvent.CreateQuestDialogDismissed)
            }

            QuestBoardDetailAction.OnProfileSettingsClick -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.Profile
                    )
                }
            }

            QuestBoardDetailAction.OnQuestMembersClick -> TODO()
        }
    }
}