package com.ukrdroiddev.presentation

import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.domain.entities.YearUiEntity
import com.ukrdroiddev.presentation.viewModels.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val EMPTY_STRING = ""

@ExperimentalCoroutinesApi
class MainActivityViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setup() {
        // Set the main coroutine dispatcher to the test dispatcher
        Dispatchers.setMain(testDispatcher)
        viewModel = MainActivityViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial values should be correct`() {
        assertEquals(EMPTY_STRING, viewModel.selectedManufacturer?.name)
        assertEquals(EMPTY_STRING, viewModel.selectedModel?.name)
        assertEquals(EMPTY_STRING, viewModel.selectedYear?.year)
    }

    @Test
    fun `onManufacturerSelected should update selectedManufacturer`() {
        val manufacturer = ManufacturerUiEntity("Ford", "USA")
        viewModel.onManufacturerSelected(manufacturer)
        assertEquals(manufacturer, viewModel.selectedManufacturer)
    }

    @Test
    fun `onModelSelected should update selectedModel`() {
        val model = ModelUiEntity("Mustang")
        viewModel.onModelSelected(model)
        assertEquals(model, viewModel.selectedModel)
    }

    @Test
    fun `onYearSelected should update selectedYear`() {
        val year = YearUiEntity("2021")
        viewModel.onYearSelected(year)
        assertEquals(year, viewModel.selectedYear)
    }

    @Test
    fun `resetSelections should clear all selections`() {
        val manufacturer = ManufacturerUiEntity("Ford", "USA")
        val model = ModelUiEntity("Mustang")
        val year = YearUiEntity("2021")

        viewModel.onManufacturerSelected(manufacturer)
        viewModel.onModelSelected(model)
        viewModel.onYearSelected(year)

        viewModel.resetSelections()

        assertEquals(viewModel.selectedManufacturer, ManufacturerUiEntity("", ""))
        assertEquals(viewModel.selectedModel, ModelUiEntity(""))
        assertEquals(viewModel.selectedYear, YearUiEntity(""))
    }

    @Test
    fun `splashShowFlow should emit false after 1 second`() = runTest {
        val splashShowValues = mutableListOf<Boolean>()
        val job = launch {
            viewModel.isSplashShow.collect { splashShowValues.add(it) }
        }

        advanceTimeBy(500)
        assertEquals(true, splashShowValues.last())
        advanceTimeBy(1000)
        job.cancel()
        assertEquals(false, splashShowValues.last())
    }
}