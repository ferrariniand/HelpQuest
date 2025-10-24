package com.helpquest.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.helpquest.core.data.dto.AuthInfoDto
import com.helpquest.core.data.encryption.Crypto
import com.helpquest.core.data.mappers.toAuthInfo
import com.helpquest.core.data.mappers.toAuthInfoDto
import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return observeSecurePreference<AuthInfoDto>(authInfoKey).map {
            it?.toAuthInfo()
        }
    }

    override suspend fun setAuthInfo(info: AuthInfo?) {
        if (info == null) {
            removePreference(authInfoKey)
            return
        }
        putSecurePreference(authInfoKey, info.toAuthInfoDto())
    }

    private suspend inline fun <reified T> putSecurePreference(
        key: Preferences.Key<String>,
        value: T
    ) {
        dataStore.edit { prefs ->
            val serializedInputString = json.encodeToString<T>(value)
            //encrypt
            val secureString = Crypto.encrypt(serializedInputString)
            prefs[key] = secureString
        }
    }

    private inline fun <reified T> observeSecurePreference(
        key: Preferences.Key<String>,
        defaultValue: T? = null
    ): Flow<T?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val secureString = preferences[key] ?: return@map defaultValue
            //decrypt
            val decryptedString = Crypto.decrypt(secureString)
            json.decodeFromString<T>(decryptedString)
        }
    }

    private suspend fun removePreference(key: Preferences.Key<String>) {
        dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}