package com.phaselauncher.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.data.PlayerStats
import com.phaselauncher.app.data.ReloadApiClient
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.theme.*

@Composable
fun LeaderboardScreen(preferences: AppPreferences? = null) {
    val client = remember { ReloadApiClient() }
    var searchQuery by remember { mutableStateOf("") }
    var players by remember { mutableStateOf<List<PlayerStats>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val apiUrl = preferences?.apiUrl?.collectAsState(initial = "http://172.24.189.149:3551")?.value ?: "http://172.24.189.149:3551"
    val discordUsername = preferences?.discordUsername?.collectAsState(initial = "Player")?.value ?: "Player"
    val discordAvatarUrl = preferences?.discordAvatarUrl?.collectAsState(initial = "https://cdn.discordapp.com/embed/avatars/0.png")?.value ?: "https://cdn.discordapp.com/embed/avatars/0.png"

    LaunchedEffect(apiUrl) {
        isLoading = true
        players = client.fetchLeaderboard(apiUrl)
        isLoading = false
    }

    val filteredPlayers = remember(searchQuery, players) {
        if (searchQuery.isBlank()) players
        else players.filter { it.username.contains(searchQuery.trim(), ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(16.dp)
    ) {
        Text(
            text = "LEADERBOARD",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )
        Text(
            text = "CHAPTER 2 SEASON 8 - ARENA HYPE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search player username...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonCyan) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = BorderColor,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Pinned User Stats Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, ElectricBlue), RoundedCornerShape(10.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DiscordAvatarCircle(avatarUrl = discordAvatarUrl, size = 44.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = discordUsername,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Your Personal Stats",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "12,450 HYPE",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VBucksGold
                    )
                    Text(
                        text = "340 Wins • 2,120 Kills",
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonCyan)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(filteredPlayers) { index, player ->
                    val rank = index + 1
                    val rankColor = when (rank) {
                        1 -> VBucksGold
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> TextSecondary
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "#$rank",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = rankColor,
                                    modifier = Modifier.width(36.dp)
                                )
                                Column {
                                    Text(
                                        text = player.username,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${player.wins} Wins • ${player.kills} Kills",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                            Text(
                                text = "${player.points} PTS",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = NeonCyan
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
