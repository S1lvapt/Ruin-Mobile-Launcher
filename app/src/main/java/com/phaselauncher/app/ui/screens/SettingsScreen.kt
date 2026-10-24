package com.phaselauncher.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.theme.BorderColor
import com.phaselauncher.app.ui.theme.CardDark
import com.phaselauncher.app.ui.theme.ErrorRed
import com.phaselauncher.app.ui.theme.SurfaceDark
import com.phaselauncher.app.ui.theme.TextPrimary
import com.phaselauncher.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    appPreferences: AppPreferences,
    username: String,
    avatarUrl: String,
    onLogout: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val liquidGlassEnabled by appPreferences.liquidGlassEnabled.collectAsState(initial = false)
    val apiUrl by appPreferences.apiUrl.collectAsState(initial = "http://172.24.189.149:3551")
    val loginUrl by appPreferences.loginUrl.collectAsState(initial = "http://172.24.189.149:8080/login")
    val targetPackage by appPreferences.targetPackage.collectAsState(initial = "com.ruin.fortnite")

    var tempApiUrl by remember { mutableStateOf(apiUrl) }
    var tempLoginUrl by remember { mutableStateOf(loginUrl) }
    var tempTargetPackage by remember { mutableStateOf(targetPackage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SETTINGS",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        UserProfileCard(
            username = username,
            avatarUrl = avatarUrl,
            onLogout = onLogout
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingSection(title = "APPEARANCE") {
            SettingToggle(
                label = "Liquid Glass Mode",
                description = "Enable translucent frosted glass effect",
                checked = liquidGlassEnabled,
                onCheckedChange = { enabled ->
                    coroutineScope.launch {
                        appPreferences.setLiquidGlassEnabled(enabled)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingSection(title = "SERVER CONFIGURATION") {
            SettingTextField(
                label = "API URL",
                value = tempApiUrl,
                onValueChange = { tempApiUrl = it },
                onSave = {
                    coroutineScope.launch {
                        appPreferences.setApiUrl(tempApiUrl)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingTextField(
                label = "Login URL",
                value = tempLoginUrl,
                onValueChange = { tempLoginUrl = it },
                onSave = {
                    coroutineScope.launch {
                        appPreferences.setLoginUrl(tempLoginUrl)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingTextField(
                label = "Target Package",
                value = tempTargetPackage,
                onValueChange = { tempTargetPackage = it },
                onSave = {
                    coroutineScope.launch {
                        appPreferences.setTargetPackage(tempTargetPackage)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingSection(title = "ABOUT") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = CardDark,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                InfoRow(label = "App Name", value = "Phase Launcher")
                InfoRow(label = "Version", value = "1.0.0")
                InfoRow(label = "Build", value = "18.40")
                InfoRow(label = "Season", value = "Chapter 2 Season 8")
            }
        }
    }
}

@Composable
fun UserProfileCard(
    username: String,
    avatarUrl: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardDark,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiscordAvatarCircle(
                avatarUrl = avatarUrl,
                size = 64
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = username,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Discord Account",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ErrorRed
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "LOG OUT OF DISCORD",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun SettingSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun SettingToggle(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardDark,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardDark,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedIndicatorColor = BorderColor,
                unfocusedIndicatorColor = BorderColor
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5865F2)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
