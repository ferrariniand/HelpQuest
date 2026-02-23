package com.helpquest.core.data.di

import com.helpquest.core.data.lifecycle.AppLifecycleObserver
import com.helpquest.core.data.networking.ConnectionErrorHandler
import com.helpquest.core.data.networking.ConnectivityObserver
import com.helpquest.core.database.DatabaseFactory
import eu.anifantakis.lib.ksafe.KSafe
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single { KSafe() }
    single { DatabaseFactory() }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
}