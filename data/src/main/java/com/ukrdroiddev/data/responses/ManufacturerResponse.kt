package com.ukrdroiddev.data.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManufacturerResponse(
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    @SerialName("wkda")
    val manufacturers: Map<String, String>
)
