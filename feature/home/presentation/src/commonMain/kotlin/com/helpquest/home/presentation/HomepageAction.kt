package com.helpquest.home.presentation

sealed interface HomepageAction {
    data object OnUserAvatarClick : HomepageAction
    data object OnDismissUserMenu : HomepageAction
    data object OnProfileSettingsClick : HomepageAction
    data object OnLogoutClick : HomepageAction
    data object OnConfirmLogout : HomepageAction
    data object OnDismissLogoutDialog : HomepageAction
    data object OnFabButtonClick : HomepageAction
}