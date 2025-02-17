package me.mitkovic.kmp.currencyconverter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.app_icon
import currencyconverter_kmp.composeapp.generated.resources.app_name
import currencyconverter_kmp.composeapp.generated.resources.content_description_application_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_back_arrow
import currencyconverter_kmp.composeapp.generated.resources.content_description_change_theme_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_favorites_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_network_status
import currencyconverter_kmp.composeapp.generated.resources.content_description_refresh_icon
import currencyconverter_kmp.composeapp.generated.resources.light_off
import currencyconverter_kmp.composeapp.generated.resources.network_status_connected
import currencyconverter_kmp.composeapp.generated.resources.network_status_no_internet
import currencyconverter_kmp.composeapp.generated.resources.network_status_unknown
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.navigation.AppNavHost
import me.mitkovic.kmp.currencyconverter.navigation.NavigationViewModel
import me.mitkovic.kmp.currencyconverter.navigation.Screen
import me.mitkovic.kmp.currencyconverter.platform.Platform
import me.mitkovic.kmp.currencyconverter.ui.theme.AppTheme
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

sealed class MainAction {

    data class TitleTextChanged(
        val title: String,
    ) : MainAction()

    data class ShowActionsChanged(
        val showActions: Boolean,
    ) : MainAction()

    data class ShowBackIconChanged(
        val showBackButton: Boolean,
    ) : MainAction()

    data class ShowReloadButtonChanged(
        val showReloadButton: Boolean,
    ) : MainAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val logger: AppLogger = koinInject<AppLogger>()
    val platform: Platform = koinInject<Platform>()
    val connectivityObserver: ConnectivityObserver = koinInject()

    val navViewModel: NavigationViewModel = koinInject<NavigationViewModel>()
    val appViewModel: AppViewModel = koinInject<AppViewModel>()

    val themeValue by appViewModel.theme.collectAsStateWithLifecycle(initialValue = null)

    logger.logError("App", "themeValue AAA: $themeValue", null)

    val networkStatus by connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Available)

    logger.logError("App", "networkStatus: $networkStatus", null)

    val topBarTitle = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        topBarTitle.value = getString(Res.string.app_name)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val showActions = remember { mutableStateOf(false) }
    val showBackIcon = remember { mutableStateOf(false) }
    val showReloadButton = remember { mutableStateOf(false) }
    val refreshTrigger = remember { mutableIntStateOf(0) }

    themeValue?.let { loadedTheme ->
        AppTheme(isDarkTheme = loadedTheme) {
            logger.logError("App", "platform.name: ${platform.name}", null)

            Scaffold(
                containerColor = MaterialTheme.colorScheme.surface,
                topBar = {
                    TopAppBar(
                        title = {
                            ApplicationTitle(topBarTitle.value, showActions.value)
                        },
                        actions = {
                            if (showActions.value) {
                                IconButton(
                                    onClick = {
                                        navViewModel.navigateTo(Screen.Favorites)
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                                        contentDescription = stringResource(Res.string.content_description_favorites_icon),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        appViewModel.updateTheme(!loadedTheme)
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.light_off),
                                        modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                                        contentDescription = stringResource(Res.string.content_description_change_theme_icon),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            if (showBackIcon.value) {
                                IconButton(
                                    onClick = {
                                        navViewModel.navigateTo(Screen.Converter)
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                                        contentDescription = stringResource(Res.string.content_description_back_arrow),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        },
                    )
                },
                bottomBar = {
                    NetworkStatusIndicator(
                        status = networkStatus,
                        snackbarHostState = snackbarHostState,
                        showReloadButton = showReloadButton.value,
                    ) {
                        refreshTrigger.intValue++
                    }
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { innerPadding ->
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
                        { refreshTrigger.intValue },
                        onAction = { action ->
                            when (action) { // Change app title, TopAppBar actions, navi icon - depending on Converter and Favorite screen
                                is MainAction.TitleTextChanged -> topBarTitle.value = action.title
                                is MainAction.ShowActionsChanged -> showActions.value = action.showActions
                                is MainAction.ShowBackIconChanged -> showBackIcon.value = action.showBackButton
                                is MainAction.ShowReloadButtonChanged -> showReloadButton.value = action.showReloadButton
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ApplicationTitle(
    title: String,
    showActions: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showActions) {
            Icon(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = stringResource(Res.string.content_description_application_icon),
                modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        }
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onBackground),
        )
    }
}

@Composable
fun NetworkStatusIndicator(
    status: ConnectivityObserver.Status?,
    snackbarHostState: SnackbarHostState,
    showReloadButton: Boolean,
    onReload: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    Column {
        // Horizontal Divider at the top
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
        // Box with network status and reload icon
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.bottomBarHeight)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = MaterialTheme.spacing.medium),
        ) {
            // Row to hold icons
            Row(
                modifier =
                    Modifier
                        .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Reload Icon on the left
                Box(
                    modifier = Modifier.padding(start = MaterialTheme.spacing.medium).size(MaterialTheme.spacing.iconSize),
                ) {
                    if (showReloadButton) {
                        IconButton(onClick = onReload) {
                            Icon(
                                Icons.Default.Refresh,
                                modifier =
                                    Modifier
                                        .size(MaterialTheme.spacing.iconSize),
                                contentDescription = stringResource(Res.string.content_description_refresh_icon),
                                tint = MaterialTheme.colorScheme.background,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Network Status Icon on the right
                val (message, networkIconColor, networkIcon) =
                    when (status?.ordinal) {
                        0 -> Triple(stringResource(Res.string.network_status_connected), Color.Green, Icons.Default.CheckCircle)
                        1 -> Triple(stringResource(Res.string.network_status_no_internet), Color.Red, Icons.Default.Info)
                        else -> Triple(stringResource(Res.string.network_status_unknown), Color.Gray, Icons.Default.Info)
                    }

                IconButton(
                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium).size(MaterialTheme.spacing.medium),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short,
                            )
                        }
                    },
                ) {
                    Icon(
                        networkIcon,
                        contentDescription = stringResource(Res.string.content_description_network_status),
                        tint = networkIconColor,
                    )
                }
            }
        }
    }
}
