package com.opollo.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileHeader(
    userProfile: UserProfile,
    email:String,
    isGuest:Boolean,
    onEditProfile: () -> Unit
) {
    val username = email.split("@")[0]
    val capitalized = username.replaceFirstChar {
        if(it.isLowerCase()) it.titlecase() else it.toString()
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onEditProfile() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isGuest) "GU" else email.take(2).uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if(isGuest) "Guest" else capitalized,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if(isGuest)"Guest Account" else email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!userProfile.isGuest) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Registered User",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}