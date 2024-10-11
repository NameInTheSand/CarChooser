package com.ukrdroiddev.presentation

import kotlinx.serialization.Serializable

sealed class Destinations {

    @Serializable
    data object Manufacturers : Destinations()

    @Serializable
    data class Models(
        val chosenManufacturerId: String,
        val chosenManufacturerName: String,
    ) : Destinations()

    @Serializable
    data class Years(
        val chosenManufacturerId: String,
        val chosenManufacturerName: String,
        val chosenModelName: String,
    ) : Destinations()

    @Serializable
    data object Summary : Destinations()
}