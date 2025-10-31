package com.helpquest.core.mock.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.helpquest.core.mock.data.auth.DATA_STORE_FILE_NAME
import com.helpquest.core.mock.data.auth.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformCoreMockModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> {
        createDataStore(androidContext(), DATA_STORE_FILE_NAME)
    }
}