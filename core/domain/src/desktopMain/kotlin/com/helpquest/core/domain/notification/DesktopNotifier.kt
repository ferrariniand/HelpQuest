package com.helpquest.core.domain.notification

import kotlinx.coroutines.flow.Flow

interface DesktopNotifier {
    fun observeNewNotifications(): Flow<NotificationPayload>
}