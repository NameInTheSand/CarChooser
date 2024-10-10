package com.ukrdroiddev.utils.result

enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    NO_INTERNET,
    SERVER_ERROR,
    URL_NOT_FOUND,
    VALIDATION_ERROR,
    LIMIT,
    FORBIDDEN,
    UNAUTHORIZED,
    UNKNOWN
}