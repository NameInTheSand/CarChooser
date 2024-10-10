package com.ukrdroiddev.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.domain.repositories.ModelsRepository
import com.ukrdroiddev.presentation.Models
import com.ukrdroiddev.utils.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val EMPTY_STRING = ""

class ModelsViewModel(
    savedStateHandle: SavedStateHandle,
    private val modelsRepository: ModelsRepository
) : ViewModel() {

    private val manufacturer = savedStateHandle.toRoute<Models>()
    val selectedManufacturerName = manufacturer.chosenManufacturerName

    private var searchJob: Job? = null

    var searchQuery: String = EMPTY_STRING
        private set

    private val _screenState = MutableStateFlow<ModelsScreenState>(ModelsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun onRefresh() {
        loadData()
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        searchJob?.cancel()

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            _screenState.emit(
                ModelsScreenState.Content(
                    modelsRepository.searchModelByName(searchQuery)
                )
            )
        }
    }

    private fun loadData() {
        _screenState.value = ModelsScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val remoteModelsResult = modelsRepository.getModelsByManufacturer(
                manufacturer.chosenManufacturerId
            )
            when (remoteModelsResult) {
                is Result.Error -> _screenState.emit(ModelsScreenState.Error)
                is Result.Success -> _screenState.emit(ModelsScreenState.Content(remoteModelsResult.data))
            }
        }
    }
}

sealed class ModelsScreenState {
    data object Loading : ModelsScreenState()
    data object Error : ModelsScreenState()
    data class Content(val models: List<ModelUiEntity>) : ModelsScreenState()
}