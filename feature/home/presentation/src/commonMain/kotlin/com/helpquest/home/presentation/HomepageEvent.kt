package com.helpquest.home.presentation

import com.helpquest.core.presentation.util.UiText

sealed interface HomepageEvent {
    data object OnLogoutSuccess : HomepageEvent
    data class OnLogoutError(val error: UiText) : HomepageEvent
}