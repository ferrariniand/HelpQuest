package com.helpquest.core.designsystem.components.selection_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDividerWithTitle
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.search_results
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> MultipleSearchSection(
    queryState: TextFieldState,
    searchTextPlaceholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusChanged: (Boolean) -> Unit,
    onDebouncedValueChange: (String) -> Unit,
    actionText: String,
    onActionClick: (T) -> Unit,
    isLoading: Boolean,
    error: UiText? = null,
    searchResult: SearchResult<List<T>>? = null,
    itemListKey: ((item: T) -> Any)? = null,
    actionEnabledCondition: (item: T) -> Boolean,
    resultItemContent: @Composable RowScope.(T) -> Unit,
    notFoundItemContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val isSmallScreenHeight = deviceConfiguration.isSmallScreenHeight

    Column(
        modifier = modifier
    ) {
        HelpQuestTextField(
            state = queryState,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = if (isSmallScreenHeight) 8.dp else 16.dp
                ),
            internalModifier = Modifier
                .padding(vertical = if (isSmallScreenHeight) 0.dp else 3.dp),
            placeholder = searchTextPlaceholder,
            title = null,
            supportingText = error?.asString(),
            isLoading = isLoading,
            isError = error != null,
            singleLine = true,
            keyboardType = keyboardType,
            onFocusChanged = onFocusChanged,
            onDebouncedValueChange = onDebouncedValueChange,
        )
        if (searchResult != null) {
            HelpQuestHorizontalDividerWithTitle(
                title = stringResource(Res.string.search_results),
            )
        }
        Box(
            modifier = Modifier
                .padding(
                    vertical = when {
                        searchResult == null -> 0.dp
                        isSmallScreenHeight -> 4.dp
                        else -> 8.dp
                    }
                ),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                searchResult?.let { result ->
                    result.getSearchResultOrNull()?.let { itemList ->
                        items(
                            items = itemList,
                            key = itemListKey
                        ) { item ->
                            MultipleSearchResultItem(
                                item = item,
                                isSmallScreenHeight = isSmallScreenHeight,
                                resultItemContent = resultItemContent,
                                actionText = actionText,
                                onActionClick = onActionClick,
                                actionEnabledCondition = actionEnabledCondition
                            )
                        }
                    } ?: run {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = if (isSmallScreenHeight) 4.dp else 8.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                notFoundItemContent()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> MultipleSearchResultItem(
    item: T,
    isSmallScreenHeight: Boolean,
    resultItemContent: @Composable RowScope.(T) -> Unit,
    actionText: String,
    onActionClick: (T) -> Unit,
    actionEnabledCondition: (item: T) -> Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = if (isSmallScreenHeight) 4.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            resultItemContent(item)
        }
        Spacer(Modifier.width(12.dp))
        HelpQuestButton(
            text = actionText,
            onClick = {
                onActionClick(item)
            },
            enabled = actionEnabledCondition(item),
            style = HelpQuestButtonStyle.PRIMARY,
            reduceVerticalPadding = isSmallScreenHeight,
        )
    }
}