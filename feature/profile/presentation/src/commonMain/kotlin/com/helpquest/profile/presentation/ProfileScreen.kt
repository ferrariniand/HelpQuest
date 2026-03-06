package com.helpquest.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.avatar.AvatarSize
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.helpquest.core.designsystem.components.dialogs.HelpQuestAdaptiveDialogSheetLayout
import com.helpquest.core.designsystem.components.drag_and_drop.DragAndDropOverlay
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDivider
import com.helpquest.core.designsystem.components.textfields.HelpQuestPasswordTextField
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.mediapicker.rememberDragAndDropTarget
import com.helpquest.core.presentation.mediapicker.rememberImagePickerLauncher
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.clearFocusOnTap
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.profile.presentation.components.ProfileHeaderSection
import com.helpquest.profile.presentation.components.ProfileSectionLayout
import helpquest.core.designsystem.generated.resources.cancel
import helpquest.core.designsystem.generated.resources.email
import helpquest.core.designsystem.generated.resources.new_password
import helpquest.core.designsystem.generated.resources.password
import helpquest.core.designsystem.generated.resources.password_hint
import helpquest.core.designsystem.generated.resources.save
import helpquest.core.designsystem.generated.resources.upload_icon
import helpquest.core.designsystem.generated.resources.upload_image
import helpquest.feature.profile.presentation.generated.resources.Res
import helpquest.feature.profile.presentation.generated.resources.contact_chirp_support_change_email
import helpquest.feature.profile.presentation.generated.resources.current_password
import helpquest.feature.profile.presentation.generated.resources.delete
import helpquest.feature.profile.presentation.generated.resources.delete_profile_picture
import helpquest.feature.profile.presentation.generated.resources.delete_profile_picture_desc
import helpquest.feature.profile.presentation.generated.resources.password_change_successful
import helpquest.feature.profile.presentation.generated.resources.password_change_warning
import helpquest.feature.profile.presentation.generated.resources.profile_image
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ProfileRoot(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberImagePickerLauncher { pickedImageData ->
        viewModel.onAction(
            ProfileAction.OnPictureSelected(
                pickedImageData.bytes,
                pickedImageData.mimeType
            )
        )
    }

    HelpQuestAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ProfileScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    is ProfileAction.OnDismiss -> onDismiss()
                    is ProfileAction.OnUploadPictureClick -> {
                        launcher.launch()
                    }
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    var isHoveringWithFile by remember {
        mutableStateOf(false)
    }
    val dragAndDropTarget = rememberDragAndDropTarget(
        onHover = { isHovered ->
            isHoveringWithFile = isHovered
        },
        onDrop = { imageData ->
            onAction(
                ProfileAction.OnPictureSelected(
                    bytes = imageData.bytes,
                    mimeType = imageData.mimeType
                )
            )
        }
    )

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .verticalScroll(rememberScrollState())
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget
            )
    ) {
        ProfileHeaderSection(
            username = state.username,
            onCloseClick = {
                onAction(ProfileAction.OnDismiss)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 20.dp
                )
        )
        HelpQuestHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.profile_image)
        ) {
            Row {
                HelpQuestAvatar(
                    displayText = state.userInitials,
                    size = AvatarSize.LARGE,
                    userImageUrl = state.profilePictureUrl,
                    showUserIdentity = true,
                    classImageUrl = state.classImageUrl,
                    showClass = true,
                    onClick = {
                        onAction(ProfileAction.OnUploadPictureClick)
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                FlowRow(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HelpQuestButton(
                        text = stringResource(DesignSystemRes.string.upload_image),
                        onClick = {
                            onAction(ProfileAction.OnUploadPictureClick)
                        },
                        style = HelpQuestButtonStyle.SECONDARY,
                        enabled = !state.isUploadingImage && !state.isDeletingImage,
                        isLoading = state.isUploadingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(DesignSystemRes.drawable.upload_icon),
                                contentDescription = stringResource(DesignSystemRes.string.upload_image)
                            )
                        }
                    )
                    HelpQuestButton(
                        text = stringResource(Res.string.delete),
                        onClick = {
                            onAction(ProfileAction.OnDeletePictureClick)
                        },
                        style = HelpQuestButtonStyle.DESTRUCTIVE_SECONDARY,
                        enabled = !state.isUploadingImage
                                && !state.isDeletingImage
                                && state.profilePictureUrl != null,
                        isLoading = state.isDeletingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(Res.string.delete)
                            )
                        }
                    )
                }
            }

            if (state.imageError != null) {
                Text(
                    text = state.imageError.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        HelpQuestHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(DesignSystemRes.string.email)
        ) {
            HelpQuestTextField(
                state = state.emailTextState,
                enabled = false,
                supportingText = stringResource(Res.string.contact_chirp_support_change_email)
            )
        }
        HelpQuestHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(DesignSystemRes.string.password)
        ) {
            HelpQuestPasswordTextField(
                state = state.currentPasswordTextState,
                isPasswordVisible = state.isCurrentPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleCurrentPasswordVisibility)
                },
                placeholder = stringResource(Res.string.current_password),
                isError = state.newPasswordError != null,
            )
            HelpQuestPasswordTextField(
                state = state.newPasswordTextState,
                isPasswordVisible = state.isNewPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleNewPasswordVisibility)
                },
                placeholder = stringResource(DesignSystemRes.string.new_password),
                isError = state.newPasswordError != null,
                supportingText = state.newPasswordError?.asString()
                    ?: stringResource(DesignSystemRes.string.password_hint)
            )
            if (state.isPasswordChangeSuccessful) {
                Text(
                    text = stringResource(Res.string.password_change_successful),
                    color = MaterialTheme.colorScheme.extended.success,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.extended.warning,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = stringResource(Res.string.password_change_warning),
                        color = MaterialTheme.colorScheme.extended.textPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
            ) {
                HelpQuestButton(
                    text = stringResource(DesignSystemRes.string.cancel),
                    style = HelpQuestButtonStyle.SECONDARY,
                    onClick = {
                        onAction(ProfileAction.OnDismiss)
                    }
                )
                HelpQuestButton(
                    text = stringResource(DesignSystemRes.string.save),
                    onClick = {
                        onAction(ProfileAction.OnChangePasswordClick)
                    },
                    enabled = state.canChangePassword,
                    isLoading = state.isChangingPassword
                )
            }
        }
        val deviceConfiguration = currentDeviceConfiguration()
        if (deviceConfiguration in listOf(
                DeviceConfiguration.MOBILE_PORTRAIT,
                DeviceConfiguration.MOBILE_LANDSCAPE
            )
        ) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    if (isHoveringWithFile) {
        DragAndDropOverlay()
    }

    if (state.showDeleteConfirmationDialog) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.delete_profile_picture),
            description = stringResource(Res.string.delete_profile_picture_desc),
            confirmButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(DesignSystemRes.string.cancel),
            onConfirmClick = {
                onAction(ProfileAction.OnConfirmDeleteClick)
            },
            onCancelClick = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialogClick)
            },
            onDismiss = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialogClick)
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun ProfileScreenLightPreview() {
    HelpQuestTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun ProfileScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}