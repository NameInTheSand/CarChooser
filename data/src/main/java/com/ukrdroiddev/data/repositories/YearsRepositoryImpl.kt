package com.ukrdroiddev.data.repositories

import com.ukrdroiddev.data.entities.YearRemoteEntity.Companion.toUiEntity
import com.ukrdroiddev.data.remoteData.YearsRemoteDataSource
import com.ukrdroiddev.data.utils.wrapNetworkExceptions
import com.ukrdroiddev.domain.entities.YearUiEntity
import com.ukrdroiddev.domain.repositories.YearsRepository
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result

class YearsRepositoryImpl(
    private val remoteDataSource: YearsRemoteDataSource
) : YearsRepository {

    override suspend fun getBuiltDates(
        manufacturer: String,
        mainType: String
    ): Result<List<YearUiEntity>, NetworkError> {
        val remoteYears = wrapNetworkExceptions {
            remoteDataSource.getBuiltDates(manufacturer, mainType)
        }

        return when (remoteYears) {
            is Result.Success -> {
                Result.Success(remoteYears.data.map { it.toUiEntity() })
            }

            is Result.Error -> {
                Result.Error(remoteYears.error)
            }
        }
    }

}