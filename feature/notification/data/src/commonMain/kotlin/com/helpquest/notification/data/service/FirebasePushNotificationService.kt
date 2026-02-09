package com.helpquest.notification.data.service

import com.helpquest.notification.domain.service.PushNotificationService
import kotlinx.coroutines.flow.Flow

expect class FirebasePushNotificationService : PushNotificationService {
    override fun observeDeviceToken(): Flow<String?>
}