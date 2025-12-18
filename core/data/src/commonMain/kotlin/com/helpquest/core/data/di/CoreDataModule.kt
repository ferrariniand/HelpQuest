package com.helpquest.core.data.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.helpquest.core.data.logging.KermitLogger
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.DatabaseFactory
import com.helpquest.core.domain.logging.HelpQuestLogger
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    includes(variantCoreDataModule)
    single<HelpQuestLogger> { KermitLogger }
    singleOf(::KtorWebSocketConnector)
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}