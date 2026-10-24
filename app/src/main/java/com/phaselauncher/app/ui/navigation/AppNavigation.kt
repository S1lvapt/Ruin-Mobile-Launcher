package com.phaselauncher.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.screens.*
import com.phaselauncher.app.ui.theme.*

data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppNavigation(preferences: AppPreferences) {
    val navController = rememberNavController()
    val isLoggedIn by preferences.isLoggedIn.collectAsState(initial = false)
    val liquidGlassEnabled by preferences.liquidGlassEnabled.collectAsState(initial = false)
    val discordAvatarUrl by preferences.discordAvatarUrl.collectAsState(initial = "https://cdn.discordapp.com/embed/avatars/0.png")

    if (!isLoggedIn) {
        AuthScreen(onLoginSuccess = {})
    } else {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "home"

                val navItems = listOf(
                    NavItem("home", "Play", Icons.Default.SportsEsports),
                    NavItem("shop", "Shop", Icons.Default.ShoppingCart),
                    NavItem("leaderboard", "Ranks", Icons.Default.EmojiEvents),
                    NavItem("settings", "Config", Icons.Default.Tune)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val barBg = if (liquidGlassEnabled) SurfaceDark.copy(alpha = 0.70f) else SurfaceDark
                    val barBorder = if (liquidGlassEnabled) NeonCyan.copy(alpha = 0.4f) else BorderColor

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        shape = RoundedCornerShape(34.dp),
                        color = barBg,
                        border = BorderStroke(1.dp, barBorder),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            navItems.forEach { item ->
                                val isSelected = currentRoute == item.route
                                val animatedBg by animateColorAsState(
                                    targetValue = if (isSelected) NeonCyan.copy(alpha = 0.18f) else Color.Transparent,
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                    label = "tab_bg"
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(animatedBg)
                                        .clickable { navController.navigate(item.route) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        tint = if (isSelected) NeonCyan else TextSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            // Avatar do Discord na ponta direita ao lado de Settings
                            Box(
                                modifier = Modifier
                                    .clickable { navController.navigate("settings") }
                                    .padding(4.dp)
                            ) {
                                DiscordAvatarCircle(avatarUrl = discordAvatarUrl, size = 34.dp)
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding),
                enterTransition = {
                    fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                            scaleIn(initialScale = 0.98f, animationSpec = tween(220))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(180, easing = FastOutLinearInEasing))
                }
            ) {
                composable("home") { HomeScreen(preferences = preferences) }
                composable("shop") { ShopScreen(preferences = preferences) }
                composable("leaderboard") { LeaderboardScreen(preferences = preferences) }
                composable("settings") { SettingsScreen(preferences = preferences) }
            }
        }
    }
}
