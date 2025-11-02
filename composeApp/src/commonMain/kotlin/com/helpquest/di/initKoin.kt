package com.helpquest.di

import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.core.data.di.coreDataModule
import com.helpquest.core.presentation.di.corePresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            coreDataModule,
            corePresentationModule,
            authPresentationModule
        )
    }
}