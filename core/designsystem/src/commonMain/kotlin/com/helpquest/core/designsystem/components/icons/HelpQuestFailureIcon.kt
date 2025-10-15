package com.helpquest.core.designsystem.components.icons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.failure_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HelpQuestFailureIcon(
    modifier: Modifier = Modifier
        .height(80.dp)
        .fillMaxWidth()
) {
    Spacer(modifier = Modifier.height(16.dp))
    Icon(
        imageVector = vectorResource(Res.drawable.failure_icon),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
    Spacer(modifier = Modifier.height(16.dp))

}