package com.helpquest.core.designsystem.components.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.helpquest.core.designsystem.theme.extended
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.success_checkmark
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HelpQuestSuccessIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
}