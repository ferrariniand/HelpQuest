package com.helpquest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.helpquest.auth.presentation.navigation.AuthGraphRoutes
import com.helpquest.auth.presentation.navigation.authGraph
import com.helpquest.chat.presentation.navigation.ChatGraphRoutes
import com.helpquest.chat.presentation.navigation.chatGraph
import com.helpquest.home.presentation.HomepageRoot
import com.helpquest.home.presentation.navigation.HomepageGraphRoutes
import com.helpquest.quests.presentation.navigation.QuestGraphRoutes
import com.helpquest.quests.presentation.navigation.questGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(HomepageGraphRoutes.HomepageRoute) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        composable<HomepageGraphRoutes.HomepageRoute> {
            HomepageRoot(
                onLogout = {

                },
                onProfileSettingsClick = {

                },
                onChatFabButtonClick = {
                    navController.navigate(ChatGraphRoutes.Graph)
                },
                onQuestFabButtonClick = {
                    navController.navigate(QuestGraphRoutes.Graph)
                }
            )
        }
        chatGraph(
            navController = navController
        )
        questGraph(
            navController = navController
        )
    }
}