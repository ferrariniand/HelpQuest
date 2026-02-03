package com.helpquest.profile.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.ParticipantRepository
import com.helpquest.core.domain.util.ClassUtils
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.domain.validation.PasswordValidator
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toUiText
import helpquest.feature.profile.presentation.generated.resources.Res
import helpquest.feature.profile.presentation.generated.resources.error_current_password_equal_to_new_one
import helpquest.feature.profile.presentation.generated.resources.error_current_password_incorrect
import helpquest.feature.profile.presentation.generated.resources.error_invalid_file_type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authService: AuthService,
    private val participantRepository: ParticipantRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = combine(
        _state,
        sessionStorage.observeAuthInfo()
    ) { currentState, authInfo ->
        if (authInfo != null) {
            currentState.copy(
                username = authInfo.user.username,
                userInitials = authInfo.user.initials,
                emailTextState = TextFieldState(initialText = authInfo.user.email),
                profilePictureUrl = authInfo.user.profilePictureUrl,
                classImageUrl = ClassUtils.findClassById(authInfo.user.classId).classImageUrl,
            )
        } else currentState
    }
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeCanChangePassword()
                fetchLocalParticipantDetails()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangePasswordClick -> changePassword()
            is ProfileAction.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            is ProfileAction.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            is ProfileAction.OnPictureSelected -> uploadProfilePicture(
                action.bytes,
                action.mimeType
            )

            is ProfileAction.OnDeletePictureClick -> showDeleteConfirmation()
            is ProfileAction.OnConfirmDeleteClick -> deleteProfilePicture()
            is ProfileAction.OnDismissDeleteConfirmationDialogClick -> dismissDeleteConfirmation()
            else -> Unit
        }
    }

    private fun fetchLocalParticipantDetails() {
        viewModelScope.launch {
            participantRepository.fetchLocalParticipant()
        }
    }

    private fun toggleCurrentPasswordVisibility() {
        _state.update {
            it.copy(
                isCurrentPasswordVisible = !it.isCurrentPasswordVisible
            )
        }
    }

    private fun toggleNewPasswordVisibility() {
        _state.update {
            it.copy(
                isNewPasswordVisible = !it.isNewPasswordVisible
            )
        }
    }

    private fun observeCanChangePassword() {
        val isCurrentPasswordValidFlow = snapshotFlow {
            _state.value.currentPasswordTextState.text.toString()
        }.map { it.isNotBlank() }.distinctUntilChanged()

        val isNewPasswordValidFlow = snapshotFlow {
            _state.value.newPasswordTextState.text.toString()
        }.map {
            PasswordValidator.validate(it).isValidPassword
        }.distinctUntilChanged()

        combine(
            isCurrentPasswordValidFlow,
            isNewPasswordValidFlow
        ) { isCurrentValid, isNewValid ->
            _state.update {
                it.copy(
                    canChangePassword = isCurrentValid && isNewValid
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun changePassword() {
        if (!state.value.canChangePassword && state.value.isChangingPassword) { //TODO: MAYBE OR??!
            return
        }

        _state.update {
            it.copy(
                isChangingPassword = true,
                isPasswordChangeSuccessful = false
            )
        }
        viewModelScope.launch {
            val currentPassword = state.value.currentPasswordTextState.text.toString()
            val newPassword = state.value.newPasswordTextState.text.toString()
            authService
                .changePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )
                .onSuccess {
                    state.value.currentPasswordTextState.clearText()
                    state.value.newPasswordTextState.clearText()

                    _state.update {
                        it.copy(
                            isChangingPassword = false,
                            newPasswordError = null,
                            isNewPasswordVisible = false,
                            isCurrentPasswordVisible = false,
                            isPasswordChangeSuccessful = true
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.UNAUTHORIZED -> {
                            UiText.Resource(Res.string.error_current_password_incorrect)
                        }

                        DataError.Remote.CONFLICT -> {
                            UiText.Resource(Res.string.error_current_password_equal_to_new_one)
                        }

                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            newPasswordError = errorMessage,
                            isChangingPassword = false
                        )
                    }
                }
        }
    }

    private fun uploadProfilePicture(bytes: ByteArray, mimeType: String?) {
        if (state.value.isUploadingImage) {
            return
        }

        if (mimeType == null) {
            _state.update {
                it.copy(
                    imageError = UiText.Resource(Res.string.error_invalid_file_type)
                )
            }
            return
        }

        _state.update {
            it.copy(
                isUploadingImage = true,
                imageError = null
            )
        }

        viewModelScope.launch {
            participantRepository
                .uploadProfilePicture(
                    imageBytes = bytes,
                    mimeType = mimeType
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isUploadingImage = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            imageError = error.toUiText(),
                            isUploadingImage = false
                        )
                    }
                }
        }
    }

    private fun deleteProfilePicture() {
        if (state.value.isDeletingImage && state.value.profilePictureUrl == null) {
            return
        }

        _state.update {
            it.copy(
                isDeletingImage = true,
                imageError = null,
                showDeleteConfirmationDialog = false
            )
        }

        viewModelScope.launch {
            participantRepository
                .deleteProfilePicture()
                .onSuccess {
                    _state.update {
                        it.copy(
                            isDeletingImage = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            imageError = error.toUiText(),
                            isDeletingImage = false
                        )
                    }
                }
        }
    }

    private fun dismissDeleteConfirmation() {
        _state.update {
            it.copy(
                showDeleteConfirmationDialog = false
            )
        }
    }

    private fun showDeleteConfirmation() {
        _state.update {
            it.copy(
                showDeleteConfirmationDialog = true
            )
        }
    }
}