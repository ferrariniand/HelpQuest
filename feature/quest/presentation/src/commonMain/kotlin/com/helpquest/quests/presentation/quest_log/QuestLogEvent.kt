package com.helpquest.quests.presentation.quest_log

import com.helpquest.core.presentation.util.UiText

sealed interface QuestLogEvent {
    data object OnQuestLeft : QuestLogEvent
    data class OnError(val error: UiText) : QuestLogEvent
}