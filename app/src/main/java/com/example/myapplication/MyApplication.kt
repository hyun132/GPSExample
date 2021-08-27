package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.di.models
import com.example.myapplication.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication

        /*  context상속받지 않는 객체들 내에서 컨텍스트 사용할 수 있도록
        * 지금 사용하고 있는 부분 수정 후 컨텍스트 주입하는 방법으로 수정 예정.*/
        fun getApplicationContext(): Context = instance
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            // declare used Android context
            androidContext(this@MyApplication)
            // declare modules
            modules(viewModelModules, models)
        }
    }
}