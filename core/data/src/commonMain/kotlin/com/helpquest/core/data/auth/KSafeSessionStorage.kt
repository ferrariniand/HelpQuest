package com.helpquest.core.data.auth

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.SessionStorage
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.Flow

class KSafeSessionStorage(
    private val ksafe: KSafe
) : SessionStorage {

    private val authInfoKey = "authInfo"

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return ksafe.getFlow(
            key = authInfoKey,
            defaultValue = null
        )
    }

    override suspend fun setAuthInfo(info: AuthInfo?) {
        if (info == null) {
            ksafe.delete(key = authInfoKey)
            return
        }

        ksafe.put(
            key = authInfoKey,
            value = info
        )
    }

}