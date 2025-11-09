package com.helpquest.core.designsystem.components.selection_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDivider
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.core.presentation.util.isKeyboardVisible

@Composable
fun <T> SingleSearchSection(
    queryState: TextFieldState,
    searchTextPlaceholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusChanged: (Boolean) -> Unit,
    onDebouncedValueChange: (String) -> Unit,
    actionText: String,
    onActionClick: () -> Unit,
    isActionEnabled: Boolean,
    isLoading: Boolean,
    error: UiText? = null,
    searchResult: SearchResult<T>? = null,
    resultItemContent: @Composable RowScope.(T) -> Unit,
    notFoundItemContent: @Composable RowScope.() -> Unit,
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
                    horizontal = 16.dp,
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
                keyboardType = keyboardType,
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
                .padding(
                    vertical = when {
                        searchResult == null -> 0.dp
                        shouldReducePadding -> 4.dp
                        else -> 8.dp
                    }
                ),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                searchResult?.let { searchResult ->
                    searchResult.getSearchResultOrNull()?.let { resultItem ->
                        item {
                            SingleSearchResultItem(
                                item = resultItem,
                                shouldReducePadding = shouldReducePadding,
                                resultItemContent = resultItemContent,
                                notFoundItemContent = notFoundItemContent,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> SingleSearchResultItem(
    item: T?,
    shouldReducePadding: Boolean,
    resultItemContent: @Composable RowScope.(T) -> Unit,
    notFoundItemContent: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = if (shouldReducePadding) 4.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item?.let {
            resultItemContent(it)
        } ?: run {
            notFoundItemContent()
        }
    }
}