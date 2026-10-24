package com.phaselauncher.app.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.theme.CubePurple
import com.phaselauncher.app.ui.theme.DiscordBlurple
import com.phaselauncher.app.ui.theme.ElectricBlue
import com.phaselauncher.app.ui.theme.NeonCyan
import com.phaselauncher.app.ui.theme.SurfaceDark
import com.phaselauncher.app.ui.theme.TextPrimary

@Composable
fun WelcomeScreen(
    appPreferences: AppPreferences,
    onLoginSuccess: () -> Unit
) {
    var showWebView by remember { mutableStateOf(false) }
    var loginUrl by remember { mutableStateOf("") }

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        if (showWebView) {
            WebViewLogin(
                url = loginUrl,
                onLoginSuccess = { username, avatarUrl, id ->
                    onLoginSuccess()
                },
                onClose = {
                    showWebView = false
                }
            )
        } else {
            WelcomeContent(
                onLoginClick = { url ->
                    loginUrl = url
                    showWebView = true
                },
                appPreferences = appPreferences
            )
        }
    }
}

@Composable
fun WelcomeContent(
    onLoginClick: (String) -> Unit,
    appPreferences: AppPreferences
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonCyan,
                            ElectricBlue,
                            CubePurple
                        )
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PH",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "PHASE LAUNCHER",
            color = TextPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Chapter 2 Season 8 (CUBED)",
            color = NeonCyan,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Build 18.40",
            color = TextPrimary.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {
                onLoginClick("http://172.24.189.149:8080/login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DiscordBlurple
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "LOGIN WITH DISCORD",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun WebViewLogin(
    url: String,
    onLoginSuccess: (String, String, String) -> Unit,
    onClose: () -> Unit
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (url?.contains("callback") == true || url?.contains("success") == true) {
                            val username = view?.title ?: "User"
                            val avatarUrl = ""
                            val id = "123456789"
                            onLoginSuccess(username, avatarUrl, id)
                        }
                    }
                }
                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
