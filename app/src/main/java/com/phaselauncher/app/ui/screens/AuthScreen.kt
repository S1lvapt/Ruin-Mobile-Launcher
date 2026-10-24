package com.phaselauncher.app.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.ui.theme.*
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit = {},
    preferences: AppPreferences? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val loginUrl by preferences?.loginUrl?.collectAsState(initial = "http://172.24.189.149:8080/login") ?: remember { mutableStateOf("http://172.24.189.149:8080/login") }
    var isOpeningWebView by remember { mutableStateOf(false) }

    if (isOpeningWebView) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DISCORD LOGIN",
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                TextButton(onClick = { isOpeningWebView = false }) {
                    Text("CANCEL", color = ErrorRed)
                }
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                val url = request?.url?.toString() ?: return false
                                if (url.contains("code=") || url.contains("token=") || url.contains("success")) {
                                    val uri = Uri.parse(url)
                                    val user = uri.getQueryParameter("username") ?: "DiscordPlayer"
                                    val avatar = uri.getQueryParameter("avatar") ?: "https://cdn.discordapp.com/embed/avatars/0.png"
                                    val id = uri.getQueryParameter("id") ?: ""

                                    coroutineScope.launch {
                                        preferences?.saveDiscordSession(user, avatar, id)
                                        onLoginSuccess()
                                    }
                                    return true
                                }
                                return false
                            }
                        }
                        loadUrl(loginUrl)
                    }
                }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgDark)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PHASE",
                color = NeonCyan,
                fontSize = 46.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
            Text(
                text = "CHAPTER 2 SEASON 8 - CUBED",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 36.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Authentication Required",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Sign in using your Discord account to sync stats and access the launcher.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    Button(
                        onClick = { isOpeningWebView = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DiscordBlurple),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "LOGIN WITH DISCORD",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    coroutineScope.launch {
                        preferences?.saveDiscordSession("Player_Guest", "https://cdn.discordapp.com/embed/avatars/0.png", "guest_001")
                        onLoginSuccess()
                    }
                }
            ) {
                Text(
                    text = "Quick Play as Guest (Offline Bypass)",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
