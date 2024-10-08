package com.ukrdroiddev.data.remoteData

import com.ukrdroiddev.data.api.AutoApi
import com.ukrdroiddev.data.api.DEFAULT_PAGE_SIZE
import com.ukrdroiddev.data.entities.ManufacturerRemoteEntity
import com.ukrdroiddev.data.responses.ManufacturerResponse.Companion.mapToRemoteEntities

class ManufacturesRemoteDataSourceImpl(
    private val autoApi: AutoApi
) : ManufacturesRemoteDataSource {

    override suspend fun getManufacturersByPage(
        page: Int,
        pageSize: Int
    ): List<ManufacturerRemoteEntity> {
        return autoApi.getManufacturers(page, pageSize).mapToRemoteEntities()
    }

}


interface ManufacturesRemoteDataSource {

    suspend fun getManufacturersByPage(
        page: Int,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<ManufacturerRemoteEntity>

}