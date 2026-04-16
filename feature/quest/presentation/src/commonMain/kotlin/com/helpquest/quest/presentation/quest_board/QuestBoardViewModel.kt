package com.helpquest.quest.presentation.quest_board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.domain.util.DataErrorException
import com.helpquest.core.domain.util.Paginator
import com.helpquest.core.domain.util.map
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toUiText
import com.helpquest.quest.domain.service.QuestConnectionClient
import com.helpquest.quest.domain.service.QuestRepository
import com.helpquest.quest.presentation.mappers.toQuestUi
import com.helpquest.quest.presentation.mappers.toQuestUiListWithSeparators
import com.helpquest.quest.presentation.model.QuestListUiElement
import com.helpquest.quest.presentation.model.QuestUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestBoardViewModel(
    private val repository: QuestRepository,
    private val connectionClient: QuestConnectionClient
) : ViewModel() {

    private val eventChannel = Channel<QuestBoardEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private var currentPaginator: Paginator<String?, QuestUi>? = null

    private val _state = MutableStateFlow(QuestBoardState())
    val state = combine(
        _state,
        repository.getQuestBoard(),
    ) { currentState, quests ->

        currentState.copy(
            quests = quests.toQuestUiListWithSeparators(),
        )
    }.onStart {
        if (!hasLoadedInitialData) {
            /** Load initial data here **/
            setupPaginatorForQuestBoard()
            observeConnectionState()
            loadNextItems() //added to load first page
            //TODO ...ON DEVICE ROTATION QUEST LIST IS DUPLICATED!!!
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

            QuestBoardAction.OnScrollToBottom -> onScrollToBottom()
            QuestBoardAction.OnRetryPaginationClick -> retryPagination()
            QuestBoardAction.OnHideBanner -> hideBanner()
            is QuestBoardAction.OnTopVisibleIndexChanged -> updateBanner(action.topVisibleIndex)
            else -> Unit
        }
    }

    private fun observeConnectionState() {
        connectionClient
            .connectionState
            .onEach { connectionState ->
                if (connectionState == ConnectionState.CONNECTED) {
                    currentPaginator?.loadNextItems()
                }

                _state.update {
                    it.copy(
                        connectionState = connectionState
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    //TODO probably can be removed
    private fun loadQuests() {
        viewModelScope.launch {
            repository.fetchQuestBoard()
        }
    }

    private fun setupPaginatorForQuestBoard() {
        currentPaginator = Paginator(
            initialKey = null,
            onLoadUpdated = { isLoading ->
                _state.update { it.copy(isPaginationLoading = isLoading) }
            },
            onRequest = { beforeTimestamp ->
                repository.fetchQuestBoard(beforeTimestamp).map {
                    it.map { quest ->
                        quest.toQuestUi()
                    }
                }
            },
            getNextKey = { quests ->
                //TODO Quest are sorted on the board by Creation timestamp, but should be sorted by position/distance or other more useful criteria
                quests.minOfOrNull { it.createdAt }?.toString()
            },
            onError = { throwable ->
                if (throwable is DataErrorException) {
                    _state.update {
                        it.copy(
                            paginationError = throwable.error.toUiText()
                        )
                    }
                }
            },
            onSuccess = { quests, _ ->
                _state.update {
                    it.copy(
                        endReached = quests.isEmpty(),
                        paginationError = null
                    )
                }
            }
        )

        _state.update {
            it.copy(
                endReached = false,
                isPaginationLoading = false,
            )
        }
    }

    private fun retryPagination() = loadNextItems()

    private fun onScrollToBottom() = loadNextItems()

    private fun loadNextItems() {
        viewModelScope.launch {
            currentPaginator?.loadNextItems()
        }
    }

    private fun hideBanner() {
        _state.update {
            it.copy(
                bannerState = it.bannerState.copy(
                    isVisible = false
                )
            )
        }
    }

    private fun updateBanner(topVisibleIndex: Int) {
        val visibleDate = calculateBannerPlaceFromIndex(
            quests = state.value.quests,
            index = topVisibleIndex
        )

        _state.update {
            it.copy(
                bannerState = BannerState(
                    bannerUiText = visibleDate,
                    isVisible = visibleDate != null
                )
            )
        }
    }

    private fun calculateBannerPlaceFromIndex(
        quests: List<QuestListUiElement>,
        index: Int
    ): UiText? {
        if (quests.isEmpty() || index < 0 || index >= quests.size) {
            return null
        }

        val nearestPlaceSeparator = (index until quests.size)
            .asSequence()
            .mapNotNull { index ->
                val item = quests.getOrNull(index)
                if (item is QuestListUiElement.PlaceSeparator) item.place else null
            }
            .firstOrNull()

        return nearestPlaceSeparator
    }
}