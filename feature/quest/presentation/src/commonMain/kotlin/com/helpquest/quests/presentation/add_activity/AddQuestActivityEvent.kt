package com.helpquest.quests.presentation.add_activity

import com.helpquest.core.presentation.util.UiText

sealed interface AddQuestActivityEvent {
    data class OnError(val error: UiText) : AddQuestActivityEvent

}