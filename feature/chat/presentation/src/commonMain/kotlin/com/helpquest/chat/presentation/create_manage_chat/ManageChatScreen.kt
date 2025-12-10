package com.helpquest.chat.presentation.create_manage_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.dialogs.DialogSheetButtonSection
import com.helpquest.core.designsystem.components.dialogs.DialogSheetHeaderRow
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDivider
import com.helpquest.core.designsystem.components.selection_sections.MultipleSearchSection
import com.helpquest.core.designsystem.components.selection_sections.ParticipantsSelectionSection
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.designsystem.theme.titleXSmall
import com.helpquest.core.presentation.util.clearFocusOnTap
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.core.presentation.util.isKeyboardVisible
import helpquest.feature.chat.presentation.generated.resources.Res
import helpquest.feature.chat.presentation.generated.resources.add
import helpquest.feature.chat.presentation.generated.resources.cancel
import helpquest.feature.chat.presentation.generated.resources.email_or_username
import helpquest.feature.chat.presentation.generated.resources.error_participant_not_found
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ManageChatScreen(
    headerText: String,
    primaryButtonText: String,
    state: ManageChatState,
    onAction: (ManageChatAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val isKeyboardVisible by isKeyboardVisible()

    val shouldHideHeader = configuration.isSmallScreenHeight || isKeyboardVisible

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !shouldHideHeader
        ) {
            Column {
                DialogSheetHeaderRow(
                    title = headerText,
                    onCloseClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                HelpQuestHorizontalDivider()
            }
        }
        MultipleSearchSection(
            queryState = state.queryTextState,
            searchTextPlaceholder = stringResource(Res.string.email_or_username),
            keyboardType = KeyboardType.Email,
            onFocusChanged = {},
            onDebouncedValueChange = {
                onAction(ManageChatAction.OnDebounceSearchTextField)
            },
            actionText = stringResource(Res.string.add),
            onActionClick = { participant ->
                onAction(ManageChatAction.OnAddClick(participant))
            },
            isLoading = state.isSearching,
            error = state.searchError,
            searchResult = state.currentSearchResult,
            itemListKey = { participant ->
                participant.id
            },
            actionEnabledCondition = { participant ->
                state.selectedChatParticipants.contains(participant).not()
                        && state.existingChatParticipants.contains(participant).not()
            },
            resultItemContent = { participant ->
                HelpQuestAvatar(
                    displayText = participant.initials,
                    userImageUrl = participant.imageUrl,
                    showUserIdentity = participant.showParticipantIdentity,
                    classImageUrl = participant.classImageUrl,
                    showClass = true
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = participant.username,
                    style = MaterialTheme.typography.titleXSmall,
                    color = MaterialTheme.colorScheme.extended.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            notFoundItemContent = {
                Text(
                    text = stringResource(Res.string.error_participant_not_found),
                    style = MaterialTheme.typography.titleXSmall,
                    color = MaterialTheme.colorScheme.extended.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        ParticipantsSelectionSection(
            existingParticipants = state.existingChatParticipants,
            selectedParticipants = state.selectedChatParticipants,
            modifier = Modifier
                .fillMaxWidth()
        )
        AnimatedVisibility(
            visible = !isKeyboardVisible
        ) {
            Column {
                HelpQuestHorizontalDivider()
                DialogSheetButtonSection(
                    primaryButton = {
                        HelpQuestButton(
                            text = primaryButtonText,
                            onClick = {
                                onAction(ManageChatAction.OnPrimaryActionClick)
                            },
                            enabled = state.selectedChatParticipants.isNotEmpty(),
                            isLoading = state.isSubmitting
                        )
                    },
                    secondaryButton = {
                        HelpQuestButton(
                            text = stringResource(Res.string.cancel),
                            onClick = {
                                onAction(ManageChatAction.OnDismissDialog)
                            },
                            style = HelpQuestButtonStyle.SECONDARY
                        )
                    },
                    error = state.submitError?.asString(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun CreateChatScreenLightPreview() {
    HelpQuestTheme {
        ManageChatScreen(
            headerText = "Create Chat",
            primaryButtonText = "Create Chat",
            state = ManageChatState(),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun CreateChatScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ManageChatScreen(
            headerText = "Create Chat",
            primaryButtonText = "Create Chat",
            state = ManageChatState(),
            onAction = {}
        )
    }
}