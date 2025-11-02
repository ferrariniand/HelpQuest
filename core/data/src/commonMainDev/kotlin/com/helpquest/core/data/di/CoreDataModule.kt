package com.helpquest.core.data.di


import com.helpquest.core.data.auth.DataStoreSessionStorage
import com.helpquest.core.data.auth.KtorAuthService
import com.helpquest.core.data.logging.KermitLogger
import com.helpquest.core.data.networking.HttpClientFactory
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.logging.HelpQuestLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<HelpQuestLogger> { KermitLogger }
    single {
        HttpClientFactory(get(), get()).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}