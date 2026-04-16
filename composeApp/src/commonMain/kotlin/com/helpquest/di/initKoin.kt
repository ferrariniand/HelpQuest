package com.helpquest.di

import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.chat.data.di.chatDataModule
import com.helpquest.chat.presentation.di.chatPresentationModule
import com.helpquest.core.data.di.coreDataModule
import com.helpquest.core.presentation.di.corePresentationModule
import com.helpquest.home.presentation.di.homepagePresentationModule
import com.helpquest.notification.data.di.notificationDataModule
import com.helpquest.profile.presentation.di.profilePresentationModule
import com.helpquest.quest.data.di.questDataModule
import com.helpquest.quest.presentation.di.questPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            coreDataModule,
            corePresentationModule,
            authPresentationModule,
            chatPresentationModule,
            chatDataModule,
            homepagePresentationModule,
            questDataModule,
            questPresentationModule,
            profilePresentationModule,
            notificationDataModule
        )
    }
}