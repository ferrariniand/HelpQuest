package com.helpquest.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.vectorResource


@Composable
fun HelpQuestBrandLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        //TODO: CREATE BRAND LOGO
        imageVector = vectorResource(Res.drawable.compose_multiplatform),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}