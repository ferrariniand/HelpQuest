package com.helpquest

import androidx.compose.runtime.Composable
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.navigation.NavigationRoot
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HelpQuestTheme {

        NavigationRoot()
    }
}