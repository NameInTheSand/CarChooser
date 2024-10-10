package com.ukrdroiddev.presentation

import kotlinx.serialization.Serializable

@Serializable
object Manufacturers

@Serializable
data class Models(
    val chosenManufacturerId: String,
    val chosenManufacturerName: String,
)

@Serializable
data class Years(
    val chosenManufacturerId: String,
    val chosenManufacturerName: String,
    val chosenModelName: String,
)