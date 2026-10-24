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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phaselauncher.app.data.PlayerStats
import com.phaselauncher.app.data.ReloadApiClient
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.theme.BorderColor
import com.phaselauncher.app.ui.theme.CardDark
import com.phaselauncher.app.ui.theme.ErrorRed
import com.phaselauncher.app.ui.theme.NeonCyan
import com.phaselauncher.app.ui.theme.SurfaceDark
import com.phaselauncher.app.ui.theme.TextPrimary
import com.phaselauncher.app.ui.theme.TextSecondary

@Composable
fun LeaderboardScreen(
    apiClient: ReloadApiClient,
    apiUrl: String,
    username: String,
    avatarUrl: String
) {
    var leaderboard by remember { mutableStateOf<List<PlayerStats>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(apiUrl) {
        isLoading = true
        error = null

        val result = apiClient.fetchLeaderboard(apiUrl)
        result.onSuccess { data ->
            leaderboard = data
        }.onFailure { e ->
            error = e.message
        }

        isLoading = false
    }

    val filteredLeaderboard = leaderboard.filter {
        it.username.contains(searchQuery, ignoreCase = true)
    }

    val userStats = leaderboard.find { it.username.equals(username, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "LEADERBOARD",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Search players...",
                    color = TextSecondary
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonCyan
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { }
            ),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = BorderColor,
                cursorColor = NeonCyan,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (userStats != null) {
            UserStatsCard(
                username = username,
                avatarUrl = avatarUrl,
                rank = userStats.rank,
                arenaPoints = userStats.arenaPoints,
                kills = userStats.kills,
                wins = userStats.wins
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = NeonCyan
                    )
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading leaderboard",
                            color = ErrorRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error ?: "Unknown error",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            filteredLeaderboard.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No players found",
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredLeaderboard) { player ->
                        PlayerRow(player = player)
                    }
                }
            }
        }
    }
}

@Composable
fun UserStatsCard(
    username: String,
    avatarUrl: String,
    rank: Int,
    arenaPoints: Int,
    kills: Int,
    wins: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardDark,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = NeonCyan,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiscordAvatarCircle(
                avatarUrl = avatarUrl,
                size = 56
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = "YOUR STATS",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = username,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Rank", value = "#$rank")
            StatItem(label = "Hype", value = arenaPoints.toString())
            StatItem(label = "Kills", value = kills.toString())
            StatItem(label = "Wins", value = wins.toString())
        }
    }
}

@Composable
fun PlayerRow(player: PlayerStats) {
    val rankColor = when (player.rank) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> TextSecondary
    }

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "#${player.rank}",
                color = rankColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = player.username,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            StatColumn(label = "Hype", value = player.arenaPoints.toString())
            StatColumn(label = "Kills", value = player.kills.toString())
            StatColumn(label = "Wins", value = player.wins.toString())
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatColumn(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
