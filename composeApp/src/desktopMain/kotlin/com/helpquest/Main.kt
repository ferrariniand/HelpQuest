package com.helpquest

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.helpquest.deeplink.DesktopDeepLinkHandler
import com.helpquest.di.desktopModule
import com.helpquest.di.initKoin
import com.helpquest.navigation.ExternalUriHandler
import com.helpquest.theme.rememberAppTheme
import com.helpquest.windows.HelpQuestWindow
import org.koin.compose.koinInject

fun main(args: Array<String>) {
    initKoin {
        modules(desktopModule)
    }

    DesktopDeepLinkHandler.setup()

    val initialDeepLink = args.firstOrNull { link ->
        val cleanedDeepLink = link.trim('"')

        DesktopDeepLinkHandler.supportedUriPatterns.any { it.matches(cleanedDeepLink) }
    }?.trim('"')

    application {
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        LaunchedEffect(windows) {
            if (windows.isEmpty()) {
                exitApplication()
            }
        }

        var canReceiveDeepLink by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(canReceiveDeepLink) {
            if (canReceiveDeepLink && initialDeepLink != null) {
                ExternalUriHandler.onNewUri(initialDeepLink)
            }
        }

        val appTheme = rememberAppTheme(applicationState.themePreference)

        for (window in windows) {
            key(window.id) {
                HelpQuestWindow(
                    appTheme = appTheme,
                    onCloseRequest = {
                        applicationStateHolder.onWindowCloseRequest(window.id)
                    },
                    onAddWindowClick = applicationStateHolder::onAddWindowClick,
                    onFocusChanged = { focused ->
                        applicationStateHolder.onWindowFocusChanged(window.id, focused)
                    },
                    onDeepLinkListenerSetup = {
                        canReceiveDeepLink = true
                    }
                )
            }
        }

        HelpQuestTrayMenu(
            state = applicationState.trayState,
            themePreferenceFromAppSettings = applicationState.themePreference,
            onThemePreferenceClick = applicationStateHolder::onThemePreferenceClick
        )
    }
}