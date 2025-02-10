package me.mitkovic.kmp.currencyconverter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform