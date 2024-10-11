package com.ukrdroiddev.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ukrdroiddev.domain.entities.YearUiEntity
import com.ukrdroiddev.domain.repositories.YearsRepository
import com.ukrdroiddev.presentation.Years
import com.ukrdroiddev.utils.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class YearsViewModel(
    savedStateHandle: SavedStateHandle,
    private val yearsRepository: YearsRepository
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Years>()

    val selectedManufacturerName = args.chosenManufacturerName
    val selectedModelName = args.chosenModelName

    private val _screenState = MutableStateFlow<YearsScreenState>(YearsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun onRefresh() {
        loadData()
    }

    private fun loadData() {
        _screenState.value = YearsScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val remoteYearsResult = yearsRepository.getBuiltDates(
                manufacturer = args.chosenManufacturerId,
                mainType = selectedModelName
            )
            when (remoteYearsResult) {
                is Result.Error -> _screenState.emit(YearsScreenState.Error)
                is Result.Success -> _screenState.emit(YearsScreenState.Content(remoteYearsResult.data))
            }
        }
    }
}

sealed class YearsScreenState {
    data object Loading : YearsScreenState()
    data object Error : YearsScreenState()
    data class Content(val years: List<YearUiEntity>) : YearsScreenState()
}