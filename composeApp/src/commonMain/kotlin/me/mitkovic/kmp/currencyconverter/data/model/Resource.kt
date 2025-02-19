package me.mitkovic.kmp.currencyconverter.data.model

sealed class Resource<out T> {

    object Loading : Resource<Nothing>()

    data class Success<T>(
        val data: T,
    ) : Resource<T>()

    data class Error(
        val message: String,
        val throwable: Throwable? = null,
        val code: Int? = null,
    ) : Resource<Nothing>()
}
