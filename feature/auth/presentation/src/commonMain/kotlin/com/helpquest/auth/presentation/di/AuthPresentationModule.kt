package com.helpquest.auth.presentation.di

import com.helpquest.auth.presentation.email_verification.EmailVerificationViewModel
import com.helpquest.auth.presentation.register.RegisterState
import com.helpquest.auth.presentation.register.RegisterViewModel
import com.helpquest.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    single<RegisterState> { RegisterState() }

    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
}