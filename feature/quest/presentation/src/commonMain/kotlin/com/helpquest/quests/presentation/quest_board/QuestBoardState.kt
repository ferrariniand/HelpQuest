package com.helpquest.quests.presentation.quest_board

import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quests.presentation.model.QuestListUiElement

data class QuestBoardState(
    val quests: List<QuestListUiElement> = emptyList(),
    val error: UiText? = null,
    val selectedQuestId: String? = null,
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)