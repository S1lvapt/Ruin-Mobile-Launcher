package com.phaselauncher.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phaselauncher.app.data.CatalogItem
import com.phaselauncher.app.data.ReloadApiClient
import com.phaselauncher.app.ui.components.VBucksBadge
import com.phaselauncher.app.ui.theme.BorderColor
import com.phaselauncher.app.ui.theme.CardDark
import com.phaselauncher.app.ui.theme.ElectricBlue
import com.phaselauncher.app.ui.theme.ErrorRed
import com.phaselauncher.app.ui.theme.NeonCyan
import com.phaselauncher.app.ui.theme.SurfaceDark
import com.phaselauncher.app.ui.theme.TextPrimary
import com.phaselauncher.app.ui.theme.TextSecondary
import com.phaselauncher.app.ui.theme.VBucksGold

@Composable
fun ShopScreen(
    apiClient: ReloadApiClient,
    apiUrl: String,
    accountId: String
) {
    var vBucksBalance by remember { mutableStateOf(0) }
    var catalogItems by remember { mutableStateOf<List<CatalogItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(apiUrl, accountId) {
        isLoading = true
        error = null

        val balanceResult = apiClient.fetchVBucksBalance(apiUrl, accountId)
        val catalogResult = apiClient.fetchCatalog(apiUrl)

        balanceResult.onSuccess { balance ->
            vBucksBalance = balance
        }.onFailure { e ->
            error = e.message
        }

        catalogResult.onSuccess { items ->
            catalogItems = items
        }.onFailure { e ->
            error = e.message
        }

        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ITEM SHOP",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            VBucksBadge(balance = vBucksBalance)
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                            text = "Error loading shop",
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

            catalogItems.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No items available",
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(catalogItems) { item ->
                        CatalogItemCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogItemCard(item: CatalogItem) {
    val rarityColor = when (item.rarity) {
        "Legendary" -> Color(0xFFFFD700)
        "Epic" -> Color(0xFF9932CC)
        "Rare" -> Color(0xFF0070DD)
        "Uncommon" -> Color(0xFF1EFF00)
        else -> Color(0xFF9D9D9D)
    }

    Column(
        modifier = Modifier
            .background(
                color = CardDark,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = rarityColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    color = BorderColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name.take(2).uppercase(),
                color = TextSecondary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = item.name,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "V",
                color = VBucksGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = item.price.toString(),
                color = VBucksGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.rarity,
            color = rarityColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
