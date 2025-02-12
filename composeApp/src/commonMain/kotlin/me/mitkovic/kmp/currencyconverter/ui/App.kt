package me.mitkovic.kmp.currencyconverter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.compose_multiplatform
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.navigation.AppNavHost
import me.mitkovic.kmp.currencyconverter.navigation.NavigationViewModel
import me.mitkovic.kmp.currencyconverter.platform.Platform
import me.mitkovic.kmp.currencyconverter.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Inject shared dependencies
    val logger: AppLogger = koinInject()
    val platform: Platform = koinInject()

    val navViewModel: NavigationViewModel = koinInject<NavigationViewModel>()
    val appViewModel: AppViewModel = koinInject<AppViewModel>()

    val themeValue by appViewModel.theme.collectAsStateWithLifecycle(initialValue = null)

    logger.logError("themeValue: $themeValue", null)

    themeValue?.let { loadedTheme ->
        AppTheme(isDarkTheme = loadedTheme) {
            logger.logError("platform.name: ${platform.name}", null)

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Currency Converter")
                        },
                        actions = {
                            IconButton(onClick = {
                                val newTheme =
                                    if (themeValue == true) {
                                        false
                                    } else {
                                        true
                                    }
                                appViewModel.updateTheme(newTheme)
                            }) {
                                Image(painterResource(Res.drawable.compose_multiplatform), null)
                            }
                        },
                    )
                },
                // Optionally, you can add bottomBar, floatingActionButton, etc.
            ) { innerPadding ->
                // Use innerPadding to account for the scaffold’s system bars
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Platform: ${platform.name}")
                    AppNavHost(
                        currentScreen = navViewModel.currentScreen,
                        navigate = { screen -> navViewModel.navigateTo(screen) },
                    )
                }
            }
        }
    }
}
