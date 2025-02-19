package me.mitkovic.kmp.currencyconverter.ui.utils

import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.aud
import currencyconverter_kmp.composeapp.generated.resources.brl
import currencyconverter_kmp.composeapp.generated.resources.cad
import currencyconverter_kmp.composeapp.generated.resources.chf
import currencyconverter_kmp.composeapp.generated.resources.cny
import currencyconverter_kmp.composeapp.generated.resources.dkk
import currencyconverter_kmp.composeapp.generated.resources.eur
import currencyconverter_kmp.composeapp.generated.resources.gbp
import currencyconverter_kmp.composeapp.generated.resources.hkd
import currencyconverter_kmp.composeapp.generated.resources.inr
import currencyconverter_kmp.composeapp.generated.resources.jpy
import currencyconverter_kmp.composeapp.generated.resources.krw
import currencyconverter_kmp.composeapp.generated.resources.mxn
import currencyconverter_kmp.composeapp.generated.resources.nok
import currencyconverter_kmp.composeapp.generated.resources.nzd
import currencyconverter_kmp.composeapp.generated.resources.pln
import currencyconverter_kmp.composeapp.generated.resources.rsd
import currencyconverter_kmp.composeapp.generated.resources.rub
import currencyconverter_kmp.composeapp.generated.resources.sek
import currencyconverter_kmp.composeapp.generated.resources.sgd
import currencyconverter_kmp.composeapp.generated.resources.tru
import currencyconverter_kmp.composeapp.generated.resources.usd
import currencyconverter_kmp.composeapp.generated.resources.zar
import me.mitkovic.kmp.currencyconverter.data.model.CurrencyDetail

object CurrencyInfo {

    private val currencies =
        listOf(
            CurrencyDetail("EUR", "Euro", Res.drawable.eur),
            CurrencyDetail("USD", "US Dollar", Res.drawable.usd),
            CurrencyDetail("NOK", "Norwegian Kroner", Res.drawable.nok),
            CurrencyDetail("SEK", "Swedish Krona", Res.drawable.sek),
            CurrencyDetail("DKK", "Danish Krone", Res.drawable.dkk),
            CurrencyDetail("CHF", "Swiss Franc", Res.drawable.chf),
            CurrencyDetail("GBP", "British Pound", Res.drawable.gbp),
            CurrencyDetail("JPY", "Japanese Yen", Res.drawable.jpy),
            CurrencyDetail("NZD", "New Zealand Dollar", Res.drawable.nzd),
            CurrencyDetail("CAD", "Canadian Dollar", Res.drawable.cad),
            CurrencyDetail("RUB", "Russian Rouble", Res.drawable.rub),
            CurrencyDetail("SGD", "Singapore Dollar", Res.drawable.sgd),
            CurrencyDetail("TRY", "Turkish Lira", Res.drawable.tru),
            CurrencyDetail("ZAR", "South African Rand", Res.drawable.zar),
            CurrencyDetail("AUD", "Australian Dollar", Res.drawable.aud),
            CurrencyDetail("BRL", "Brazilian Real", Res.drawable.brl),
            CurrencyDetail("CNY", "Chinese Yuan", Res.drawable.cny),
            CurrencyDetail("HKD", "Hong Kong Dollar", Res.drawable.hkd),
            CurrencyDetail("INR", "Indian Rupee", Res.drawable.inr),
            CurrencyDetail("KRW", "Korean Won", Res.drawable.krw),
            CurrencyDetail("MXN", "Mexican Peso", Res.drawable.mxn),
            CurrencyDetail("PLN", "Polish Zloty", Res.drawable.pln),
            CurrencyDetail("RSD", "Serbian Dinar", Res.drawable.rsd),
        )

    fun getCurrencyDetail(code: String): CurrencyDetail? = currencies.find { it.code == code }
}
