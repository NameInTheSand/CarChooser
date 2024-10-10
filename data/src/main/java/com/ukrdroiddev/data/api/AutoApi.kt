package com.ukrdroiddev.data.api

import com.ukrdroiddev.data.responses.ManufacturerResponse
import com.ukrdroiddev.data.responses.ModelsResponse
import com.ukrdroiddev.data.responses.YearResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

const val DEFAULT_PAGE_SIZE = 15

interface AutoApi {

    @GET("v1/car-types/manufacturer")
    suspend fun getManufacturers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE
    ): ManufacturerResponse

    @GET("v1/car-types/main-types")
    suspend fun getModels(@Query("manufacturer") manufacturer: String):ModelsResponse

    @GET("v1/car-types/built-dates")
    suspend fun getBuiltDates(
        @Query("manufacturer") manufacturer: String,
        @Query("main-type") mainType: String
    ): YearResponse

}