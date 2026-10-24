package com.phaselauncher.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.navigation.AppNavigation
import com.phaselauncher.app.ui.theme.PhaseTheme

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
