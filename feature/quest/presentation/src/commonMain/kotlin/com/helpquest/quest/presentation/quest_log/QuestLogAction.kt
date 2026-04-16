package com.helpquest.quest.presentation.quest_log

import com.helpquest.quest.presentation.model.QuestUi

sealed interface QuestLogAction {
    data class OnQuestClick(val quest: QuestUi) : QuestLogAction
    data object OnOpenChatClick : QuestLogAction
    data object OnQuestOptionsClick : QuestLogAction

    data class OnLeaveQuestClick(val questId: String) : QuestLogAction
    data class OnDeleteQuestClick(val questId: String) : QuestLogAction
    data object OnDismissQuestOptions : QuestLogAction

}