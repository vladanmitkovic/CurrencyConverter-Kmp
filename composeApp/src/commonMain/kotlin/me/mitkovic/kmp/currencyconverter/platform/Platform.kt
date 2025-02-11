package me.mitkovic.kmp.currencyconverter.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
