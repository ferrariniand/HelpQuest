package com.helpquest.core.designsystem.components.selection_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDivider
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.designsystem.theme.titleXSmall
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.core.presentation.util.isKeyboardVisible
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.error_participant_not_found
import org.jetbrains.compose.resources.stringResource

@Composable
fun ParticipantsSearchSection(
    queryState: TextFieldState,
    searchTextPlaceholder: String,
    onFocusChanged: (Boolean) -> Unit,
    onDebouncedValueChange: (String) -> Unit,
    actionText: String,
    onActionClick: () -> Unit,
    isActionEnabled: Boolean,
    isLoading: Boolean,
    error: UiText? = null,
    searchResult: ParticipantSearchResult? = null,
    modifier: Modifier = Modifier,
) {
    val isKeyboardVisible by isKeyboardVisible()

    val deviceConfiguration = currentDeviceConfiguration()
    val shouldReducePadding = (deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE)
            && isKeyboardVisible

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = if (shouldReducePadding) 6.dp else 16.dp
                ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HelpQuestTextField(
                state = queryState,
                modifier = Modifier
                    .weight(1f),
                internalModifier = Modifier
                    .padding(vertical = if (shouldReducePadding) 0.dp else 3.dp),
                placeholder = searchTextPlaceholder,
                title = null,
                supportingText = error?.asString(),
                isError = error != null,
                singleLine = true,
                keyboardType = KeyboardType.Email,
                onFocusChanged = onFocusChanged,
                onDebouncedValueChange = onDebouncedValueChange,
            )
            HelpQuestButton(
                text = actionText,
                onClick = onActionClick,
                style = HelpQuestButtonStyle.SECONDARY,
                enabled = isActionEnabled,
                isLoading = isLoading,
                reduceVerticalPadding = shouldReducePadding
            )
        }
        HelpQuestHorizontalDivider()
        Box(
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                searchResult?.let {
                    item {
                        SearchResultItem(
                            participantSearchResult = searchResult,
                            shouldReducePadding = shouldReducePadding,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    participantSearchResult: ParticipantSearchResult,
    shouldReducePadding: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = if (shouldReducePadding) 6.dp else 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        participantSearchResult.getParticipantUiOrNull()?.let { participant ->
            HelpQuestAvatar(
                displayText = participant.initials,
                userImageUrl = participant.imageUrl,
                showUserIdentity = participant.showParticipantIdentity,
                classImageUrl = participant.classImageUrl,
                showClass = true
            )
        }
        val resultText = if (
            participantSearchResult is ParticipantSearchResult.Success
        ) {
            participantSearchResult.participant.username
        } else {
            stringResource(Res.string.error_participant_not_found)
        }
        val resultVerticalPadding = if (
            participantSearchResult.isSuccess()
        ) 0.dp else 12.dp

        Text(
            text = resultText,
            style = MaterialTheme.typography.titleXSmall,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(vertical = resultVerticalPadding)
        )
    }
}