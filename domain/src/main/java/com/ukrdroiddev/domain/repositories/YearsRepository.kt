package com.ukrdroiddev.domain.repositories

import com.ukrdroiddev.domain.entities.YearUiEntity
import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result

interface YearsRepository {

    suspend fun getBuiltDates(
        manufacturer: String,
        mainType: String
    ): Result<List<YearUiEntity>, NetworkError>

}