package me.mitkovic.kmp.currencyconverter.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class IConnectivityObserverImpl(
    context: Context,
) : IConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<IConnectivityObserver.Status> =
        callbackFlow {
            val callback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        launch { send(IConnectivityObserver.Status.Available) }
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        launch { send(IConnectivityObserver.Status.Unavailable) }
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        launch { send(IConnectivityObserver.Status.Lost) }
                    }
                }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
}
