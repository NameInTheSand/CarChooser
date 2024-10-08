package com.ukrdroiddev.data.entities

import com.ukrdroiddev.domain.entities.YearUiEntity

data class YearRemoteEntity(val year: String) {

    companion object {
        fun YearRemoteEntity.toUiEntity(): YearUiEntity {
            return YearUiEntity(year)
        }
    }

}
