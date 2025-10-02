package com.helpquest

import android.app.Application
import com.helpquest.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class HelpQuestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@HelpQuestApplication)
            androidLogger()
        }
    }
}