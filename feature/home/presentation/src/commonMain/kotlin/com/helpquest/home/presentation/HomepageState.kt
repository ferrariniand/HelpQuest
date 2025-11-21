package com.helpquest.home.presentation

import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText

data class HomepageState(
    val error: UiText? = null,
    val localParticipant: ParticipantUi? = null,
    val isUserMenuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val isLoading: Boolean = false
)