package com.example.myapplication.di

import com.example.myapplication.MyApplication
import com.example.myapplication.repsitory.ServiceStateRepository
import com.example.myapplication.services.TrackingService
import com.example.myapplication.api.LoginService
import com.example.myapplication.database.TrackingDatabase
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.login.LoginViewModel
import com.example.myapplication.ui.main.drivingList.DrivingListFragment
import com.example.myapplication.ui.main.drivingList.DrivingListViewModel
import com.example.myapplication.ui.main.drivingroute.DrivingRouteViewModel
import com.example.myapplication.ui.main.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { DrivingListViewModel(get()) }
    viewModel { DrivingRouteViewModel(get()) }
}

val modules = module {
    single { TrackingDatabase.getInstance(androidContext()).trackingDao() }
    single { LogInRepository(get(), androidContext()) }
    single { TrackingRepository(get()) }
    single { LoginService().loginApi }
    single { ServiceStateRepository() }
    single { TrackingService() }
}