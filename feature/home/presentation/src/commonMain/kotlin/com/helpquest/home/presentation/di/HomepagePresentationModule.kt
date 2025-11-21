package com.helpquest.home.presentation.di


import com.helpquest.home.presentation.HomepageViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homepagePresentationModule = module {

    viewModelOf(::HomepageViewModel)
}