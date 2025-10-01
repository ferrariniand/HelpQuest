package com.helpquest

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.helpquest.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "HelpQuest",
        ) {
            App()
        }
    }
}