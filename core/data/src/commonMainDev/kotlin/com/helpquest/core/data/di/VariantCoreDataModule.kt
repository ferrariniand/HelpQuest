package com.helpquest.core.data.di


import com.helpquest.core.data.auth.DataStoreSessionStorage
import com.helpquest.core.data.auth.KtorAuthService
import com.helpquest.core.data.networking.HttpClientFactory
import com.helpquest.core.data.service.KtorParticipantService
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.ParticipantService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val variantCoreDataModule = module {
    single {
        HttpClientFactory(get(), get()).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
    singleOf(::KtorParticipantService) bind ParticipantService::class
}