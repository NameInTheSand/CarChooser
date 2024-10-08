package com.ukrdroiddev.domain.repositories

import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result

interface ModelsRepository {

    suspend fun getModelsByManufacturer(manufacturer: String): Result<List<ModelUiEntity>, NetworkError>

    suspend fun searchModelByName(query: String): List<ModelUiEntity>

}