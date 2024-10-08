package com.ukrdroiddev.carchooser

import com.ukrdroiddev.data.entities.ManufacturerRemoteEntity
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSource
import com.ukrdroiddev.data.repositories.ManufacturersRepositoryImpl
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result.Error
import com.ukrdroiddev.utils.result.Result.Success
import io.ktor.utils.io.errors.IOException
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ManufacturersRepositoryTest {

    private lateinit var repository: ManufacturersRepository
    private lateinit var errorRepository: ManufacturersRepository
    private val remoteDataSource = MockManufacturersRemoteDataSource()
    private val errorRemoteDataSource = ErrorMockManufacturersRemoteDataSource()

    @Before
    fun setup() {
        repository = ManufacturersRepositoryImpl(remoteDataSource)
        errorRepository = ManufacturersRepositoryImpl(errorRemoteDataSource)
    }

    @Test
    fun `test getManufacturersByPage success`(): Unit = runBlocking {
        val result = repository.getManufacturersByPage(1, 15)
        assertTrue(result is Success)
        assertEquals(2, (result as Success).data.size)
        assertEquals("BMW", result.data.first().name)
    }

    @Test
    fun `test getManufacturersByPage error`() = runBlocking {
        val networkError = NetworkError.NO_INTERNET
        val result = errorRepository.getManufacturersByPage(1, 15)

        assertTrue(result is Error)
        assertEquals(networkError, (result as Error).error)
    }

    private class MockManufacturersRemoteDataSource : ManufacturesRemoteDataSource {

        override suspend fun getManufacturersByPage(
            page: Int,
            pageSize: Int
        ): List<ManufacturerRemoteEntity> {
            val remoteManufactures = listOf(
                ManufacturerRemoteEntity("1", "BMW"),
                ManufacturerRemoteEntity("2", "Audi")
            )

            return remoteManufactures
        }

    }

    private class ErrorMockManufacturersRemoteDataSource : ManufacturesRemoteDataSource {

        override suspend fun getManufacturersByPage(
            page: Int,
            pageSize: Int
        ): List<ManufacturerRemoteEntity> {
            throw IOException("Network error")
        }

    }

}

