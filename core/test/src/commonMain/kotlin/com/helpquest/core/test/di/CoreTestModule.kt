package com.helpquest.core.test.di


import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.ParticipantRepository
import com.helpquest.core.domain.service.ParticipantService
import com.helpquest.core.test.auth.FakeAuthService
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.core.test.service.FakeParticipantRepository
import com.helpquest.core.test.service.FakeParticipantService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreTestModule = module {
    singleOf(::FakeAuthService) bind AuthService::class
    singleOf(::FakeSessionStorage) bind SessionStorage::class
    singleOf(::FakeParticipantService) bind ParticipantService::class
    singleOf(::FakeParticipantRepository) bind ParticipantRepository::class
}