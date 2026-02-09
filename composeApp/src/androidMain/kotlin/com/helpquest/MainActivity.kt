package com.helpquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.helpquest.navigation.ExternalUriHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition {
            shouldShowSplashScreen
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        //called when the app is opened (after a notification)
        handleChatMessageDeeplink(intent)
        //TODO: add logic to understand what is the intent and act accordingly
        //handleQuestUpdateDeeplink(intent)

        setContent {
            App(
                onAuthenticationChecked = {
                    shouldShowSplashScreen = false
                }
            )
        }
    }

    //called when the app is already open and a new notification arrives
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //TODO: add logic to understand what is the intent and act accordingly
        handleChatMessageDeeplink(intent)
        //handleQuestUpdateDeeplink(intent)
    }

    private fun handleChatMessageDeeplink(intent: Intent) {
        val chatId = intent.getStringExtra("chatId")
            ?: intent.extras?.getString("chatId")

        if (chatId != null) {
            val deepLinkUrl = "helpquest://chat_detail/$chatId"
            ExternalUriHandler.onNewUri(deepLinkUrl)
        }
    }

    private fun handleQuestUpdateDeeplink(intent: Intent) {
        val questId = intent.getStringExtra("questId")
            ?: intent.extras?.getString("questId")

        if (questId != null) {
            val deepLinkUrl = "helpquest://quest_detail/$questId"
            ExternalUriHandler.onNewUri(deepLinkUrl)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}