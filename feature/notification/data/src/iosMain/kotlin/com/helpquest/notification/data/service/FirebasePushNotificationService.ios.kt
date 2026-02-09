package com.helpquest.notification.data.service

import com.helpquest.notification.data.IosDeviceTokenHolder
import com.helpquest.notification.domain.service.PushNotificationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications

actual class FirebasePushNotificationService : PushNotificationService {
    actual override fun observeDeviceToken(): Flow<String?> {
        return IosDeviceTokenHolder
            .token
            .onStart {
                if (IosDeviceTokenHolder.token.value == null) {
                    //local Preferences for IOS
                    val userDefaults = NSUserDefaults.standardUserDefaults
                    val fcmToken = userDefaults.stringForKey("FCM_TOKEN")

                    if (fcmToken != null) {
                        IosDeviceTokenHolder.updateToken(fcmToken)
                    } else {
                        UIApplication.sharedApplication.registerForRemoteNotifications()
                    }
                }
            }
    }
}