package com.example.myapplication.di

import com.example.myapplication.repsitory.MyRepository
import com.example.myapplication.ui.login.LoginViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel{ LoginViewModel(get())}
}

val models = module {
    single { MyRepository() }
}