package com.ukrdroiddev.data.repositories

import com.ukrdroiddev.data.entities.ModelRemoteEntity.Companion.toUiEntity
import com.ukrdroiddev.data.remoteData.ModelsRemoteDataSource
import com.ukrdroiddev.data.utils.wrapNetworkExceptions
import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.domain.repositories.ModelsRepository
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result

class ModelsRepositoryImpl(
    private val remoteDataSource: ModelsRemoteDataSource
) : ModelsRepository {

    //SN: Maybe, it's better to use Trie or other algo, to count user mistakes, simplified to save time
    private var models = mapOf<String, ModelUiEntity>()

    override suspend fun getModelsByManufacturer(
        manufacturer: String
    ): Result<List<ModelUiEntity>, NetworkError> {
        val remoteModels = wrapNetworkExceptions {
            remoteDataSource.getModelsByManufacturer(manufacturer)
        }

        return when (remoteModels) {
            is Result.Success -> {
                val response = remoteModels.data.map { it.toUiEntity() }
                models = response.associateBy { it.name.lowercase() }
                Result.Success(response)
            }

            is Result.Error -> {
                Result.Error(remoteModels.error)
            }
        }
    }

    override suspend fun searchModelByName(query: String): List<ModelUiEntity> {
        return models.filterKeys { it.contains(query.lowercase()) }
            .values
            .toList()
    }

}