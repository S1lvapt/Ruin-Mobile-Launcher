package com.phaselauncher.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.navigation.AppNavigation

@Composable
fun PhaseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF00E5FF),
            secondary = Color(0xFF0066FF),
            background = Color(0xFF080B11),
            surface = Color(0xFF101520),
            onPrimary = Color(0xFF080B11),
            onBackground = Color(0xFFF3F6FB),
            onSurface = Color(0xFFF3F6FB)
        ),
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = AppPreferences(applicationContext)

        setContent {
            PhaseTheme {
                AppNavigation(preferences = preferences)
            }
        }
    }
}
