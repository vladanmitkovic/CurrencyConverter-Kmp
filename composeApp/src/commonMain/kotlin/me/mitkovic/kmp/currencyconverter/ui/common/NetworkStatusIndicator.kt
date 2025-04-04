package me.mitkovic.kmp.currencyconverter.ui.common

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.content_description_network_status
import currencyconverter_kmp.composeapp.generated.resources.content_description_refresh_icon
import currencyconverter_kmp.composeapp.generated.resources.network_status_connected
import currencyconverter_kmp.composeapp.generated.resources.network_status_no_internet
import currencyconverter_kmp.composeapp.generated.resources.network_status_unknown
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.platform.platformHorizontalPadding
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

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
                    .padding(horizontal = platformHorizontalPadding()),
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
                    modifier = Modifier.size(MaterialTheme.spacing.iconSize),
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
                    modifier = Modifier.size(MaterialTheme.spacing.medium),
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
