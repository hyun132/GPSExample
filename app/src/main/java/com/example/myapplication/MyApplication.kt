package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.modules
import com.example.myapplication.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            androidContext(this@MyApplication)
            modules(viewModelModules, modules)
        }
    }
}