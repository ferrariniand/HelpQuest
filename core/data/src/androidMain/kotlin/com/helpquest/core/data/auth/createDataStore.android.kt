package com.helpquest.core.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context, fileName: String): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(fileName).absolutePath
    }
}