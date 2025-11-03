package com.helpquest.core.presentation.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity

@Composable
fun isKeyboardOpen(): State<Boolean> {
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    return rememberUpdatedState(imeHeight > 0)
}

@Composable
fun isKeyboardVisible(): State<Boolean> {
    val configuration = currentDeviceConfiguration()
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    return rememberUpdatedState(imeHeight > 0 && (configuration != DeviceConfiguration.DESKTOP))
}