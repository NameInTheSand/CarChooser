package com.ukrdroiddev.data.api

import com.ukrdroiddev.data.responses.ManufacturerResponse
import com.ukrdroiddev.data.responses.ModelsResponse
import com.ukrdroiddev.data.responses.YearResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface AutoApi {

    @GET("manufacturer")
    suspend fun getManufacturers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 15
    ): ManufacturerResponse

    @GET("main-types")
    suspend fun getModels(@Query("manufacturer") manufacturer: String):ModelsResponse

    @GET("built-dates")
    suspend fun getBuiltDates(
        @Query("manufacturer") manufacturer: String,
        @Query("main-type") mainType: String
    ): YearResponse

}