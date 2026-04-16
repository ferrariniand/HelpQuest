package com.helpquest.chat.data.di


import com.helpquest.chat.data.notification.ChatDesktopNotifier
import com.helpquest.core.domain.notification.DesktopNotifier
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformChatDataModule = module {
    singleOf(::ChatDesktopNotifier) bind DesktopNotifier::class
}