package me.mitkovic.kmp.currencyconverter.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.net.InetSocketAddress
import java.net.Socket

class IConnectivityObserverImpl : IConnectivityObserver {

    override fun observe(): Flow<IConnectivityObserver.Status> =
        flow {
            while (true) {
                val status =
                    if (isNetworkAvailable()) {
                        IConnectivityObserver.Status.Available
                    } else {
                        IConnectivityObserver.Status.Unavailable
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
