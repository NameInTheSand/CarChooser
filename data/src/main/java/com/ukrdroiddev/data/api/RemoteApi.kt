package com.ukrdroiddev.data.api

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val WA_PARAMETER_NAME = "wa_key"

class RemoteApi(private val waToken: String, private val baseUrl: String) {

    fun getAutoApi(): AutoApi {
        val ktorfit = Ktorfit.Builder().httpClient(getHttpClient()).build()
        return ktorfit.createAutoApi()
    }


    private fun getHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 2000
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseUrl
                    parameters.append(WA_PARAMETER_NAME, waToken)
                }
            }
        }
    }
}