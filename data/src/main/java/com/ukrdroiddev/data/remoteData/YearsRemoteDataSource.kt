package com.ukrdroiddev.data.remoteData

import com.ukrdroiddev.data.api.AutoApi
import com.ukrdroiddev.data.entities.YearRemoteEntity
import com.ukrdroiddev.data.responses.YearResponse.Companion.mapToRemoteEntities

class YearsRemoteDataSourceImpl(
    private val autoApi: AutoApi
) : YearsRemoteDataSource {

    override suspend fun getBuiltDates(
        manufacturer: String,
        mainType: String
    ): List<YearRemoteEntity> {
        return autoApi.getBuiltDates(manufacturer, mainType).mapToRemoteEntities()
    }

}

interface YearsRemoteDataSource {

    suspend fun getBuiltDates(manufacturer: String, mainType: String): List<YearRemoteEntity>
}