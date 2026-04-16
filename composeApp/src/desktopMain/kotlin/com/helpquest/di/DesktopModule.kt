package com.helpquest.di

import com.helpquest.ApplicationStateHolder
import com.helpquest.core.domain.notification.DesktopNotifier
import com.helpquest.notification.ApplicationDesktopNotifier
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val desktopModule = module {
    singleOf(::ApplicationStateHolder)
    singleOf(::ApplicationDesktopNotifier) bind DesktopNotifier::class
}