package com.helpquest.quest.presentation.quest_board


sealed interface QuestBoardAction {
    data object OnProfileSettingsClick : QuestBoardAction
    data object OnCreateQuestClick : QuestBoardAction
    data class OnSelectQuest(val questId: String?) : QuestBoardAction
    data object OnScrollToBottom : QuestBoardAction
    data object OnRetryPaginationClick : QuestBoardAction
    data object OnHideBanner : QuestBoardAction
    data class OnTopVisibleIndexChanged(val topVisibleIndex: Int) : QuestBoardAction
}