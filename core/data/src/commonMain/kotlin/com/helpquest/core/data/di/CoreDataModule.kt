package com.helpquest.core.data.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.helpquest.core.data.logging.KermitLogger
import com.helpquest.core.data.networking.ConnectionRetryHandler
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.data.service.auth.OfflineFirstAuthRepository
import com.helpquest.core.data.service.participant.OfflineFirstParticipantRepository
import com.helpquest.core.database.DatabaseFactory
import com.helpquest.core.domain.logging.HelpQuestLogger
import com.helpquest.core.domain.service.auth.AuthRepository
import com.helpquest.core.domain.service.participant.ParticipantRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    includes(variantCoreDataModule)
    single<HelpQuestLogger> { KermitLogger }
    singleOf(::ConnectionRetryHandler)
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
    singleOf(::OfflineFirstParticipantRepository) bind ParticipantRepository::class
    singleOf(::OfflineFirstAuthRepository) bind AuthRepository::class
}