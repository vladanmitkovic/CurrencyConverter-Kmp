package me.mitkovic.kmp.currencyconverter.common

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_status_unsatisfied

class ConnectivityObserverImpl : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.Status> =
        callbackFlow {
            val monitor = nw_path_monitor_create()

            nw_path_monitor_set_update_handler(monitor) { path ->
                val status =
                    when {
                        nw_path_get_status(path) == nw_path_status_satisfied ->
                            ConnectivityObserver.Status.Available
                        nw_path_get_status(path) == nw_path_status_unsatisfied ->
                            ConnectivityObserver.Status.Unavailable
                        else -> ConnectivityObserver.Status.Lost
                    }
                trySend(status)
            }

            nw_path_monitor_start(monitor)

            awaitClose {
                nw_path_monitor_cancel(monitor)
            }
        }.distinctUntilChanged()
}
