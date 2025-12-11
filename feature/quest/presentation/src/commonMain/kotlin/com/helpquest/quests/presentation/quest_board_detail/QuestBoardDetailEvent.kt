package com.helpquest.quests.presentation.quest_board_detail

sealed interface QuestBoardDetailEvent {
    data object CreateQuestDialogDismissed : QuestBoardDetailEvent
}