package com.helpquest.quests.presentation.quest_log_detail

sealed interface QuestLogDetailAction {
    data class OnSelectQuest(val questId: String?) : QuestLogDetailAction
    data object OnProfileSettingsClick : QuestLogDetailAction
    data object OnManageQuestClick : QuestLogDetailAction
    data object OnDismissCurrentDialog : QuestLogDetailAction
}