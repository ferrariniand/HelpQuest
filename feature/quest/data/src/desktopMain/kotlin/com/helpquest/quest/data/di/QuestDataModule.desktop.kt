package com.helpquest.quest.data.di


import com.helpquest.core.domain.notification.DesktopNotifier
import com.helpquest.quest.data.notification.QuestDesktopNotifier
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformQuestDataModule = module {
    singleOf(::QuestDesktopNotifier) bind DesktopNotifier::class
}