package com.helpquest.chat.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ChatGraphRoutes {
    @Serializable
    data object Graph : ChatGraphRoutes

    @Serializable
    data object ChatListDetailRoute : ChatGraphRoutes
}