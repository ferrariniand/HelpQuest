package com.helpquest.core.test.di


import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.auth.AuthRepository
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.service.notification.DeviceTokenService
import com.helpquest.core.domain.service.participant.ParticipantRepository
import com.helpquest.core.domain.service.participant.ParticipantService
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.core.test.service.auth.FakeAuthRepository
import com.helpquest.core.test.service.auth.FakeAuthService
import com.helpquest.core.test.service.notification.FakeDeviceTokenService
import com.helpquest.core.test.service.participant.FakeParticipantRepository
import com.helpquest.core.test.service.participant.FakeParticipantService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreTestModule = module {
    singleOf(::FakeAuthService) bind AuthService::class
    singleOf(::FakeAuthRepository) bind AuthRepository::class
    singleOf(::FakeSessionStorage) bind SessionStorage::class
    singleOf(::FakeParticipantService) bind ParticipantService::class
    singleOf(::FakeParticipantRepository) bind ParticipantRepository::class
    singleOf(::FakeDeviceTokenService) bind DeviceTokenService::class
}