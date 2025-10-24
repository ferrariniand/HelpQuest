package com.helpquest

sealed interface MainEvent {
    data object OnSessionExpired : MainEvent
}