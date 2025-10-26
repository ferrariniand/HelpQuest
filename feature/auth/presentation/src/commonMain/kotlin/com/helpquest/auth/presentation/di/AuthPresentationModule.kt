package com.helpquest.auth.presentation.di

import com.helpquest.auth.presentation.email_verification.EmailVerificationViewModel
import com.helpquest.auth.presentation.forgot_password.ForgotPasswordViewModel
import com.helpquest.auth.presentation.login.LoginState
import com.helpquest.auth.presentation.login.LoginViewModel
import com.helpquest.auth.presentation.register.RegisterState
import com.helpquest.auth.presentation.register.RegisterViewModel
import com.helpquest.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    single<RegisterState> { RegisterState() }
    single<LoginState> { LoginState() }

    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
}