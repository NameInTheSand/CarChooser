package com.ukrdroiddev

import android.app.Application
import com.ukrdroiddev.carchooser.BuildConfig
import com.ukrdroiddev.data.koin.getRepositoriesModule
import com.ukrdroiddev.presentation.koin.mainModule
import com.ukrdroiddev.presentation.koin.manufacturersModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class CarChooserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CarChooserApp)
            modules(
                mainModule,
                manufacturersModule,
                getRepositoriesModule(
                    accessToken = BuildConfig.ACCESS_TOKEN,
                    baseUrl = BuildConfig.BASE_URL
                )
            )

        }
    }

}