package com.helpquest.windows

import java.util.UUID


data class WindowState(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "HelpQuest",
    val isFocused: Boolean = false
)