package com.ukrdroiddev.data.entities

import com.ukrdroiddev.domain.entities.ManufacturerUiEntity

data class ManufacturerRemoteEntity(val id: String, val name: String) {

    companion object {
        fun ManufacturerRemoteEntity.toUiEntity(): ManufacturerUiEntity {
            return ManufacturerUiEntity(id = id, name = name)
        }
    }
}