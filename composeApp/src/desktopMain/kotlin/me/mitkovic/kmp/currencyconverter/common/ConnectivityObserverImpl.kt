package me.mitkovic.kmp.currencyconverter.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.net.InetSocketAddress
import java.net.Socket

class ConnectivityObserverImpl : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.Status> =
        flow {
            while (true) {
                val status =
                    if (isNetworkAvailable()) {
                        ConnectivityObserver.Status.Available
                    } else {
                        ConnectivityObserver.Status.Unavailable
                    }
                emit(status)
                delay(5000) // Check every 5 seconds
            }
        }.distinctUntilChanged()

    private fun isNetworkAvailable(): Boolean =
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
}
