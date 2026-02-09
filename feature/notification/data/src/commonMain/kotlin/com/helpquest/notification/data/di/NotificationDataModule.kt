package com.helpquest.notification.data.di

import com.helpquest.notification.data.service.KtorDeviceTokenService
import com.helpquest.notification.domain.service.DeviceTokenService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformNotificationDataModule: Module

val notificationDataModule = module {
    includes(platformNotificationDataModule)

    singleOf(::KtorDeviceTokenService) bind DeviceTokenService::class
}