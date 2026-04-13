package com.helpquest

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.window.application
import com.helpquest.di.desktopModule
import com.helpquest.di.initKoin
import com.helpquest.windows.ApplicationStateHolder
import com.helpquest.windows.HelpQuestWindow
import org.koin.compose.koinInject

fun main() {
    initKoin {
        modules(desktopModule)
    }


    application {
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        LaunchedEffect(windows) {
            if (windows.isEmpty()) {
                exitApplication()
            }
        }

        for (window in windows) {
            key(window.id) {
                HelpQuestWindow(
                    onCloseRequest = {
                        applicationStateHolder.onWindowCloseRequest(window.id)
                    },
                    onAddWindowClick = applicationStateHolder::onAddWindowClick,
                    onFocusChanged = {

                    }
                )
            }
        }
    }
}