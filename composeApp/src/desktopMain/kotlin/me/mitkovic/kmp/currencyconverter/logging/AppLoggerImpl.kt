package me.mitkovic.kmp.currencyconverter.logging

actual class AppLoggerImpl : IAppLogger {

    actual override fun logDebug(
        tag: String?,
        message: String,
    ) {
        println("${tag ?: "Debug"}: $message")
    }

    actual override fun logError(
        tag: String?,
        message: String?,
        throwable: Throwable?,
    ) {
        println("${tag ?: "Error"}: $message")
        throwable?.printStackTrace()
    }
}
