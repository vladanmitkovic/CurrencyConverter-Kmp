package me.mitkovic.kmp.currencyconverter.logging

class JvmLogger : AppLogger {

    override fun logDebug(
        message: String,
        tag: String?,
    ) {
        println("${tag ?: "Debug"}: $message")
    }

    override fun logError(
        message: String?,
        throwable: Throwable?,
        tag: String?,
    ) {
        println("${tag ?: "Error"}: $message")
        throwable?.printStackTrace()
    }
}
