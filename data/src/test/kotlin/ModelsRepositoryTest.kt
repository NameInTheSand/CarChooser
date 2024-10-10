package com.ukrdroiddev.carchooser

import com.ukrdroiddev.data.entities.ModelRemoteEntity
import com.ukrdroiddev.data.remoteData.ModelsRemoteDataSource
import com.ukrdroiddev.data.repositories.ModelsRepositoryImpl
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result
import com.ukrdroiddev.utils.result.Result.Success
import io.ktor.utils.io.errors.IOException
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ModelsRepositoryImplTest {

    private lateinit var repository: ModelsRepositoryImpl

    @Before
    fun setup() {
        val mockRemoteDataSource = MockModelsRemoteDataSource()
        repository = ModelsRepositoryImpl(mockRemoteDataSource)
    }

    @Test
    fun `test getModelsByManufacturer returns correct models`(): Unit = runBlocking {
        val result = repository.getModelsByManufacturer("BMW")

        assertTrue(result is Success)
        assertEquals(2, (result as Success).data.size)
        assertEquals("X5", result.data[0].name)
        assertEquals("X3", result.data[1].name)
    }

    @Test
    fun `test searchModelByName returns correct model`() = runBlocking {
        repository.getModelsByManufacturer("Audi")
        val searchResults = repository.searchModelByName("ser")

        assertEquals(1, searchResults.size)
        assertEquals("8 Series", searchResults[0].name)
    }

    @Test
    fun `test getModelsByManufacturer handles network error`() = runBlocking {
        val errorRemoteDataSource = ErrorMockModelsRemoteDataSource()
        val errorRepository = ModelsRepositoryImpl(errorRemoteDataSource)
        val networkError = NetworkError.NO_INTERNET
        val result = errorRepository.getModelsByManufacturer("BMW")

        assertTrue(result is Result.Error)
        assertEquals(networkError, (result as Result.Error).error)
    }

    private class MockModelsRemoteDataSource : ModelsRemoteDataSource {
        override suspend fun getModelsByManufacturer(manufacturer: String): List<ModelRemoteEntity> {
            return when (manufacturer) {
                "BMW" -> listOf(ModelRemoteEntity("X5"), ModelRemoteEntity("X3"))
                "Audi" -> listOf(
                    ModelRemoteEntity("Q5"),
                    ModelRemoteEntity("A4"),
                    ModelRemoteEntity("8 Series")
                )

                else -> emptyList()
            }
        }
    }

    private class ErrorMockModelsRemoteDataSource : ModelsRemoteDataSource {

        override suspend fun getModelsByManufacturer(manufacturer: String): List<ModelRemoteEntity> {
            throw IOException("Network error")
        }

    }

}