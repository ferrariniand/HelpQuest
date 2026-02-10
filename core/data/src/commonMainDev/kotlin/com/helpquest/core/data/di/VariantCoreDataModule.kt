package com.helpquest.core.data.di


import com.helpquest.core.data.auth.DataStoreSessionStorage
import com.helpquest.core.data.networking.HttpClientFactory
import com.helpquest.core.data.service.auth.KtorAuthService
import com.helpquest.core.data.service.notification.KtorDeviceTokenService
import com.helpquest.core.data.service.participant.KtorParticipantService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.service.notification.DeviceTokenService
import com.helpquest.core.domain.service.participant.ParticipantService
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
    singleOf(::KtorDeviceTokenService) bind DeviceTokenService::class
}