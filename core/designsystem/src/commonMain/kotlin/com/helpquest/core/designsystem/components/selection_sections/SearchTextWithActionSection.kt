package com.helpquest.core.designsystem.components.selection_sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.core.presentation.util.isKeyboardVisible

@Composable
fun SearchTextWithActionSection(
    queryState: TextFieldState,
    searchTextPlaceholder: String,
    onFocusChanged: (Boolean) -> Unit,
    onDebouncedValueChange: (String) -> Unit,
    actionText: String,
    onActionClick: () -> Unit,
    isActionEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: UiText? = null,
) {
    val isKeyboardVisible by isKeyboardVisible()

    val deviceConfiguration = currentDeviceConfiguration()
    val shouldReducePadding = (deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE)
            && isKeyboardVisible

    Row(
        modifier = modifier
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
}