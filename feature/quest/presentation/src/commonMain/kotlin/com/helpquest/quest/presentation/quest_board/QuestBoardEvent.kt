package com.helpquest.quest.presentation.quest_board

import com.helpquest.core.presentation.util.UiText

sealed interface QuestBoardEvent {
    data class OnError(val error: UiText) : QuestBoardEvent

}