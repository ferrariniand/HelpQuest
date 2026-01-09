package com.helpquest.quests.presentation.quest_board_detail

sealed interface QuestBoardDetailAction {
    data class OnSelectQuest(val questId: String?) : QuestBoardDetailAction
    data object OnProfileSettingsClick : QuestBoardDetailAction
    data object OnCreateQuestClick : QuestBoardDetailAction
    data object OnDismissCurrentDialog : QuestBoardDetailAction
    data object OnQuestMembersClick : QuestBoardDetailAction
}