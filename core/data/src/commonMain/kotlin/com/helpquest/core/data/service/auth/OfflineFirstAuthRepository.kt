package com.helpquest.core.data.service.auth

import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.service.auth.AuthRepository
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.service.notification.DeviceTokenService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.onSuccess

class OfflineFirstAuthRepository(
    private val database: HelpQuestDatabase,
    private val sessionStorage: SessionStorage,
    private val deviceTokenService: DeviceTokenService,
    private val authService: AuthService
) : AuthRepository {
    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return deviceTokenService
            .unregisterToken(refreshToken)
            .onSuccess {
                authService
                    .logout(refreshToken)
                    .onSuccess {
                        sessionStorage.setAuthInfo(null)
                        database.chatDao.deleteAllChats()
                        database.questLogDao.deleteAllQuests()
                        //TODO: check if are needed additional actions
                    }
                //TODO: test failure of authService.logout
            }
        //TODO: test failure of deviceTokenService.unregisterToken
    }
}