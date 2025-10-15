package com.helpquest

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.navigation.DeepLinkListener
import com.helpquest.navigation.NavigationRoot
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HelpQuestTheme {
        val navController = rememberNavController()
        DeepLinkListener(navController)

        NavigationRoot(navController)
    }
}