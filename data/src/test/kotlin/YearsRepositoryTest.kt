package com.ukrdroiddev.carchooser

import com.ukrdroiddev.data.entities.YearRemoteEntity
import com.ukrdroiddev.data.remoteData.YearsRemoteDataSource
import com.ukrdroiddev.data.repositories.YearsRepositoryImpl
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result
import com.ukrdroiddev.utils.result.Result.Success
import io.ktor.utils.io.errors.IOException
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class YearsRepositoryImplTest {

    private lateinit var repository: YearsRepositoryImpl

    @Before
    fun setup() {
        val mockRemoteDataSource = MockYearsRemoteDataSource()
        repository = YearsRepositoryImpl(mockRemoteDataSource)
    }

    @Test
    fun `test getBuiltDates returns correct years`(): Unit = runBlocking {
        val result = repository.getBuiltDates("BMW", "X5")

        assertTrue(result is Success)
        assertEquals(3, (result as Success).data.size)
        assertEquals("2020", result.data[0].year)
        assertEquals("2021", result.data[1].year)
    }

    @Test
    fun `test getBuiltDates handles network error`() = runBlocking {
        val errorRemoteDataSource = ErrorMockYearsRemoteDataSource()
        val errorRepository = YearsRepositoryImpl(errorRemoteDataSource)
        val networkError = NetworkError.NO_INTERNET
        val result = errorRepository.getBuiltDates("BMW", "X5")

        assertTrue(result is Result.Error)
        assertEquals(networkError, (result as Result.Error).error)
    }

    private class MockYearsRemoteDataSource : YearsRemoteDataSource {

        override suspend fun getBuiltDates(
            manufacturer: String,
            mainType: String
        ): List<YearRemoteEntity> {
            return listOf(
                YearRemoteEntity("2020"),
                YearRemoteEntity("2021"),
                YearRemoteEntity("2022")
            )
        }

    }

    private class ErrorMockYearsRemoteDataSource : YearsRemoteDataSource {

        override suspend fun getBuiltDates(
            manufacturer: String,
            mainType: String
        ): List<YearRemoteEntity> {
            throw IOException("Network error")
        }

    }

}