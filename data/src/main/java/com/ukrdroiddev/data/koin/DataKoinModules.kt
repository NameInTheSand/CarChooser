package com.ukrdroiddev.data.koin

import androidx.paging.PagingSource
import com.ukrdroiddev.data.api.RemoteApi
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSource
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSourceImpl
import com.ukrdroiddev.data.repositories.ManufacturersRepositoryImpl
import com.ukrdroiddev.data.repositories.pagingSources.ManufacturersPagingSource
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import org.koin.dsl.module


fun getRepositoriesModule(accessToken: String, baseUrl: String) = module {
    single<ManufacturersRepository> { ManufacturersRepositoryImpl(get()) }
    single<ManufacturesRemoteDataSource> { ManufacturesRemoteDataSourceImpl(get()) }
    single<PagingSource<Int, ManufacturerUiEntity>> { ManufacturersPagingSource(get()) }
    single<RemoteApi> { RemoteApi(accessToken, baseUrl) }
    single { get<RemoteApi>().getAutoApi() }
}