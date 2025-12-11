package com.helpquest.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.helpquest.core.data.auth.DATA_STORE_FILE_NAME
import com.helpquest.core.data.auth.createDataStore
import com.helpquest.core.data.lifecycle.AppLifecycleObserver
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> {
        createDataStore(androidContext(), DATA_STORE_FILE_NAME)
    }
    single { ::AppLifecycleObserver }
}