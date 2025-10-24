package com.helpquest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.helpquest.auth.presentation.navigation.AuthGraphRoutes
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.home.presentation.HomepageRoute
import com.helpquest.navigation.DeepLinkListener
import com.helpquest.navigation.NavigationRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    DeepLinkListener(navController)

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

    HelpQuestTheme {
        if (!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if (state.isLoggedIn) {
                    HomepageRoute
                } else {
                    AuthGraphRoutes.Graph
                }
            )
        }

//        RegisterScreen(
//            state = RegisterState(
//                registrationError = UiText.DynamicString("show a long longlong long long long long long long longlong longlong long message with the error")
//            ),
//            onAction = {},
//            snackbarHostState = SnackbarHostState()
//        )
//        RegisterSuccessScreen(
//            state = RegisterSuccessState(
//                registeredEmail = "message with the test",
//                isResendingVerificationEmail = true,
//                resendVerificationError = UiText.DynamicString("show a long longlong long long long long long long longlong longlong long message with the error")
//            ),
//            onAction = {},
//            snackbarHostState = SnackbarHostState()
//        )
//        EmailVerificationScreen(
//            state = EmailVerificationState(
//                isVerifying = false,
//                isVerified = false
//            ),
//            onAction = {}
//            )
    }
}