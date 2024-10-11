package com.ukrdroiddev.presentation

import androidx.lifecycle.SavedStateHandle
import com.ukrdroiddev.domain.entities.YearUiEntity
import com.ukrdroiddev.domain.repositories.YearsRepository
import com.ukrdroiddev.presentation.viewModels.YearsScreenState
import com.ukrdroiddev.presentation.viewModels.YearsViewModel
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class YearsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: YearsViewModel
    private lateinit var mockRepository: YearsRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = object : YearsRepository {
            override suspend fun getBuiltDates(
                manufacturer: String,
                mainType: String
            ): Result<List<YearUiEntity>, NetworkError> {
                return Result.Success(emptyList())
            }
        }

        savedStateHandle = SavedStateHandle(
            mapOf(
                "chosenManufacturerId" to "1",
                "chosenManufacturerName" to "Ford",
                "chosenModelName" to "Focus"
            )
        )

        viewModel = YearsViewModel(savedStateHandle, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial screen state should be Loading`() = runTest {
        YearsViewModel(savedStateHandle, mockRepository).also {
            assertTrue(it.screenState.value is YearsScreenState.Loading)
        }
    }

    @Test
    fun `loadData should update screen state to Content on success`() = runTest {
        val years = listOf(YearUiEntity("2020"), YearUiEntity("2021"))
        mockRepository = object : YearsRepository {
            override suspend fun getBuiltDates(
                manufacturer: String,
                mainType: String
            ): Result<List<YearUiEntity>, NetworkError> {
                return Result.Success(years)
            }
        }
        viewModel = YearsViewModel(savedStateHandle, mockRepository)

        val job = launch {
            viewModel.screenState.collect { state ->
                if (state is YearsScreenState.Content) {
                    assertEquals(years, state.years)
                }
            }
        }
        viewModel.onRefresh()
        advanceTimeBy(1000)
        job.cancel()
    }

    @Test
    fun `loadData should update screen state to Error on failure`() = runTest {
        mockRepository = object : YearsRepository {
            override suspend fun getBuiltDates(
                manufacturer: String,
                mainType: String
            ): Result<List<YearUiEntity>, NetworkError> {
                return Result.Error(NetworkError.SERVER_ERROR)
            }
        }
        viewModel = YearsViewModel(savedStateHandle, mockRepository)
        viewModel.onRefresh()

        val job = launch {
            viewModel.screenState.collect {
                println(it)
            }
        }

        advanceTimeBy(4000)
        assertTrue(viewModel.screenState.value is YearsScreenState.Error)
        job.cancel()
    }
}