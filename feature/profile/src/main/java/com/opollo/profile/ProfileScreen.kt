package com.opollo.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.profile.components.LogoutButton
import com.opollo.profile.components.SettingsItem
import com.opollo.profile.components.SettingsSection
import com.opollo.profile.components.UpgradeCard
import com.opollo.profile.components.UserProfile
import com.opollo.profile.components.UserProfileHeader
import com.opollo.profile.components.UserStatsCard


@Composable
fun ProfileSettingsScreen(
    userProfile: UserProfile = UserProfile(
        name = "Guest User",
        email = "Sign up to save your progress",
        initials = "GU",
        isGuest = true
    ),
    onUpgrade: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onNotificationSettings: () -> Unit = {},
    onPlaybackSettings: () -> Unit = {},
    onDownloadSettings: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onSupport: () -> Unit = {},
    onAbout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val isGuest by viewModel.isAnonymous.collectAsState()
    val email by viewModel.userEmail.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            UserProfileHeader(
                userProfile = userProfile,
                onEditProfile = onEditProfile,
                isGuest = isGuest,
                email = email
            )
        }
        item {
            UserStatsCard(userProfile = userProfile)
        }

        if (userProfile.isGuest) {
            item {
                UpgradeCard(onUpgrade = onUpgrade)
            }
        }

        item {
            SettingsSection(
                title = "Account",
                items = listOf(
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Edit Profile",
                        subtitle = "Update your name and profile picture",
                        onClick = onEditProfile
                    ),
                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Manage your notification preferences",
                        onClick = onNotificationSettings
                    )
                )
            )
        }

        item {
            SettingsSection(
                title = "Playback",
                items = listOf(
                    SettingsItem(
                        icon = Icons.Default.PlayArrow,
                        title = "Playback Settings",
                        subtitle = "Audio quality, speed controls",
                        onClick = onPlaybackSettings
                    ),
                    SettingsItem(
                        icon = Icons.Default.Download,
                        title = "Downloads",
                        subtitle = "Manage offline content",
                        onClick = onDownloadSettings
                    )
                )
            )
        }

        item {
            SettingsSection(
                title = "Support & Legal",
                items = listOf(
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & Security",
                        subtitle = "Control your data and privacy",
                        onClick = onPrivacy
                    ),
                    SettingsItem(
                        icon = Icons.AutoMirrored.Filled.Help,
                        title = "Help & Support",
                        subtitle = "Get help or contact us",
                        onClick = onSupport
                    ),
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About",
                        subtitle = "App version and legal information",
                        onClick = onAbout
                    )
                )
            )
        }
        item {
            LogoutButton(onLogout = {viewModel.logout()})
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
