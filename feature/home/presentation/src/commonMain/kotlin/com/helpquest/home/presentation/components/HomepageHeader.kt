package com.helpquest.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.brand.HelpQuestBrandLogo
import com.helpquest.core.designsystem.components.generic.GenericPageHeaderSection
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDivider
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import helpquest.core.designsystem.generated.resources.app_name
import helpquest.core.designsystem.generated.resources.log_out_icon
import helpquest.core.designsystem.generated.resources.users_icon
import helpquest.feature.home.presentation.generated.resources.Res
import helpquest.feature.home.presentation.generated.resources.logout
import helpquest.feature.home.presentation.generated.resources.profile_settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun HomepageHeader(
    localParticipant: ParticipantUi,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericPageHeaderSection(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            HelpQuestBrandLogo(
                tintColor = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = stringResource(DesignSystemRes.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            ProfileAvatarSection(
                localParticipant = localParticipant,
                isMenuOpen = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onDismissMenu = onDismissMenu,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick,
            )
        }
    }
}

@Composable
fun ProfileAvatarSection(
    localParticipant: ParticipantUi,
    isMenuOpen: Boolean,
    onClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        HelpQuestAvatar(
            displayText = localParticipant.initials,
            userImageUrl = localParticipant.imageUrl,
            showUserIdentity = true,
            classImageUrl = localParticipant.classImageUrl,
            showClass = true,
            onClick = onClick
        )

        DropdownMenu(
            expanded = isMenuOpen,
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = onDismissMenu,
            containerColor = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline
            )
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(DesignSystemRes.drawable.users_icon),
                            contentDescription = stringResource(Res.string.profile_settings),
                            tint = MaterialTheme.colorScheme.extended.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(Res.string.profile_settings),
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onProfileSettingsClick()
                }
            )
            HelpQuestHorizontalDivider()
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(DesignSystemRes.drawable.log_out_icon),
                            contentDescription = stringResource(Res.string.logout),
                            tint = MaterialTheme.colorScheme.extended.destructiveHover,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(Res.string.logout),
                            color = MaterialTheme.colorScheme.extended.destructiveHover,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onLogoutClick()
                }
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HomepageHeaderLightPreview() {
    HelpQuestTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomepageHeader(
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                isUserMenuOpen = true,
                onUserAvatarClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {}
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HomepageHeaderDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomepageHeader(
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                isUserMenuOpen = true,
                onUserAvatarClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {}
            )
        }
    }
}