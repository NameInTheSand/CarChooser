package com.ukrdroiddev.data.responses

import com.ukrdroiddev.data.entities.ManufacturerRemoteEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManufacturerResponse(
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    @SerialName("wkda")
    val manufacturers: Map<String, String>
) {

    companion object {
        fun ManufacturerResponse.mapToRemoteEntities(): List<ManufacturerRemoteEntity> {
            return manufacturers.map { (key, value) ->
                ManufacturerRemoteEntity(id = key, name = value)
            }
        }
    }

}
