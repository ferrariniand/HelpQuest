package com.helpquest.chat.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ChatGraphRoutes {
    @Serializable
    data object Graph : ChatGraphRoutes

    @Serializable
    data class ChatListDetailRoute(val chatId: String? = null) : ChatGraphRoutes
}