package com.helpquest.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

val LightExtendedColors = ExtendedColors(
    primaryHover = HelpQuestBrand600,
    destructiveHover = HelpQuestRed600,
    destructiveSecondaryOutline = HelpQuestRed200,
    disabledOutline = HelpQuestBase200,
    disabledFill = HelpQuestBase150,
    successOutline = HelpQuestBrand100,
    success = HelpQuestBrand600,
    onSuccess = HelpQuestBase0,
    secondaryFill = HelpQuestBase100,

    textPrimary = HelpQuestBase1000,
    textTertiary = HelpQuestBase800,
    textSecondary = HelpQuestBase900,
    textPlaceholder = HelpQuestBase700,
    textDisabled = HelpQuestBase400,

    surfaceLower = HelpQuestBase100,
    surfaceHigher = HelpQuestBase100,
    surfaceOutline = HelpQuestBase1000Alpha14,
    overlay = HelpQuestBase1000Alpha80,

    accentBlue = HelpQuestBlue,
    accentPurple = HelpQuestPurple,
    accentViolet = HelpQuestViolet,
    accentPink = HelpQuestPink,
    accentOrange = HelpQuestOrange,
    accentYellow = HelpQuestYellow,
    accentGreen = HelpQuestGreen,
    accentTeal = HelpQuestTeal,
    accentLightBlue = HelpQuestLightBlue,
    accentGrey = HelpQuestGrey,

    cakeViolet = HelpQuestCakeLightViolet,
    cakeGreen = HelpQuestCakeLightGreen,
    cakeBlue = HelpQuestCakeLightBlue,
    cakePink = HelpQuestCakeLightPink,
    cakeOrange = HelpQuestCakeLightOrange,
    cakeYellow = HelpQuestCakeLightYellow,
    cakeTeal = HelpQuestCakeLightTeal,
    cakePurple = HelpQuestCakeLightPurple,
    cakeRed = HelpQuestCakeLightRed,
    cakeMint = HelpQuestCakeLightMint,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = HelpQuestBrand600,
    destructiveHover = HelpQuestRed600,
    destructiveSecondaryOutline = HelpQuestRed200,
    disabledOutline = HelpQuestBase900,
    disabledFill = HelpQuestBase1000,
    successOutline = HelpQuestBrand500Alpha40,
    success = HelpQuestBrand500,
    onSuccess = HelpQuestBase1000,
    secondaryFill = HelpQuestBase900,

    textPrimary = HelpQuestBase0,
    textTertiary = HelpQuestBase200,
    textSecondary = HelpQuestBase150,
    textPlaceholder = HelpQuestBase400,
    textDisabled = HelpQuestBase500,

    surfaceLower = HelpQuestBase1000,
    surfaceHigher = HelpQuestBase900,
    surfaceOutline = HelpQuestBase100Alpha10Alt,
    overlay = HelpQuestBase1000Alpha80,

    accentBlue = HelpQuestBlue,
    accentPurple = HelpQuestPurple,
    accentViolet = HelpQuestViolet,
    accentPink = HelpQuestPink,
    accentOrange = HelpQuestOrange,
    accentYellow = HelpQuestYellow,
    accentGreen = HelpQuestGreen,
    accentTeal = HelpQuestTeal,
    accentLightBlue = HelpQuestLightBlue,
    accentGrey = HelpQuestGrey,

    cakeViolet = HelpQuestCakeDarkViolet,
    cakeGreen = HelpQuestCakeDarkGreen,
    cakeBlue = HelpQuestCakeDarkBlue,
    cakePink = HelpQuestCakeDarkPink,
    cakeOrange = HelpQuestCakeDarkOrange,
    cakeYellow = HelpQuestCakeDarkYellow,
    cakeTeal = HelpQuestCakeDarkTeal,
    cakePurple = HelpQuestCakeDarkPurple,
    cakeRed = HelpQuestCakeDarkRed,
    cakeMint = HelpQuestCakeDarkMint,
)

val LightColorScheme = lightColorScheme(
    primary = HelpQuestBrand500Dev,
    onPrimary = HelpQuestBrand1000Dev,
    primaryContainer = HelpQuestBrand100,
    onPrimaryContainer = HelpQuestBrand900Dev,

    secondary = HelpQuestBase700,
    onSecondary = HelpQuestBase0,
    secondaryContainer = HelpQuestBase100,
    onSecondaryContainer = HelpQuestBase900,

    tertiary = HelpQuestBrand900Dev,
    onTertiary = HelpQuestBase0,
    tertiaryContainer = HelpQuestBrand100,
    onTertiaryContainer = HelpQuestBrand1000Dev,

    error = HelpQuestRed500,
    onError = HelpQuestBase0,
    errorContainer = HelpQuestRed200,
    onErrorContainer = HelpQuestRed600,

    background = HelpQuestBrand1000Dev,
    onBackground = HelpQuestBase0,
    surface = HelpQuestBase0,
    onSurface = HelpQuestBase1000,
    surfaceVariant = HelpQuestBase100,
    onSurfaceVariant = HelpQuestBase900,

    outline = HelpQuestBase1000Alpha8,
    outlineVariant = HelpQuestBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = HelpQuestBrand500Dev,
    onPrimary = HelpQuestBrand1000Dev,
    primaryContainer = HelpQuestBrand900Dev,
    onPrimaryContainer = HelpQuestBrand500,

    secondary = HelpQuestBase400,
    onSecondary = HelpQuestBase1000,
    secondaryContainer = HelpQuestBase900,
    onSecondaryContainer = HelpQuestBase150,

    tertiary = HelpQuestBrand500Dev,
    onTertiary = HelpQuestBase1000,
    tertiaryContainer = HelpQuestBrand900Dev,
    onTertiaryContainer = HelpQuestBrand500Dev,

    error = HelpQuestRed500,
    onError = HelpQuestBase0,
    errorContainer = HelpQuestRed600,
    onErrorContainer = HelpQuestRed200,

    background = HelpQuestBase1000,
    onBackground = HelpQuestBase0,
    surface = HelpQuestBase950,
    onSurface = HelpQuestBase0,
    surfaceVariant = HelpQuestBase900,
    onSurfaceVariant = HelpQuestBase150,

    outline = HelpQuestBase100Alpha10,
    outlineVariant = HelpQuestBase800,
)