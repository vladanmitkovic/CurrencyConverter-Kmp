package me.mitkovic.kmp.currencyconverter.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ksp.generated.module

expect fun platformModule(): Module

fun initKoin(koinContext: KoinApplication.() -> Unit = {}) {
    try {
        startKoin {
            koinContext()
            modules(
                AppModule().module,
                DatabaseModule().module,
                ViewModelModule().module,
                platformModule(),
            )
        }
    } catch (e: Exception) {
        println("Koin init failed: ${e.message}")
        throw RuntimeException("Koin init failed", e)
    }
}
