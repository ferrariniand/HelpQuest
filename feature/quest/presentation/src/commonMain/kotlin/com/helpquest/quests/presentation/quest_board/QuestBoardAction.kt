package com.helpquest.quests.presentation.quest_board

import com.helpquest.quests.presentation.model.QuestUi

sealed interface QuestBoardAction {
    data object OnProfileSettingsClick : QuestBoardAction
    data object OnCreateQuestClick : QuestBoardAction
    data class OnQuestClick(val quest: QuestUi) : QuestBoardAction
    data class OnSelectQuest(val questId: String?) : QuestBoardAction
}