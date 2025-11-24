package me.mitkovic.kmp.currencyconverter.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Common factory function to eliminate duplication across platforms
fun createHttpClient(
    engine: HttpClientEngineFactory<*>,
    json: Json,
): HttpClient =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
        }
    }
