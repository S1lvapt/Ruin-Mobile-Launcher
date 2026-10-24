package com.phaselauncher.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phaselauncher.app.data.AppPreferences
import com.phaselauncher.app.data.ReloadApiClient
import com.phaselauncher.app.data.ShopItem
import com.phaselauncher.app.ui.components.VBucksBadge
import com.phaselauncher.app.ui.theme.*

typealias CatalogItem = ShopItem

@Composable
fun ShopScreen(preferences: AppPreferences) {
    val apiUrl by preferences.apiUrl.collectAsState(initial = "http://172.24.189.149:3551")
    val discordId by preferences.discordId.collectAsState(initial = "")
    val client = remember { ReloadApiClient() }

    var shopItems by remember { mutableStateOf<List<ShopItem>>(emptyList()) }
    var vbucksBalance by remember { mutableStateOf(1500) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(apiUrl) {
        isLoading = true
        shopItems = client.fetchCatalog(apiUrl)
        vbucksBalance = client.fetchVBucksBalance(apiUrl, discordId)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ITEM SHOP",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "CHAPTER 2 SEASON 8",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            }
            VBucksBadge(amount = vbucksBalance)
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonCyan)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(shopItems) { item ->
                    val borderColor = when (item.rarity) {
                        "Legendary" -> VBucksGold
                        "Epic" -> CubePurple
                        else -> ElectricBlue
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = CardDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .background(SurfaceDark, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.rarity.uppercase(),
                                    color = borderColor,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${item.price} V-BUCKS",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = VBucksGold
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
