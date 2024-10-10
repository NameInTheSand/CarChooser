package com.ukrdroiddev.data.entities

import com.ukrdroiddev.domain.entities.ModelUiEntity

data class ModelRemoteEntity(val name: String) {

    companion object {
        fun ModelRemoteEntity.toUiEntity(): ModelUiEntity {
            return ModelUiEntity(name)
        }
    }

}
