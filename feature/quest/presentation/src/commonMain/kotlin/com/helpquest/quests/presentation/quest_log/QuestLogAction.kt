package com.helpquest.quests.presentation.quest_log

import com.helpquest.quests.presentation.model.QuestLogItemUi

sealed interface QuestLogAction {
    data class OnQuestClick(val quest: QuestLogItemUi) : QuestLogAction
    data class OnLeaveQuestClick(val questId: String) : QuestLogAction
}