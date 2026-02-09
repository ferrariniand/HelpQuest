package com.helpquest.notification.data.service

import com.helpquest.notification.domain.service.PushNotificationService
import kotlinx.coroutines.flow.Flow

actual class FirebasePushNotificationService :
    PushNotificationService {
    actual override fun observeDeviceToken(): Flow<String?> {
        TODO("Not yet implemented")
    }
}