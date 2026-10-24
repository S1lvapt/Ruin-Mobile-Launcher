package com.phaselauncher.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.data.ReloadApiClient
import com.phaselauncher.app.ui.navigation.AppNavigation
import com.phaselauncher.app.ui.theme.PhaseLauncherTheme

class MainActivity : ComponentActivity() {
    private lateinit var appPreferences: AppPreferences
    private lateinit var apiClient: ReloadApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appPreferences = AppPreferences(applicationContext)
        apiClient = ReloadApiClient()

        setContent {
            PhaseLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        appPreferences = appPreferences,
                        apiClient = apiClient
                    )
                }
            }
        }
    }
}
