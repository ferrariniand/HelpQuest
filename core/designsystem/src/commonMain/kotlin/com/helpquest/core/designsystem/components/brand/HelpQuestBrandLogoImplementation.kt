package com.helpquest.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.vectorResource


@Composable
fun HelpQuestBrandLogoImplementation(
    contentDescription: String?,
    tintColor: Color,
    modifier: Modifier
) {
    Icon(
        //TODO: CREATE BRAND LOGO
        imageVector = vectorResource(Res.drawable.compose_multiplatform),
        contentDescription = contentDescription,
        tint = tintColor,
        modifier = modifier
    )
}