package com.ukrdroiddev.domain.repositories

import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result

interface ManufacturersRepository {

    suspend fun getManufacturersByPage(
        page: Int,
        pageSize: Int? = null
    ): Result<List<ManufacturerUiEntity>, NetworkError>

}