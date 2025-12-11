package com.helpquest.quests.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.helpquest.quests.presentation.quest_board_detail.QuestBoardDetailAdaptiveLayout

fun NavGraphBuilder.questGraph(
    navController: NavController
) {
    navigation<QuestGraphRoutes.Graph>(
        startDestination = QuestGraphRoutes.QuestBoardDetailRoute
    ) {
        composable<QuestGraphRoutes.QuestBoardDetailRoute> {
            QuestBoardDetailAdaptiveLayout()
        }
        composable<QuestGraphRoutes.QuestLogDetailRoute> {
//            QuestLogDetailAdaptiveLayout()
        }
    }
}