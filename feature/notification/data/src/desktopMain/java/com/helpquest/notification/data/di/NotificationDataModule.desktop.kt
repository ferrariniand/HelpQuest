package com.helpquest.notification.data.di

import com.helpquest.notification.data.service.FirebasePushNotificationService
import com.helpquest.notification.domain.service.PushNotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformNotificationDataModule = module {
    singleOf(::FirebasePushNotificationService) bind PushNotificationService::class
}