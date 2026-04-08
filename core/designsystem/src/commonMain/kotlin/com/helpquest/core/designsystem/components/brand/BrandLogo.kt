package com.helpquest.core.designsystem.components.brand

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.helpquest.core.designsystem.BuildKonfig
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.app_name
import helpquest.core.designsystem.generated.resources.content_description_enable_debug_mode
import org.jetbrains.compose.resources.stringResource


@Composable
fun BrandLogo(
    tintColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) {
    var logoContentDescription = stringResource(Res.string.app_name)
    var logoModifier = modifier
    var logoTint = tintColor

    //TODO: REPLACE WHEN IT WILL BE POSSIBLE TO IMPLEMENT BUILD VARIANTS AT MODULE LEVEL (CommonMainMock, CommonMainDev ...)
    if (BuildKonfig.IS_DEBUG) {
        // 1. Define the colors for the rainbow effect
        val rainbowColors = listOf(
            Color.Red,
            Color(0xFFFFA500), // Orange
            Color.Yellow,
            Color.Green,
            Color.Blue,
            Color(0xFF4B0082), // Indigo
            Color(0xFFEE82EE)  // Violet
        )

        // 2. State to trigger the animation on/off
        var triggerAnimation by remember { mutableStateOf(false) }
        val animatedColor = remember { Animatable(tintColor) }

        // 3. This LaunchedEffect runs the rainbow animation loop
        LaunchedEffect(triggerAnimation) {
            if (triggerAnimation) {
                for (color in rainbowColors) {
                    animatedColor.animateTo(
                        targetValue = color,
                        animationSpec = tween(
                            durationMillis = 800, // Speed of transition between colors
                            easing = LinearEasing
                        )
                    )
                }
                animatedColor.animateTo(
                    targetValue = tintColor,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
                triggerAnimation = false
            }
        }


        logoContentDescription = stringResource(Res.string.content_description_enable_debug_mode)
        logoModifier = modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    triggerAnimation = true
                }
            )
        logoTint = animatedColor.value
    }

    HelpQuestBrandLogoImplementation(
        contentDescription = logoContentDescription,
        tintColor = logoTint,
        modifier = logoModifier,
    )

}