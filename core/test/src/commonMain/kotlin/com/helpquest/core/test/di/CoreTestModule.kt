package com.helpquest.core.test.di


import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.test.auth.FakeAuthService
import com.helpquest.core.test.auth.FakeSessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreTestModule = module {
    singleOf(::FakeAuthService) bind AuthService::class
    singleOf(::FakeSessionStorage) bind SessionStorage::class
}