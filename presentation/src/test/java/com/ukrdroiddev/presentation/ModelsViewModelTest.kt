package com.ukrdroiddev.presentation

import androidx.lifecycle.SavedStateHandle
import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.domain.repositories.ModelsRepository
import com.ukrdroiddev.presentation.viewModels.ModelsScreenState
import com.ukrdroiddev.presentation.viewModels.ModelsViewModel
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
class ModelsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ModelsViewModel
    private lateinit var mockRepository: ModelsRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockRepository = object : ModelsRepository {
            override suspend fun getModelsByManufacturer(manufacturer: String): Result<List<ModelUiEntity>, NetworkError> {
                return Result.Success(emptyList())
            }

            override suspend fun searchModelByName(query: String): List<ModelUiEntity> {
                return if (query.isEmpty()) emptyList() else listOf(ModelUiEntity(query))
            }
        }
        savedStateHandle = SavedStateHandle(
            mapOf(
                "chosenManufacturerId" to "1",
                "chosenManufacturerName" to "Ford"
            )
        )
        viewModel = ModelsViewModel(savedStateHandle, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial screen state should be Loading`() = runTest {
        ModelsViewModel(savedStateHandle, mockRepository).also {
            assertTrue(it.screenState.value is ModelsScreenState.Loading)
        }
    }

    @Test
    fun `loadData should update screen state to Content on success`() = runTest {
        val viewModel = ModelsViewModel(savedStateHandle, mockRepository)

        val job = launch {
            viewModel.screenState.collect {
                println(it)
            }
        }

        viewModel.onRefresh()
        advanceTimeBy(3000L)
        assertEquals(viewModel.screenState.value, ModelsScreenState.Content(emptyList()))
        job.cancel()
    }

    @Test
    fun `loadData should update screen state to Error on failure`() = runTest {
        mockRepository = object : ModelsRepository {
            override suspend fun getModelsByManufacturer(manufacturer: String): Result<List<ModelUiEntity>, NetworkError> {
                return Result.Error(NetworkError.SERVER_ERROR)
            }

            override suspend fun searchModelByName(query: String): List<ModelUiEntity> {
                return emptyList()
            }
        }
        val viewModel = ModelsViewModel(savedStateHandle, mockRepository)

        val job = launch {
            viewModel.screenState.collect {
                println(it)
            }
        }

        viewModel.onRefresh()
        advanceTimeBy(4000L)
        assertTrue(viewModel.screenState.value is ModelsScreenState.Error)
        job.cancel()
    }

    @Test
    fun `onSearchQueryChanged should emit search results`() = runTest {
        viewModel.onSearchQueryChanged("Test Model")
        advanceTimeBy(3000)
        val job = launch {
            viewModel.screenState.collect { state ->
                if (state is ModelsScreenState.Content) {
                    assertEquals(1, state.models.size)
                    assertEquals("Test Model", state.models.first().name)
                }
            }
        }

        job.cancel()
    }
}