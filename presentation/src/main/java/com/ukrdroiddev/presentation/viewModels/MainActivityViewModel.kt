package com.ukrdroiddev.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.entities.ModelUiEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    var selectedManufacturer: ManufacturerUiEntity? = null
        private set

    var selectedModel: ModelUiEntity? = null
        private set

    private val splashShowFlow = MutableStateFlow(true)
    val isSplashShow = splashShowFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            splashShowFlow.value = false
        }
    }

    fun onManufacturerSelected(selectedManufacturer: ManufacturerUiEntity) {
        this.selectedManufacturer = selectedManufacturer
    }

    fun onModelSelected(selectedModel: ModelUiEntity?) {
        this.selectedModel = selectedModel
    }

}