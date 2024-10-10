package com.ukrdroiddev.data.responses

import com.ukrdroiddev.data.entities.YearRemoteEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YearResponse(
    @SerialName("wkda")
    val years: Map<String, String>
) {

    companion object {
        fun YearResponse.mapToRemoteEntities(): List<YearRemoteEntity> {
            return years.map { (_, value) ->
                YearRemoteEntity(year = value)
            }
        }
    }

}
