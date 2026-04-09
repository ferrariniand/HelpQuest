package com.helpquest

import com.helpquest.notification.domain.service.PushNotificationService
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.coroutineContext

class FakePushNotificationService : PushNotificationService {

    override fun observeDeviceToken(): Flow<String?> = flow {
        try {
            emit("fakeDeviceToken")
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            emit(null)
        }
    }
}