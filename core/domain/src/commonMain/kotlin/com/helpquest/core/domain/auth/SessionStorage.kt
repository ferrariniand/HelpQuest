package com.helpquest.core.domain.auth

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun setAuthInfo(info: AuthInfo?)
}