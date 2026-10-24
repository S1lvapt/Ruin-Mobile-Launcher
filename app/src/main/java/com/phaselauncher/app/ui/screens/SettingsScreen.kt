package com.phaselauncher.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.components.PhasePlayButton
import com.phaselauncher.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(preferences: AppPreferences) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val initialApiUrl by preferences.apiUrl.collectAsState(initial = "http://172.24.189.149:3551")
    val initialLoginUrl by preferences.loginUrl.collectAsState(initial = "http://172.24.189.149:8080/login")
    val initialPackage by preferences.targetPackage.collectAsState(initial = "com.ruin.fortnite")
    val liquidGlassEnabled by preferences.liquidGlassEnabled.collectAsState(initial = false)

    val discordUsername by preferences.discordUsername.collectAsState(initial = "Player")
    val discordAvatarUrl by preferences.discordAvatarUrl.collectAsState(initial = "https://cdn.discordapp.com/embed/avatars/0.png")
    val discordId by preferences.discordId.collectAsState(initial = "")

    var currentApiUrl by remember { mutableStateOf("") }
    var currentLoginUrl by remember { mutableStateOf("") }
    var currentPkg by remember { mutableStateOf("") }

    LaunchedEffect(initialApiUrl, initialLoginUrl, initialPackage) {
        if (currentApiUrl.isEmpty()) currentApiUrl = initialApiUrl
        if (currentLoginUrl.isEmpty()) currentLoginUrl = initialLoginUrl
        if (currentPkg.isEmpty()) currentPkg = initialPackage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(16.dp)
    ) {
        Text(
            text = "SETTINGS",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // User Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DiscordAvatarCircle(avatarUrl = discordAvatarUrl, size = 48.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = discordUsername,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        if (discordId.isNotBlank()) {
                            Text(
                                text = "ID: $discordId",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            preferences.logout()
                            Toast.makeText(context, "Logged out of Discord", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("LOG OUT OF DISCORD", fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Appearance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Liquid Glass Mode",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Translucent frosted bottom dock",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Switch(
                    checked = liquidGlassEnabled,
                    onCheckedChange = {
                        coroutineScope.launch {
                            preferences.setLiquidGlassEnabled(it)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeonCyan,
                        checkedTrackColor = ElectricBlue
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Server Configuration Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Server & Game Configuration",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NeonCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = currentApiUrl,
                    onValueChange = { currentApiUrl = it },
                    label = { Text("Reload Backend URL (port 3551)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = currentLoginUrl,
                    onValueChange = { currentLoginUrl = it },
                    label = { Text("Discord Auth URL (port 8080)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = currentPkg,
                    onValueChange = { currentPkg = it },
                    label = { Text("Target Game Package") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                PhasePlayButton(
                    onClick = {
                        coroutineScope.launch {
                            preferences.saveApiUrl(currentApiUrl)
                            preferences.saveLoginUrl(currentLoginUrl)
                            preferences.saveTargetPackage(currentPkg)
                            Toast.makeText(context, "Settings saved successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    text = "SAVE SETTINGS"
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
