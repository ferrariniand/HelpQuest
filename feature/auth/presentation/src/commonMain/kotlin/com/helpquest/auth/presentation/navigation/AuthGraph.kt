package com.helpquest.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.helpquest.auth.presentation.email_verification.EmailVerificationRoot
import com.helpquest.auth.presentation.register.RegisterRoot
import com.helpquest.auth.presentation.register_success.RegisterSuccessRoot

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Register
    ) {
        composable<AuthGraphRoutes.Register> {
            RegisterRoot(
                onRegisterSuccess = {
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(it))
                }
            )
        }
        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessRoot()
        }
        val deepLinkUrl = ""
        composable<AuthGraphRoutes.EmailVerification>(
            deepLinks = listOf(
                navDeepLink {
                    //TODO define URL!!!
                    this.uriPattern = "https://$deepLinkUrl/api/auth/verify?token={token}"
                },
                navDeepLink {
                    //TODO define URL!!!
                    this.uriPattern = "helpquest://$deepLinkUrl/api/auth/verify?token={token}"
                },
            )
        ) {
            EmailVerificationRoot()
        }
    }
}