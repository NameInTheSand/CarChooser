package com.ukrdroiddev.data.repositories

import com.ukrdroiddev.data.api.DEFAULT_PAGE_SIZE
import com.ukrdroiddev.data.entities.ManufacturerRemoteEntity.Companion.toUiEntity
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSource
import com.ukrdroiddev.data.utils.wrapNetworkExceptions
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result

class ManufacturersRepositoryImpl(
    private val remoteDataSource: ManufacturesRemoteDataSource
) : ManufacturersRepository {

    override suspend fun getManufacturersByPage(
        page: Int,
        pageSize: Int?
    ): Result<List<ManufacturerUiEntity>, NetworkError> {
        val remoteManufactures = wrapNetworkExceptions {
            remoteDataSource.getManufacturersByPage(page, pageSize ?: DEFAULT_PAGE_SIZE)
        }

        return when (remoteManufactures) {
            is Result.Success -> {
                Result.Success(
                    remoteManufactures.data.map { it.toUiEntity() }
                )
            }

            is Result.Error -> {
                Result.Error(remoteManufactures.error)
            }
        }
    }
}
