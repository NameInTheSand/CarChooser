package com.ukrdroiddev.data.utils

import com.ukrdroiddev.utils.result.NetworkError
import com.ukrdroiddev.utils.result.Result
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException

private const val UNAUTHORIZED = 401
private const val FORBIDDEN = 403
private const val URL_NOT_FOUND = 404
private const val VALIDATION_ERROR = 422
private const val LIMIT = 429
private const val SERVER_ERROR = 500

suspend fun <D> wrapNetworkExceptions(block: suspend () -> D): Result<D, NetworkError> {
    return try {
        Result.Success(block())
    } catch (exception: ClientRequestException) {
        Result.Error(
            parseHttpException(
                code = exception.response.status.value,
                response = exception.response
            )
        )
    } catch (exception: IOException) {
        Result.Error(NetworkError.NO_INTERNET)
    } catch (exception: HttpRequestTimeoutException) {
        Result.Error(NetworkError.REQUEST_TIMEOUT)
    }
}

private fun parseHttpException(
    code: Int,
    response: HttpResponse?
): NetworkError {
    return when {
        response == null -> NetworkError.SERVER_ERROR
        code == FORBIDDEN -> NetworkError.FORBIDDEN
        code == UNAUTHORIZED -> NetworkError.UNAUTHORIZED
        code == SERVER_ERROR -> NetworkError.SERVER_ERROR
        code == URL_NOT_FOUND -> NetworkError.URL_NOT_FOUND
        code == VALIDATION_ERROR -> NetworkError.VALIDATION_ERROR
        code == LIMIT -> NetworkError.LIMIT
        else -> NetworkError.UNKNOWN
    }
}
