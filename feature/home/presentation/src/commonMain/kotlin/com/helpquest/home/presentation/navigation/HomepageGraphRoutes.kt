package com.helpquest.home.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface HomepageGraphRoutes {

    @Serializable
    data object HomepageRoute : HomepageGraphRoutes
}