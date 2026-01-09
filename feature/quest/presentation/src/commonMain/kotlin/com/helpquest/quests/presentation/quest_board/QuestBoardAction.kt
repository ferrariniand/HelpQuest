package com.helpquest.quests.presentation.quest_board


sealed interface QuestBoardAction {
    data object OnProfileSettingsClick : QuestBoardAction
    data object OnCreateQuestClick : QuestBoardAction
    data class OnSelectQuest(val questId: String?) : QuestBoardAction
}