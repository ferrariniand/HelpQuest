package com.helpquest.quest.presentation.quest_details

import com.helpquest.core.presentation.util.UiText

sealed interface QuestDetailEvent {
    data object OnQuestLeftOrDeleted : QuestDetailEvent
    data class OnError(val error: UiText) : QuestDetailEvent
    data object OnNewActivity : QuestDetailEvent

}