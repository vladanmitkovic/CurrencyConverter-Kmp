package me.mitkovic.kmp.currencyconverter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.app_icon
import currencyconverter_kmp.composeapp.generated.resources.app_name
import currencyconverter_kmp.composeapp.generated.resources.content_description_application_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_back_arrow
import currencyconverter_kmp.composeapp.generated.resources.content_description_change_theme_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_favorites_icon
import currencyconverter_kmp.composeapp.generated.resources.light_off
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val logger: AppLogger = koinInject()
    val platform: Platform = koinInject()

    val navViewModel: NavigationViewModel = koinInject<NavigationViewModel>()
    val appViewModel: AppViewModel = koinInject<AppViewModel>()

    val themeValue by appViewModel.theme.collectAsStateWithLifecycle(initialValue = null)

    val topBarTitle = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        topBarTitle.value = getString(Res.string.app_name)
    }

    val showActions = remember { mutableStateOf(false) }
    val showBackIcon = remember { mutableStateOf(false) }

    logger.logError("themeValue: $themeValue", null)

    themeValue?.let { loadedTheme ->
        AppTheme(isDarkTheme = loadedTheme) {
            logger.logError("platform.name: ${platform.name}", null)

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
                        onAction = { action ->
                            when (action) { // Change app title, TopAppBar actions, navi icon - depending on Converter and Favorite screen
                                is MainAction.TitleTextChanged -> topBarTitle.value = action.title
                                is MainAction.ShowActionsChanged -> showActions.value = action.showActions
                                is MainAction.ShowBackIconChanged -> showBackIcon.value = action.showBackButton
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
