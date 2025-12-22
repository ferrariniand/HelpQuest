package com.helpquest.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.helpquest.core.data.auth.DATA_STORE_FILE_NAME
import com.helpquest.core.data.auth.createDataStore
import com.helpquest.core.data.lifecycle.AppLifecycleObserver
import com.helpquest.core.data.networking.ConnectionErrorHandler
import com.helpquest.core.data.networking.ConnectivityObserver
import com.helpquest.core.database.DatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> {
        createDataStore(androidContext(), DATA_STORE_FILE_NAME)
    }
    single { DatabaseFactory(androidContext()) }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
}