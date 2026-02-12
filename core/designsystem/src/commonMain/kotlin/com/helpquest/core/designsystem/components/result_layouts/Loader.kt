package com.helpquest.core.designsystem.components.result_layouts

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Loader(
    isLoading: Boolean,
    size: Dp = 15.dp,
    color: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator( //TODO: Customize this loader
        modifier = modifier
            .size(size)
            .alpha(
                alpha = if (isLoading) 1f else 0f
            ),
        strokeWidth = 1.5.dp,
        color = color
    )
}