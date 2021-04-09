package com.cookandroid.gachon_study_room.data.di

import com.cookandroid.gachon_study_room.data.repository.LoginRepository
import com.cookandroid.gachon_study_room.data.repository.MainRepository
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModel
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LoginRepository() }
    single {MainRepository()}
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
}
