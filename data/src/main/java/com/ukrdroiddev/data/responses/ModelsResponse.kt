package com.ukrdroiddev.data.responses

import com.ukrdroiddev.data.entities.ModelRemoteEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelsResponse(
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    @SerialName("wkda")
    val models: Map<String, String>
) {

    companion object {
        fun ModelsResponse.mapToRemoteEntities(): List<ModelRemoteEntity> {
            return models.map { (_, value) ->
                ModelRemoteEntity(name = value)
            }
        }
    }

}