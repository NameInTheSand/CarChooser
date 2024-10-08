package com.ukrdroiddev.data.remoteData

import com.ukrdroiddev.data.api.AutoApi
import com.ukrdroiddev.data.entities.ModelRemoteEntity
import com.ukrdroiddev.data.responses.ModelsResponse.Companion.mapToRemoteEntities

class ModelsRemoteDataSourceImpl(
    private val autoApi: AutoApi
) : ModelsRemoteDataSource {

    override suspend fun getModelsByManufacturer(manufacturer: String): List<ModelRemoteEntity> {
        return autoApi.getModels(manufacturer).mapToRemoteEntities()
    }

}

interface ModelsRemoteDataSource {

    suspend fun getModelsByManufacturer(manufacturer: String): List<ModelRemoteEntity>

}