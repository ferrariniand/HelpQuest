package com.helpquest.core.designsystem.components.icons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.theme.extended
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.success_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HelpQuestSuccessIcon(
    modifier: Modifier = Modifier
        .height(80.dp)
        .fillMaxWidth()
) {
    Spacer(modifier = Modifier.height(16.dp))
    Icon(
        imageVector = vectorResource(Res.drawable.success_icon),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
    Spacer(modifier = Modifier.height(10.dp))
}