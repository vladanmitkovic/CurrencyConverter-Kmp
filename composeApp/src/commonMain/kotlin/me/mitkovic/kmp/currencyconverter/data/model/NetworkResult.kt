package me.mitkovic.kmp.currencyconverter.data.model

sealed class NetworkResult<out T> {

    object Loading : NetworkResult<Nothing>()

    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>()

    data class Error(
        val message: String,
        val throwable: Throwable? = null,
        val code: Int? = null,
    ) : NetworkResult<Nothing>()
}
