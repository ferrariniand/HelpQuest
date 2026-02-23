package com.helpquest.core.data.di

import com.helpquest.core.domain.service.participant.ParticipantService
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.service.notification.DeviceTokenService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.logging.HelpQuestLogger
import com.helpquest.core.data.service.MockCoreResponseElements
import com.helpquest.core.data.service.participant.MockParticipantService
import com.helpquest.core.data.service.auth.MockAuthService
import com.helpquest.core.data.service.notification.MockDeviceTokenService
import com.helpquest.core.data.logging.KermitLogger
import com.helpquest.core.data.networking.MockHttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val variantCoreDataModule = module {
    single {
        MockHttpClientFactory(get(), get(), get()).create(get())
    }
    singleOf(::MockAuthService) bind AuthService::class
    single { MockCoreResponseElements }
    singleOf(::MockParticipantService) bind ParticipantService::class
    singleOf(::MockDeviceTokenService) bind DeviceTokenService::class
}