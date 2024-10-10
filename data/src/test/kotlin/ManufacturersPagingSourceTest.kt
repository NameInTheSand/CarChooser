package com.ukrdroiddev.carchooser

import androidx.paging.PagingSource
import com.ukrdroiddev.data.repositories.pagingSources.ManufacturersPagingSource
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ManufacturersPagingSourceTest {

    private lateinit var repository: MockedManufacturersRepository
    private lateinit var pagingSource: ManufacturersPagingSource

    @Before
    fun setup() {
        repository = MockedManufacturersRepository()
        pagingSource = ManufacturersPagingSource(repository)
    }

    @Test
    fun `load returns success result when page 1 is requested`() = runBlocking {
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 2, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(2, pageResult.data.size)
        assertEquals("BMW", pageResult.data[0].name)
        assertEquals("Audi", pageResult.data[1].name)
        assertNull(pageResult.prevKey)
        assertEquals(2, pageResult.nextKey)
    }

    @Test
    fun `load returns success result when page 2 is requested`() = runBlocking {
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 2, loadSize = 2, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(2, pageResult.data.size)
        assertEquals("Porshe", pageResult.data[0].name)
        assertEquals("Ford", pageResult.data[1].name)
        assertEquals(1, pageResult.prevKey)
        assertEquals(3, pageResult.nextKey)
    }

    @Test
    fun `load returns empty result when page requested is beyond available pages`() = runBlocking {
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 3, loadSize = 2, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertTrue(pageResult.data.isEmpty())
        assertEquals(2, pageResult.prevKey)
        assertNull(pageResult.nextKey)
    }


    @Test
    fun `load returns error result when page 4 is requested`() = runBlocking {
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 4, loadSize = 2, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertTrue(errorResult.throwable is Exception)
        assertEquals("NO_INTERNET", errorResult.throwable.message)
    }

    private class MockedManufacturersRepository : ManufacturersRepository {

        override suspend fun getManufacturersByPage(
            page: Int,
            pageSize: Int?
        ): Result<List<ManufacturerUiEntity>, NetworkError> {
            return when (page) {
                1 -> Result.Success(
                    listOf(
                        ManufacturerUiEntity("1", "BMW"),
                        ManufacturerUiEntity("2", "Audi")
                    )
                )

                2 -> {
                    Result.Success(
                        listOf(
                            ManufacturerUiEntity("3", "Porshe"),
                            ManufacturerUiEntity("4", "Ford")
                        )
                    )
                }

                4 -> {
                    Result.Error(NetworkError.NO_INTERNET)
                }

                else -> {
                    Result.Success(emptyList())
                }
            }
        }

    }

}