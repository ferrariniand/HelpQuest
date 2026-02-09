package com.helpquest.quests.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface QuestGraphRoutes {
    @Serializable
    data object Graph : QuestGraphRoutes

    @Serializable
    data object QuestBoardDetailRoute : QuestGraphRoutes

    @Serializable
    data class QuestLogDetailRoute(val questId: String? = null) : QuestGraphRoutes

}