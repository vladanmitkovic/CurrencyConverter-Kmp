package me.mitkovic.kmp.currencyconverter.data.model

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val exception: Throwable? = null,
) {
    class Success<T>(
        data: T?,
    ) : Resource<T>(data)

    class Loading<T>(
        data: T? = null,
    ) : Resource<T>(data)

    class Error<T>(
        message: String,
        data: T? = null,
        exception: Throwable? = null,
    ) : Resource<T>(data, message, exception)
}
