package com.ukrdroiddev.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity

private const val DEFAULT_PAGE_SIZE = 15

class ManufacturersViewModel(
    manufacturersPagingSource: PagingSource<Int, ManufacturerUiEntity>
) : ViewModel() {

    val manufacturersFlow = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            prefetchDistance = 1,
            initialLoadSize = DEFAULT_PAGE_SIZE * 2
        ),
        pagingSourceFactory = { manufacturersPagingSource }
    ).flow

}