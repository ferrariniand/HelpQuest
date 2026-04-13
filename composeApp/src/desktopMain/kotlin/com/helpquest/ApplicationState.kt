package com.helpquest

import com.helpquest.windows.WindowState


data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState())
)
