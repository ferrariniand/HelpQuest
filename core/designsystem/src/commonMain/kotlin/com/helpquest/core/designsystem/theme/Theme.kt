package com.helpquest.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.helpquest.core.designsystem.BuildKonfig
import com.helpquest.core.designsystem.Environment

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Warning colors
    val warning: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

//TODO: replace colors (at the moment are used the colors taken from PL Chirp Project)
//TODO: REPLACE BuildKonfig WHEN IT WILL BE POSSIBLE TO IMPLEMENT BUILD VARIANTS AT MODULE LEVEL (CommonMainMock, CommonMainDev ...)

val LightExtendedColors = when (BuildKonfig.FLAVOR_ENV) {
    Environment.Mock.name -> LightExtendedColorsMock
    Environment.Dev.name -> LightExtendedColorsDev
    Environment.Stage.name -> LightExtendedColorsStage
    else -> LightExtendedColorsDefault
}

val DarkExtendedColors = when (BuildKonfig.FLAVOR_ENV) {
    Environment.Mock.name -> DarkExtendedColorsMock
    Environment.Dev.name -> DarkExtendedColorsDev
    Environment.Stage.name -> DarkExtendedColorsStage
    else -> DarkExtendedColorsDefault
}

val LightColorScheme = when (BuildKonfig.FLAVOR_ENV) {
    Environment.Mock.name -> LightColorSchemeMock
    Environment.Dev.name -> LightColorSchemeDev
    Environment.Stage.name -> LightColorSchemeStage
    else -> LightColorSchemeDefault
}

val DarkColorScheme = when (BuildKonfig.FLAVOR_ENV) {
    Environment.Mock.name -> DarkColorSchemeMock
    Environment.Dev.name -> DarkColorSchemeDev
    Environment.Stage.name -> DarkColorSchemeStage
    else -> DarkColorSchemeDefault
}