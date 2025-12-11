package com.helpquest.quests.presentation.quest_board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.presentation.mappers.toQuestUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestBoardViewModel(
    private val repository: QuestRepository,
) : ViewModel() {

    private val eventChannel = Channel<QuestBoardEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(QuestBoardState())
    val state = combine(
        _state,
        repository.getQuestBoard(),
    ) { currentState, quests ->

        currentState.copy(
            quests = quests.map { it.toQuestUi() },
        )
    }.onStart {
        if (!hasLoadedInitialData) {
            /** Load initial data here **/
            loadQuests()
            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = QuestBoardState()
    )

    fun onAction(action: QuestBoardAction) {
        when (action) {
            is QuestBoardAction.OnSelectQuest -> {
                _state.update {
                    it.copy(
                        selectedQuestId = action.questId
                    )
                }
            }

            is QuestBoardAction.OnQuestClick -> {
                _state.update {
                    it.copy(
                        selectedQuestId = action.quest.questId
                    )
                }
            }

            else -> Unit
        }
    }

    private fun loadQuests() {
        viewModelScope.launch {
            repository.fetchQuestBoard()
        }
    }

}