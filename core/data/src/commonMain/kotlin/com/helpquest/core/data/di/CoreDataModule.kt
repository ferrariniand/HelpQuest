package com.helpquest.core.data.di


import com.helpquest.core.data.logging.KermitLogger
import com.helpquest.core.domain.logging.HelpQuestLogger
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    includes(variantCoreDataModule)
    single<HelpQuestLogger> { KermitLogger }
}