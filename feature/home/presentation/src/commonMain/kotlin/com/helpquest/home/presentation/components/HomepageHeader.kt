package com.helpquest.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.brand.BrandLogo
import com.helpquest.core.designsystem.components.buttons.HelpQuestIconButton
import com.helpquest.core.designsystem.components.dropdown.DropDownItem
import com.helpquest.core.designsystem.components.dropdown.HelpQuestDropDownMenu
import com.helpquest.core.designsystem.components.generic.GenericPageHeaderSection
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.appName
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import helpquest.core.designsystem.generated.resources.ic_hamburger_menu
import helpquest.core.designsystem.generated.resources.log_out_icon
import helpquest.core.designsystem.generated.resources.profile_settings
import helpquest.core.designsystem.generated.resources.users_icon
import helpquest.feature.home.presentation.generated.resources.Res
import helpquest.feature.home.presentation.generated.resources.logout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun HomepageHeader(
    localParticipant: ParticipantUi?,
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
            BrandLogo(
                tintColor = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = appName(),
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
    localParticipant: ParticipantUi?,
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
        if (localParticipant != null) {
            HelpQuestAvatar(
                displayText = localParticipant.initials,
                userImageUrl = localParticipant.imageUrl,
                showUserIdentity = true,
                classImageUrl = localParticipant.classImageUrl,
                showClass = true,
                onClick = onClick
            )
        } else {
            HelpQuestIconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.ic_hamburger_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

        HelpQuestDropDownMenu(
            isOpen = isMenuOpen,
            onDismiss = onDismissMenu,
            items = listOf(
                DropDownItem(
                    title = stringResource(DesignSystemRes.string.profile_settings),
                    icon = vectorResource(DesignSystemRes.drawable.users_icon),
                    contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                    onClick = onProfileSettingsClick
                ),
                DropDownItem(
                    title = stringResource(Res.string.logout),
                    icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                    contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                    onClick = onLogoutClick
                )
            )
        )
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