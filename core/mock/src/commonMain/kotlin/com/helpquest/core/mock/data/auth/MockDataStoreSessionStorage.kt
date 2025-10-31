package com.helpquest.core.mock.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.mock.data.dto.AuthInfoDto
import com.helpquest.core.mock.data.mappers.toAuthInfo
import com.helpquest.core.mock.data.mappers.toAuthInfoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class MockDataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return observePreference<AuthInfoDto>(authInfoKey).map {
            it?.toAuthInfo()
        }
    }

    override suspend fun setAuthInfo(info: AuthInfo?) {
        if (info == null) {
            removePreference(authInfoKey)
            return
        }
        putPreference(authInfoKey, info.toAuthInfoDto())
    }

    private suspend inline fun <reified T> putPreference(
        key: Preferences.Key<String>,
        value: T
    ) {
        dataStore.edit { prefs ->
            val serializedInputString = json.encodeToString<T>(value)
            //NO ENCRYPTION
            prefs[key] = serializedInputString
        }
    }

    private inline fun <reified T> observePreference(
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
            val serializedOutputString = preferences[key] ?: return@map defaultValue
            //NO DECRYPTION
            json.decodeFromString<T>(serializedOutputString)
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