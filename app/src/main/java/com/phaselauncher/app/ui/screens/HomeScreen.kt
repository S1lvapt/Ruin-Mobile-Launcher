package com.phaselauncher.app.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.components.PhasePlayButton
import com.phaselauncher.app.ui.theme.*
import kotlinx.coroutines.launch

data class NewsArticle(val title: String, val date: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(preferences: AppPreferences) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentBuild by preferences.selectedBuild.collectAsState(initial = "Build 18.40 (Chapter 2 Season 8)")
    val targetPackage by preferences.targetPackage.collectAsState(initial = "com.ruin.fortnite")
    val discordUsername by preferences.discordUsername.collectAsState(initial = "Player")
    val discordAvatarUrl by preferences.discordAvatarUrl.collectAsState(initial = "https://cdn.discordapp.com/embed/avatars/0.png")

    var expandedDropdown by remember { mutableStateOf(false) }

    val builds = listOf(
        "Build 18.40 (Chapter 2 Season 8)",
        "Build 18.20 (Chapter 2 Season 8)",
        "Build 18.00 (Chapter 2 Season 8)",
        "Build 12.41 (Chapter 2 Season 2)"
    )

    val news = remember {
        listOf(
            NewsArticle("Reload Servers Online", "Active", "Connected to host 172.24.189.149:3551."),
            NewsArticle("C2S8 The Sideways Event", "Live", "Enter the Sideways anomalies to obtain exclusive weapons."),
            NewsArticle("Arena Tournament This Weekend", "Saturday", "Compete for Chapter 2 Season 8 Hype Points.")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PHASE LAUNCHER",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan
                    )
                    Text(
                        text = "ONLINE (C2S8)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlue
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = discordUsername,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    DiscordAvatarCircle(avatarUrl = discordAvatarUrl, size = 36.dp)
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=1000&auto=format&fit=crop",
                    contentDescription = "Season Art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, BgDark.copy(alpha = 0.95f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "CHAPTER 2 - SEASON 8",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Text(
                        text = "CUBED & THE SIDEWAYS",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                }
            }
        }

        item {
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = currentBuild,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selected Build") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false },
                    modifier = Modifier.background(SurfaceDark)
                ) {
                    builds.forEach { build ->
                        DropdownMenuItem(
                            text = { Text(build, color = TextPrimary) },
                            onClick = {
                                coroutineScope.launch {
                                    preferences.saveSelectedBuild(build)
                                }
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }
        }

        item {
            PhasePlayButton(
                onClick = {
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(targetPackage)
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(launchIntent)
                    } else {
                        Toast.makeText(
                            context,
                            "Error: Make sure that Ruin's game APK is installed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                text = "PLAY RUIN"
            )
        }

        item {
            Text(
                text = "LATEST NEWS",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(news) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(item.date, fontSize = 12.sp, color = NeonCyan)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.description, fontSize = 12.sp, color = TextSecondary)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(90.dp)) }
    }
}
