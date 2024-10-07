package com.ukrdroiddev.data.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YearResponse(
    @SerialName("wkda")
    val years: Map<String, String>
)
