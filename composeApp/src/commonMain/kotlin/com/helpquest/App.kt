package com.helpquest

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.helpquest.auth.presentation.navigation.AuthGraphRoutes
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.home.presentation.navigation.HomepageGraphRoutes
import com.helpquest.navigation.DeepLinkListener
import com.helpquest.navigation.NavigationRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    onAuthenticationChecked: () -> Unit = {},
    onDeepLinkListenerSetup: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if (!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MainEvent.OnSessionExpired -> {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }

    HelpQuestTheme(
        darkTheme = isDarkTheme
    ) {
        if (!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if (state.isLoggedIn) {
                    HomepageGraphRoutes.HomepageRoute
                } else {
                    AuthGraphRoutes.Graph
                }
            )
            DeepLinkListener(navController, onDeepLinkListenerSetup)
        }
    }
}