package com.ukrdroiddev.presentation.koin

import androidx.lifecycle.SavedStateHandle
import com.ukrdroiddev.presentation.viewModels.MainActivityViewModel
import com.ukrdroiddev.presentation.viewModels.ManufacturersViewModel
import com.ukrdroiddev.presentation.viewModels.ModelsViewModel
import com.ukrdroiddev.presentation.viewModels.YearsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MainActivityViewModel)
}

val manufacturersModule = module {
    viewModel { ManufacturersViewModel(get()) }
}

val modelsModule = module {
    viewModel { (manufacturer: SavedStateHandle) -> ModelsViewModel(manufacturer, get()) }
}

val yearsModule = module {
    viewModel { (model: SavedStateHandle) -> YearsViewModel(model, get()) }
}