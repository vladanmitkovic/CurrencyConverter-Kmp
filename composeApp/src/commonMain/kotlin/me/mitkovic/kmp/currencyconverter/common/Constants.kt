package me.mitkovic.kmp.currencyconverter.common

object Constants {

    const val BASE_URL = "https://android.mitkovic.me"
    const val BASE_CURRENCY = "EUR"
    const val SOMETHING_WENT_WRONG = "Something went wrong!"

    // Define the list of preferred favorites
    val PREFERRED_FAVORITES =
        listOf(
            "EUR",
            "USD",
            "NOK",
            "SEK",
            "DKK",
            "CHF",
            "GBP",
        )

    val PREFERRED_CURRENCY_ORDER =
        listOf(
            "EUR",
            "USD",
            "NOK",
            "SEK",
            "DKK",
            "CHF",
            "JPY",
            "GBP",
            "NZD",
            "CAD",
            "RUB",
            "SGD",
            "TRY",
            "ZAR",
            "AUD",
            "BRL",
            "CNY",
            "HKD",
            "INR",
            "KRW",
            "MXN",
            "PLN",
            "RSD",
        )
}
