package com.helpquest.notification.data.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformNotificationDataModule: Module

val notificationDataModule = module {
    includes(platformNotificationDataModule)
}