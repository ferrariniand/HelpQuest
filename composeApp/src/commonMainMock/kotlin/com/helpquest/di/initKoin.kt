package com.helpquest.di

import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.core.presentation.di.corePresentationModule
import com.helpquest.core.test.di.coreTestModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            coreTestModule, //TODO: REPLACE WITH core.mock MODULE
            corePresentationModule,
            authPresentationModule
        )
    }
}