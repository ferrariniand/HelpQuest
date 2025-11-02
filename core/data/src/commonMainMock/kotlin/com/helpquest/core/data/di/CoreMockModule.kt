package com.helpquest.core.data.di


import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.logging.HelpQuestLogger
import com.helpquest.core.data.auth.MockAuthService
import com.helpquest.core.data.auth.MockDataStoreSessionStorage
import com.helpquest.core.data.logging.KermitLogger
import com.helpquest.core.data.networking.MockHttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<HelpQuestLogger> { KermitLogger }
    single {
        MockHttpClientFactory(get(), get(), get()).create(get())
    }
    singleOf(::MockAuthService) bind AuthService::class
    singleOf(::MockDataStoreSessionStorage) bind SessionStorage::class
}