package com.helpquest

import androidx.compose.ui.window.TrayState
import com.helpquest.core.domain.preferences.ThemePreference
import com.helpquest.windows.WindowState


data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState()),
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val trayState: TrayState = TrayState()
)
