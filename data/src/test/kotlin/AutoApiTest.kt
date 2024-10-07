package com.ukrdroiddev.carchooser

import com.ukrdroiddev.data.api.AutoApi
import com.ukrdroiddev.data.api.createAutoApi
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AutoApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var autoApi: AutoApi

    private fun getHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }
    }

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        val ktorfit = Ktorfit.Builder()
            .httpClient(getHttpClient())
            .baseUrl(mockWebServer.url("/").toString())
            .build()

        autoApi = ktorfit.createAutoApi()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test getManufacturers success`() = runBlocking {
        val mockResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody("""
       {
        "page": 0,
        "pageSize": 15,
        "totalPageCount": 7,
        "wkda": {
            "020": "Abarth",
            "032": "Aiways",
            "040": "Alfa Romeo",
            "042": "Alpina",
            "043": "Alpine",
            "057": "Aston Martin",
            "060": "Audi",
            "095": "Barkas",
            "107": "Bentley",
            "130": "BMW",
            "125": "Borgward",
            "145": "Brilliance",
            "141": "Buick",
            "147": "BYD",
            "150": "Cadillac"
          }
        }
            """.trimIndent())
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)

        val response = autoApi.getManufacturers(0)

        assertEquals(0, response.page)
        assertEquals(15, response.pageSize)
        assertEquals(7, response.totalPageCount)
        assertEquals("Bentley", response.manufacturers["107"])
    }

    //SN: Error test is missing because error response is missing on the documentation side

    @Test
    fun `test getModels success`() = runBlocking {
        val mockResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody("""
            {
              "page": 0,
              "pageSize": 2147483647,
              "totalPageCount": 1,
              "wkda": {
                "1er": "1er",
                "2er": "2er",
                "3er": "3er",
                "4er": "4er",
                "5er": "5er",
                "6er": "6er",
                "7er": "7er",
                "8er": "8er",
                "i3": "i3",
                "i8": "i8",
                "X1": "X1",
                "X2": "X2",
                "X3": "X3",
                "X4": "X4",
                "X5": "X5",
                "X6": "X6",
                "X7": "X7",
                "Z1": "Z1",
                "Z3": "Z3",
                "Z4": "Z4",
                "Z8": "Z8"
              }
            }
            """.trimIndent())
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)
        val response = autoApi.getModels("BMW")

        assertEquals("1er", response.models["1er"])
        assertEquals("2er", response.models["2er"])
    }

    @Test
    fun `test getModels error`() = runBlocking {
        val mockResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody("""
            {
              "page": 0,
              "pageSize": 2147483647,
              "totalPageCount": 0,
              "wkda": {}
            }
            """.trimIndent())
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)
        val response = autoApi.getModels("BMW")

        assertEquals(0, response.totalPageCount)
        assertEquals(emptyMap<String, String>(), response.models)
    }

    @Test
    fun `test getBuiltDates success`() = runBlocking {
        val mockResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {
                    "wkda": {
                        "2014": "2014",
                        "2015": "2015"
                    }
                }
            """.trimIndent())
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)
        val response = autoApi.getBuiltDates("BMW", "3er")

        assertEquals("2014", response.years["2014"])
        assertEquals("2015", response.years["2015"])
    }

    @Test
    fun `test getBuiltDates error`() = runBlocking {
        val mockResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {
                    "wkda": {}
                }
            """.trimIndent())
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)
        val response = autoApi.getBuiltDates("BMW", "3er")

        assertEquals(emptyMap<String, String>(), response.years)
    }

}