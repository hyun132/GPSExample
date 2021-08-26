package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.di.models
import com.example.myapplication.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {

    init {
        instance = this
    }

    companion object{
        lateinit var instance:MyApplication
        fun getApplicationContext():Context = instance
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            // declare used Android context
            androidContext(this@MyApplication)
            // declare modules
            modules(viewModelModules,models)
        }
    }
}