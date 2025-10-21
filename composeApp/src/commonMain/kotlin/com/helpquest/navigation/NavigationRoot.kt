package com.helpquest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.helpquest.auth.presentation.navigation.AuthGraphRoutes
import com.helpquest.auth.presentation.navigation.authGraph
import com.helpquest.home.presentation.HomepageRoot
import com.helpquest.home.presentation.HomepageRoute

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthGraphRoutes.Graph
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(HomepageRoute) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        composable<HomepageRoute> {
            HomepageRoot()
        }
    }
}