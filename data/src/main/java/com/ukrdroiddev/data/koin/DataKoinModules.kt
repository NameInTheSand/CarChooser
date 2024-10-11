package com.ukrdroiddev.data.koin

import androidx.paging.PagingSource
import com.ukrdroiddev.data.api.RemoteApi
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSource
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSourceImpl
import com.ukrdroiddev.data.remoteData.ModelsRemoteDataSource
import com.ukrdroiddev.data.remoteData.ModelsRemoteDataSourceImpl
import com.ukrdroiddev.data.remoteData.YearsRemoteDataSource
import com.ukrdroiddev.data.remoteData.YearsRemoteDataSourceImpl
import com.ukrdroiddev.data.repositories.ManufacturersRepositoryImpl
import com.ukrdroiddev.data.repositories.ModelsRepositoryImpl
import com.ukrdroiddev.data.repositories.YearsRepositoryImpl
import com.ukrdroiddev.data.repositories.pagingSources.ManufacturersPagingSource
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import com.ukrdroiddev.domain.repositories.ModelsRepository
import com.ukrdroiddev.domain.repositories.YearsRepository
import org.koin.dsl.module


fun getRepositoriesModule(accessToken: String, baseUrl: String) = module {
    single<ManufacturersRepository> { ManufacturersRepositoryImpl(get()) }
    single<ManufacturesRemoteDataSource> { ManufacturesRemoteDataSourceImpl(get()) }
    single<PagingSource<Int, ManufacturerUiEntity>> { ManufacturersPagingSource(get()) }
    single<RemoteApi> { RemoteApi(accessToken, baseUrl) }
    single { get<RemoteApi>().getAutoApi() }

    single<ModelsRepository> { ModelsRepositoryImpl(get()) }
    single<ModelsRemoteDataSource> { ModelsRemoteDataSourceImpl(get()) }

    single<YearsRepository> { YearsRepositoryImpl(get()) }
    single<YearsRemoteDataSource> { YearsRemoteDataSourceImpl(get()) }
}