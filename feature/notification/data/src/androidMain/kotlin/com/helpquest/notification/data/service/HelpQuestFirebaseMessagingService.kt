package com.helpquest.notification.data.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.helpquest.core.data.util.ANDROID_OS_NAME
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.notification.DeviceTokenService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HelpQuestFirebaseMessagingService : FirebaseMessagingService() {

    private val deviceTokenService by inject<DeviceTokenService>()
    private val sessionStorage by inject<SessionStorage>()
    private val applicationScope by inject<CoroutineScope>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        applicationScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            if (authInfo != null) {
                deviceTokenService.registerToken(
                    token = token,
                    platform = ANDROID_OS_NAME
                )
            }
        }
    }
}