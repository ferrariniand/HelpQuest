@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.quests.presentation.quest_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.util.toUiText
import com.helpquest.quests.domain.service.ActivityRepository
import com.helpquest.quests.domain.service.QuestConnectionClient
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.presentation.mappers.toActivityListUiElement
import com.helpquest.quests.presentation.mappers.toQuestUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestDetailViewModel(
    private val questRepository: QuestRepository,
    private val sessionStorage: SessionStorage,
    private val activityRepository: ActivityRepository,
    private val connectionClient: QuestConnectionClient
) : ViewModel() {

    private val eventChannel = Channel<QuestDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _questId = MutableStateFlow<String?>(null)

    private val questInfoFlow = _questId
        .flatMapLatest { questId ->
            if (questId != null) {
                questRepository
                    .getQuestInfoById(questId)
            } else emptyFlow()
        }

    private val _state = MutableStateFlow(QuestDetailState())

    private val stateWithActivities = combine(
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
                stateWithActivities
            } else _state
        }.onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeConnectionState()
                observeQuestActivities()
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


    private fun observeQuestActivities() {
        val currentActivities = state
            .map { it.activities }
            .distinctUntilChanged()

        val newActivities = _questId.flatMapLatest { questId ->
            if (questId != null) {
                activityRepository.getActivitiesForQuest(questId)
            } else emptyFlow()
        }.map { activities ->
            _state.update {
                it.copy(
                    activities = activities.map { it.toActivityListUiElement() }
                )
            }
            activities
        }

        combine(
            currentActivities,
            newActivities,
        ) { currentActivities, newActivities ->
            val lastNewId = newActivities.lastOrNull()?.activity?.activityId
            val lastCurrentId = currentActivities.lastOrNull()?.id

            if (lastNewId != lastCurrentId) {
                eventChannel.send(QuestDetailEvent.OnNewActivity)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeConnectionState() {
        connectionClient
            .connectionState
            .onEach { connectionState ->
                if (connectionState == ConnectionState.CONNECTED) {
                    _questId.value?.let {
                        activityRepository.fetchActivities(it, before = null)
                    }
                }

                _state.update {
                    it.copy(
                        connectionState = connectionState
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun switchQuest(questId: String?) {
        viewModelScope.launch {
            questId?.let {
                questRepository.fetchQuestById(questId)
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