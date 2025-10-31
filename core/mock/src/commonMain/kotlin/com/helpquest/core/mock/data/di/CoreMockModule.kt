package com.helpquest.core.mock.data.di


import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.logging.HelpQuestLogger
import com.helpquest.core.mock.data.auth.MockAuthService
import com.helpquest.core.mock.data.auth.MockDataStoreSessionStorage
import com.helpquest.core.mock.data.logging.KermitLogger
import com.helpquest.core.mock.data.networking.MockHttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreMockModule: Module

val coreMockModule = module {
    includes(platformCoreMockModule)
    single<HelpQuestLogger> { KermitLogger }
    single {
        MockHttpClientFactory(get(), get(), get()).create(get())
    }
    singleOf(::MockAuthService) bind AuthService::class
    singleOf(::MockDataStoreSessionStorage) bind SessionStorage::class
}