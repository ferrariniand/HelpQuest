package com.helpquest.quest.presentation.quest_board_detail

data class QuestBoardDetailState(
    val selectedQuestId: String? = null,
    val dialogState: DialogState = DialogState.Hidden
)

sealed interface DialogState {
    data object Hidden : DialogState
    data object CreateQuest : DialogState
    data class ManageQuest(val questId: String) : DialogState
    data object Profile : DialogState
}