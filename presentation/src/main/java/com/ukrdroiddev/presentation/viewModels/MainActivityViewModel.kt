package com.ukrdroiddev.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.domain.entities.YearUiEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val EMPTY_STRING = ""

class MainActivityViewModel : ViewModel() {

    var selectedManufacturer: ManufacturerUiEntity? = null
        private set
        get() = field ?: ManufacturerUiEntity(EMPTY_STRING, EMPTY_STRING)

    var selectedModel: ModelUiEntity? = null
        private set
        get() = field ?: ModelUiEntity(EMPTY_STRING)

    var selectedYear: YearUiEntity? = null
        private set
        get() = field ?: YearUiEntity(EMPTY_STRING)

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

    fun onYearSelected(selectedYear: YearUiEntity?) {
        this.selectedYear = selectedYear
    }

    fun resetSelections() {
        selectedManufacturer = null
        selectedModel = null
        selectedYear = null
    }

}