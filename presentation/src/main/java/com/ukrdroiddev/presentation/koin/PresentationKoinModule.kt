package com.ukrdroiddev.presentation.koin

import com.ukrdroiddev.presentation.viewModels.MainActivityViewModel
import com.ukrdroiddev.presentation.viewModels.ManufacturersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MainActivityViewModel)
}

val manufacturersModule = module {
    viewModel { ManufacturersViewModel(get()) }
}