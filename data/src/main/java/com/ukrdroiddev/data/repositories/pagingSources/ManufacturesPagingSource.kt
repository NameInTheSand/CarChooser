package com.ukrdroiddev.data.repositories.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import com.ukrdroiddev.utils.result.Result

class ManufacturersPagingSource(
    private val repository: ManufacturersRepository
) : PagingSource<Int, ManufacturerUiEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ManufacturerUiEntity> {
        return try {
            val page = params.key ?: 1
            return when (val manufacturers = repository.getManufacturersByPage(page = page)) {
                is Result.Success -> {
                    LoadResult.Page(
                        data = manufacturers.data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (manufacturers.data.isEmpty()) null else page + 1
                    )
                }

                is Result.Error -> {
                    LoadResult.Error(Exception(manufacturers.error.name))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ManufacturerUiEntity>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}