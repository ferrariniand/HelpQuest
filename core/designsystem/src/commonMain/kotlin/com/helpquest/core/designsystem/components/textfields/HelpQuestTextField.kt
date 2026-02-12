package com.helpquest.core.designsystem.components.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.result_layouts.Loader
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HelpQuestTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    internalModifier: Modifier = Modifier,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isLoading: Boolean = false,
    isError: Boolean = false,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusChanged: (Boolean) -> Unit = {},
    onDebouncedValueChange: (String) -> Unit = {},
) {
    HelpQuestTextFieldLayout(
        title = title,
        isError = isError,
        supportingText = supportingText,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
        currentValue = state.text as String,
        onDebouncedValueChange = onDebouncedValueChange,
        modifier = modifier
    ) { styleModifier, interactionSource ->
        val focusRequester = remember { FocusRequester() }
        Row(
            modifier = styleModifier
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    focusRequester.requestFocus()
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                state = state,
                enabled = enabled,
                lineLimits = if (singleLine) {
                    TextFieldLineLimits.SingleLine
                } else TextFieldLineLimits.Default,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.extended.textPlaceholder
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                interactionSource = interactionSource,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                decorator = { innerBox ->
                    Box(
                        modifier = internalModifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.extended.textPlaceholder,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        innerBox()
                    }
                }
            )
            Loader(
                isLoading,
                size = 22.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestTextFieldEmptyLightPreview() {
    HelpQuestTheme {
        HelpQuestTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "no email",
            title = "Email",
            supportingText = "Please enter your email",
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestTextFieldEmptyDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "no email",
            title = "Email",
            supportingText = "Please enter your email",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestTextFieldFilledLightPreview() {
    HelpQuestTheme {
        HelpQuestTextField(
            state = rememberTextFieldState(
                initialText = "initial@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "no email",
            title = "Email",
            supportingText = "Please enter your email",
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestTextFieldFilledDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestTextField(
            state = rememberTextFieldState(
                initialText = "initial@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "no email",
            title = "Email",
            supportingText = "Please enter your email",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestTextFieldDisabledLightPreview() {
    HelpQuestTheme {
        HelpQuestTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "disabled",
            title = "Email",
            supportingText = "Please enter your email",
            enabled = false
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestTextFieldDisabledDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "disabled",
            title = "Email",
            supportingText = "Please enter your email",
            enabled = false
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestTextFieldErrorLightPreview() {
    HelpQuestTheme {
        HelpQuestTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "error",
            title = "Email",
            supportingText = "Please enter your email",
            isError = true,
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestTextFieldErrorDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "error",
            title = "Email",
            supportingText = "Please enter your email",
            isError = true,
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestTextFieldLoaderLightPreview() {
    HelpQuestTheme {
        HelpQuestTextField(
            state = rememberTextFieldState(
                initialText = "initial@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "no email",
            title = "Email",
            supportingText = "Please enter your email",
            isLoading = true
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestTextFieldLoaderDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestTextField(
            state = rememberTextFieldState(
                initialText = "initial@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "no email",
            title = "Email",
            supportingText = "Please enter your email",
            isLoading = true
        )
    }
}