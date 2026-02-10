package com.helpquest.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.auth.AuthRepository
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomepageViewModel(
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

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

            HomepageAction.OnLogoutClick -> showLogoutConfirmation()
            HomepageAction.OnConfirmLogout -> logout()
            HomepageAction.OnDismissLogoutDialog -> {
                _state.update {
                    it.copy(
                        showLogoutConfirmation = false
                    )
                }
            }

            HomepageAction.OnProfileSettingsClick,
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

    private fun showLogoutConfirmation() {
        _state.update {
            it.copy(
                isUserMenuOpen = false,
                showLogoutConfirmation = true
            )
        }
    }

    private fun logout() {
        _state.update {
            it.copy(
                showLogoutConfirmation = false
            )
        }

        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            val refreshToken = authInfo?.refreshToken ?: return@launch

            authRepository
                .logout(refreshToken)
                .onSuccess {
                    eventChannel.send(HomepageEvent.OnLogoutSuccess)
                }
                .onFailure { error ->
                    eventChannel.send(HomepageEvent.OnLogoutError(error.toUiText()))
                }
        }
    }

}