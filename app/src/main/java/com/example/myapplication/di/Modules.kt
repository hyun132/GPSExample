package com.example.myapplication.di

import com.example.myapplication.TrackingService
import com.example.myapplication.database.TrackingDatabase
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.login.LoginViewModel
import com.example.myapplication.ui.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

val models = module {
    single { TrackingDatabase.getInstance() }
    single { get<TrackingDatabase>().trackingDao() }
    single { LogInRepository(get()) }
//    single { TrackingRepository(get()) }
//    factory { TrackingService(get()) }
}