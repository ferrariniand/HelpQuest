@file:OptIn(ExperimentalUuidApi::class)

package com.helpquest.quest.presentation.add_activity

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.util.toUiText
import com.helpquest.quest.domain.models.OutgoingNewActivity
import com.helpquest.quest.domain.service.ActivityRepository
import com.helpquest.quest.domain.service.QuestConnectionClient
import com.helpquest.quest.presentation.model.ActivityListUiElement
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddQuestActivityViewModel(
    private val sessionStorage: SessionStorage,
    private val activityRepository: ActivityRepository,
    private val connectionClient: QuestConnectionClient
) : ViewModel() {

    private val eventChannel = Channel<AddQuestActivityEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _questId = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow(AddQuestActivityState())

    private val canAddActivity =
        snapshotFlow { _state.value.activityTextFieldState.text.toString() }
            .map { it.isBlank() }
            .combine(connectionClient.connectionState) { isMessageBlank, connectionState ->
                !isMessageBlank && connectionState == ConnectionState.CONNECTED
            }

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeCanAddActivity()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AddQuestActivityState()
        )

    fun onAction(action: AddQuestActivityAction) {
        when (action) {
            AddQuestActivityAction.OnCreateActivityClick -> createActivity()
            is AddQuestActivityAction.OnRetryClick -> retryCreate(action.activity)
            else -> TODO("Handle actions")
        }
    }

    private fun createActivity() {
        val currentQuestId = _questId.value
        val content = state.value.activityTextFieldState.text.toString().trim()
        if (content.isBlank() || currentQuestId == null) {
            return
        }

        viewModelScope.launch {
            val activity = OutgoingNewActivity(
                questId = currentQuestId,
                activityId = Uuid.random().toString(),
                content = content
            )

            activityRepository
                .addActivity(activity)
                .onSuccess {
                    state.value.activityTextFieldState.clearText()
                }
                .onFailure { error ->
                    eventChannel.send(AddQuestActivityEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun retryCreate(activity: ActivityListUiElement.ActivityItem) {
        viewModelScope.launch {
            activityRepository
                .retryAddActivity(activity.id)
                .onFailure { error ->
                    eventChannel.send(AddQuestActivityEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun observeCanAddActivity() {
        canAddActivity.onEach { canAdd ->
            _state.update {
                it.copy(
                    canAddActivity = canAdd
                )
            }
        }.launchIn(viewModelScope)
    }

}