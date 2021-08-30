package com.example.myapplication.di

import com.example.myapplication.TrackingService
import com.example.myapplication.api.LoginService
import com.example.myapplication.database.TrackingDatabase
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.login.LoginViewModel
import com.example.myapplication.ui.main.drivingList.DrivingListViewModel
import com.example.myapplication.ui.main.drivingroute.DrivingRouteViewModel
import com.example.myapplication.ui.main.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DrivingListViewModel(get()) }
    viewModel { DrivingRouteViewModel(get()) }
}

val models = module {
    single { TrackingDatabase.getInstance().trackingDao() }
    single { LoginService().loginApi }
    single { TrackingRepository(get()) }
    single { TrackingService() }
    single { LogInRepository(get(), androidContext()) }
}