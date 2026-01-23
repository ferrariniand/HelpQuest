package com.helpquest.profile.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ProfileGraphRoutes {

    @Serializable
    data object ProfileRoute : ProfileGraphRoutes
}