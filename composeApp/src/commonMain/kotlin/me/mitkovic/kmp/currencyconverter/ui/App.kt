package me.mitkovic.kmp.currencyconverter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.content_description_back_arrow
import currencyconverter_kmp.composeapp.generated.resources.content_description_change_theme_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_favorites_icon
import currencyconverter_kmp.composeapp.generated.resources.light_off
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.ui.common.ApplicationTitle
import me.mitkovic.kmp.currencyconverter.ui.common.NetworkStatusIndicator
import me.mitkovic.kmp.currencyconverter.ui.navigation.AppNavHost
import me.mitkovic.kmp.currencyconverter.ui.navigation.Screen
import me.mitkovic.kmp.currencyconverter.ui.navigation.currentTopBarState
import me.mitkovic.kmp.currencyconverter.ui.theme.AppTheme
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun App() {
    val appViewModel: AppViewModel = koinInject<AppViewModel>()

    val themeValue by appViewModel.theme.collectAsStateWithLifecycle(initialValue = null)

    val connectivityObserver: ConnectivityObserver = koinInject()
    val networkStatus by connectivityObserver.observe().collectAsStateWithLifecycle(initialValue = null)

    themeValue?.let { loadedTheme ->

        AppTheme(isLightTheme = loadedTheme) {
            MainScreen(
                appViewModel = appViewModel,
                networkStatus = networkStatus,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    networkStatus: ConnectivityObserver.Status?,
) {
    val navController = rememberNavController()
    val topBarState = navController.currentTopBarState()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentOnReload = remember { mutableStateOf<(() -> Unit)?>(null) }
    val setOnReload: ((() -> Unit)?) -> Unit = { handler -> currentOnReload.value = handler }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    ApplicationTitle(topBarState.title, topBarState.showActions)
                },
                actions = {
                    if (topBarState.showActions) {
                        IconButton(onClick = { navController.navigate(Screen.Favorites) }) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                                contentDescription = stringResource(Res.string.content_description_favorites_icon),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        IconButton(onClick = { appViewModel.toggleTheme() }) {
                            Icon(
                                painterResource(Res.drawable.light_off),
                                modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                                contentDescription = stringResource(Res.string.content_description_change_theme_icon),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (topBarState.showBackIcon) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                                contentDescription = stringResource(Res.string.content_description_back_arrow),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
        bottomBar = {
            NetworkStatusIndicator(
                status = networkStatus,
                snackbarHostState = snackbarHostState,
                showReloadButton = topBarState.isHome,
                onReload = { currentOnReload.value?.invoke() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavHost(
                navHostController = navController,
                setOnReload = setOnReload,
                onError = { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                },
            )
        }
    }
}
