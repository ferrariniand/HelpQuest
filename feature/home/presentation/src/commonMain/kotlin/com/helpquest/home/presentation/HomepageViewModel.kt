package com.helpquest.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomepageViewModel : ViewModel() {

    private val eventChannel = Channel<HomepageEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomepageState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomepageState()
        )

    fun onAction(action: HomepageAction) {
        when (action) {
            HomepageAction.OnUserAvatarClick -> {
                _state.update {
                    it.copy(
                        isUserMenuOpen = true
                    )
                }
            }

            HomepageAction.OnProfileSettingsClick,
            HomepageAction.OnLogoutClick,
            HomepageAction.OnDismissUserMenu -> {
                _state.update {
                    it.copy(
                        isUserMenuOpen = false
                    )
                }
            }
            else -> Unit
        }
    }

}