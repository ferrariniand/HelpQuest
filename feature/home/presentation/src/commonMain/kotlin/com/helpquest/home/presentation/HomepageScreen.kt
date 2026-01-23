package com.helpquest.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.buttons.HelpQuestFloatingActionButton
import com.helpquest.core.designsystem.components.containers_layouts.SnackbarScaffold
import com.helpquest.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.home.presentation.components.HomepageHeader
import helpquest.core.designsystem.generated.resources.cancel
import helpquest.feature.home.presentation.generated.resources.Res
import helpquest.feature.home.presentation.generated.resources.do_you_want_to_logout
import helpquest.feature.home.presentation.generated.resources.do_you_want_to_logout_desc
import helpquest.feature.home.presentation.generated.resources.logout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun HomepageRoot(
    onLogout: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onChatFabButtonClick: () -> Unit,
    onQuestFabButtonClick: () -> Unit,
    viewModel: HomepageViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            else -> Unit
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    HomepageScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HomepageAction.OnConfirmLogout -> onLogout()
                HomepageAction.OnProfileSettingsClick -> onProfileSettingsClick()
                HomepageAction.OnChatFabButtonClick -> onChatFabButtonClick()
                HomepageAction.OnQuestFabButtonClick -> onQuestFabButtonClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HomepageScreen(
    state: HomepageState,
    onAction: (HomepageAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    SnackbarScaffold(
        snackbarHostState = snackbarHostState,
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.extended.surfaceLower,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomepageHeader(
                localParticipant = state.localParticipant,
                isUserMenuOpen = state.isUserMenuOpen,
                onUserAvatarClick = {
                    onAction(HomepageAction.OnUserAvatarClick)
                },
                onLogoutClick = {
                    onAction(HomepageAction.OnLogoutClick)
                },
                onDismissMenu = {
                    onAction(HomepageAction.OnDismissUserMenu)
                },
                onProfileSettingsClick = {
                    onAction(HomepageAction.OnProfileSettingsClick)
                }
            )
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(
                                horizontal = 8.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HelpQuestFloatingActionButton(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                onAction(HomepageAction.OnChatFabButtonClick)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                            )
                        }

                        HelpQuestFloatingActionButton(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                onAction(HomepageAction.OnQuestFabButtonClick)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showLogoutConfirmation) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.do_you_want_to_logout),
            description = stringResource(Res.string.do_you_want_to_logout_desc),
            confirmButtonText = stringResource(Res.string.logout),
            cancelButtonText = stringResource(DesignSystemRes.string.cancel),
            onDismiss = {
                onAction(HomepageAction.OnDismissLogoutDialog)
            },
            onCancelClick = {
                onAction(HomepageAction.OnDismissLogoutDialog)
            },
            onConfirmClick = {
                onAction(HomepageAction.OnConfirmLogout)
            },
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun HomepageScreenLightPreview() {
    HelpQuestTheme {
        HomepageScreen(
            state = HomepageState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun HomepageScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        HomepageScreen(
            state = HomepageState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}