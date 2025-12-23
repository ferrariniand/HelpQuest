package com.helpquest.quests.presentation.add_activity

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.presentation.modelsUi.BannerState

data class AddQuestActivityState(
    val activityTextFieldState: TextFieldState = TextFieldState(),
    val canAddActivity: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)