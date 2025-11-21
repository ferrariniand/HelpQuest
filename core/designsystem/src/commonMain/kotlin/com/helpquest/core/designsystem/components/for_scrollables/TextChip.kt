package com.helpquest.core.designsystem.components.for_scrollables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TextChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(100)
            )
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 12.dp
                ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.extended.textPlaceholder
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun TextChipLightPreview() {
    HelpQuestTheme {
        TextChip(
            text = "20 Nov"
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun TextChipDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        TextChip(
            text = "20 Nov"
        )
    }
}