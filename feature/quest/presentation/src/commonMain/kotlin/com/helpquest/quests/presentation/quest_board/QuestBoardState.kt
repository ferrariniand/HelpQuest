package com.helpquest.quests.presentation.quest_board

import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quests.presentation.model.QuestUi

data class QuestBoardState(
    val quests: List<QuestUi> = emptyList(),
    val error: UiText? = null,
    val selectedQuestId: String? = null,
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)