package com.ukrdroiddev.carchooser

import android.app.Application
import com.ukrdroiddev.CarChooserApp
import com.ukrdroiddev.data.koin.getRepositoriesModule
import com.ukrdroiddev.data.remoteData.ManufacturesRemoteDataSource
import com.ukrdroiddev.data.remoteData.ModelsRemoteDataSource
import com.ukrdroiddev.data.remoteData.YearsRemoteDataSource
import com.ukrdroiddev.domain.repositories.ManufacturersRepository
import com.ukrdroiddev.domain.repositories.ModelsRepository
import com.ukrdroiddev.domain.repositories.YearsRepository
import com.ukrdroiddev.presentation.koin.mainModule
import com.ukrdroiddev.presentation.koin.manufacturersModule
import com.ukrdroiddev.presentation.koin.modelsModule
import com.ukrdroiddev.presentation.koin.yearsModule
import com.ukrdroiddev.presentation.viewModels.MainActivityViewModel
import com.ukrdroiddev.presentation.viewModels.ManufacturersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class KoinSetupTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var app: Application

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        app = CarChooserApp()

        startKoin {
            androidContext(app)
            modules(
                mainModule,
                manufacturersModule,
                modelsModule,
                yearsModule,
                getRepositoriesModule(
                    accessToken = BuildConfig.ACCESS_TOKEN,
                    baseUrl = BuildConfig.BASE_URL
                )
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `check MainActivityViewModel is injected correctly`() {
        val viewModel: MainActivityViewModel by inject()
        assertNotNull(viewModel)
    }

    @Test
    fun `check ManufacturersViewModel is injected correctly`() {
        val viewModel: ManufacturersViewModel by inject()
        assertNotNull(viewModel)
    }

    @Test
    fun `check ManufacturersRepository is injected correctly`() {
        val repository: ManufacturersRepository by inject()
        assertNotNull(repository)
    }

    @Test
    fun `check ModelsRepository is injected correctly`() {
        val repository: ModelsRepository by inject()
        assertNotNull(repository)
    }

    @Test
    fun `check YearsRepository is injected correctly`() {
        val repository: YearsRepository by inject()
        assertNotNull(repository)
    }

    @Test
    fun `check ManufacturesRemoteDataSource is injected correctly`() {
        val dataSource: ManufacturesRemoteDataSource by inject()
        assertNotNull(dataSource)
    }

    @Test
    fun `check ModelsRemoteDataSource is injected correctly`() {
        val dataSource: ModelsRemoteDataSource by inject()
        assertNotNull(dataSource)
    }

    @Test
    fun `check YearsRemoteDataSource is injected correctly`() {
        val dataSource: YearsRemoteDataSource by inject()
        assertNotNull(dataSource)
    }

}