package com.phaselauncher.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.data.ReloadApiClient
import com.phaselauncher.app.ui.components.DiscordAvatarCircle
import com.phaselauncher.app.ui.screens.HomeScreen
import com.phaselauncher.app.ui.screens.LeaderboardScreen
import com.phaselauncher.app.ui.screens.SettingsScreen
import com.phaselauncher.app.ui.screens.ShopScreen
import com.phaselauncher.app.ui.screens.WelcomeScreen
import com.phaselauncher.app.ui.theme.BorderColor
import com.phaselauncher.app.ui.theme.ElectricBlue
import com.phaselauncher.app.ui.theme.NeonCyan
import com.phaselauncher.app.ui.theme.SurfaceDark

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Shop : Screen("shop")
    object Leaderboard : Screen("leaderboard")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    appPreferences: AppPreferences,
    apiClient: ReloadApiClient
) {
    val navController = rememberNavController()
    val isLoggedIn by appPreferences.isLoggedIn.collectAsState(initial = false)
    val username by appPreferences.discordUsername.collectAsState(initial = "")
    val avatarUrl by appPreferences.discordAvatarUrl.collectAsState(initial = "")
    val apiUrl by appPreferences.apiUrl.collectAsState(initial = "http://172.24.189.149:3551")
    val liquidGlassEnabled by appPreferences.liquidGlassEnabled.collectAsState(initial = false)

    val startRoute = if (isLoggedIn) Screen.Home.route else Screen.Welcome.route

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                ) + scaleIn(
                    initialScale = 0.98f,
                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(180, easing = FastOutLinearInEasing)
                ) + scaleOut(
                    targetScale = 0.98f,
                    animationSpec = tween(180, easing = FastOutLinearInEasing)
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                ) + scaleIn(
                    initialScale = 0.98f,
                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(180, easing = FastOutLinearInEasing)
                ) + scaleOut(
                    targetScale = 0.98f,
                    animationSpec = tween(180, easing = FastOutLinearInEasing)
                )
            }
        ) {
            composable(Screen.Welcome.route) {
                WelcomeScreen(
                    appPreferences = appPreferences,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    appPreferences = appPreferences,
                    username = username,
                    avatarUrl = avatarUrl
                )
            }

            composable(Screen.Shop.route) {
                ShopScreen(
                    apiClient = apiClient,
                    apiUrl = apiUrl,
                    accountId = "default_account"
                )
            }

            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(
                    apiClient = apiClient,
                    apiUrl = apiUrl,
                    username = username,
                    avatarUrl = avatarUrl
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    appPreferences = appPreferences,
                    username = username,
                    avatarUrl = avatarUrl,
                    onLogout = {
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        if (isLoggedIn) {
            FloatingCapsuleNavBar(
                navController = navController,
                avatarUrl = avatarUrl,
                liquidGlassEnabled = liquidGlassEnabled,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun FloatingCapsuleNavBar(
    navController: NavHostController,
    avatarUrl: String,
    liquidGlassEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        NavItem(Screen.Home.route, Icons.Default.SportsEsports, "Home"),
        NavItem(Screen.Shop.route, Icons.Default.ShoppingCart, "Shop"),
        NavItem(Screen.Leaderboard.route, Icons.Default.EmojiEvents, "Leaderboard"),
        NavItem(Screen.Settings.route, Icons.Default.Tune, "Settings")
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RoundedCornerShape(32.dp),
        shadowElevation = if (liquidGlassEnabled) 8.dp else 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .then(
                    if (liquidGlassEnabled) {
                        Modifier
                            .background(
                                color = SurfaceDark.copy(alpha = 0.65f)
                            )
                            .border(
                                width = 1.dp,
                                color = BorderColor.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(32.dp)
                            )
                    } else {
                        Modifier
                            .background(color = SurfaceDark)
                            .border(
                                width = 1.dp,
                                color = BorderColor,
                                shape = RoundedCornerShape(32.dp)
                            )
                    }
                )
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, item ->
                NavItem(
                    icon = item.icon,
                    isSelected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            DiscordAvatarCircle(
                avatarUrl = avatarUrl,
                size = 40,
                modifier = Modifier.clickable {
                    selectedTab = 3
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) NeonCyan else Color.Transparent,
        animationSpec = androidx.compose.animation.core.spring(
            stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
        ),
        label = "backgroundColor"
    )

    val iconColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) Color.Black else NeonCyan,
        animationSpec = androidx.compose.animation.core.spring(
            stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
        ),
        label = "iconColor"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

data class NavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)
