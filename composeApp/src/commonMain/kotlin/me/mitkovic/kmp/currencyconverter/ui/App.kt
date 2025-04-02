package me.mitkovic.kmp.currencyconverter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.ui.navigation.AppNavHost
import me.mitkovic.kmp.currencyconverter.ui.theme.AppTheme
import org.koin.compose.koinInject

@Composable
fun App() {

    val appViewModel: AppViewModel = koinInject<AppViewModel>()

    val themeValue by appViewModel.theme.collectAsStateWithLifecycle(initialValue = null)

    val connectivityObserver: ConnectivityObserver = koinInject()
    val networkStatus by connectivityObserver.observe().collectAsStateWithLifecycle(initialValue = null)

    val navController = rememberNavController()

    themeValue?.let { loadedTheme ->
        AppTheme(isLightTheme = loadedTheme) {

            AppNavHost(
                navHostController = navController,
                networkStatus = { networkStatus },
                onThemeClick = {
                    appViewModel.updateTheme(!loadedTheme)
                },
            )
        }
    }
}
