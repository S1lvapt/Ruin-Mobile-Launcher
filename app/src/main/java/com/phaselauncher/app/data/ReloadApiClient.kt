package com.phaselauncher.app.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

data class CatalogItem(
    val id: String,
    val name: String,
    val price: Int,
    val rarity: String,
    val imageUrl: String
)

data class PlayerStats(
    val rank: Int,
    val username: String,
    val arenaPoints: Int,
    val kills: Int,
    val wins: Int
)

class ReloadApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun fetchCatalog(apiUrl: String): Result<List<CatalogItem>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$apiUrl/fortnite/api/storefront/v2/catalog")
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Failed to fetch catalog: ${response.code}"))
            }

            val responseBody = response.body?.string() ?: return@withContext Result.failure(
                Exception("Empty response body")
            )

            val catalogItems = parseCatalog(responseBody)
            Result.success(catalogItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseCatalog(json: String): List<CatalogItem> {
        val items = mutableListOf<CatalogItem>()
        try {
            val jsonObject = JsonParser.parseString(json).asJsonObject
            val storefronts = jsonObject.getAsJsonArray("storefronts") ?: return items

            for (storefront in storefronts) {
                val storefrontObj = storefront.asJsonObject
                val catalogEntries = storefrontObj.getAsJsonArray("catalogEntries") ?: continue

                for (entry in catalogEntries) {
                    val entryObj = entry.asJsonObject
                    val offers = entryObj.getAsJsonArray("offers") ?: continue

                    for (offer in offers) {
                        val offerObj = offer.asJsonObject
                        val displayData = offerObj.getAsJsonObject("displayData") ?: continue
                        val price = offerObj.getAsJsonObject("price")?.getAsJsonArray("offerId")?.get(0)?.asInt ?: 0

                        val name = displayData.get("title")?.asString ?: "Unknown"
                        val description = displayData.get("description")?.asString ?: ""
                        val rarity = extractRarity(description)
                        val imageUrl = extractImageUrl(displayData)

                        items.add(
                            CatalogItem(
                                id = offerObj.get("offerId")?.asString ?: "",
                                name = name,
                                price = price,
                                rarity = rarity,
                                imageUrl = imageUrl
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            return getFallbackCatalog()
        }
        return items
    }

    private fun extractRarity(description: String): String {
        return when {
            description.contains("Legendary", ignoreCase = true) -> "Legendary"
            description.contains("Epic", ignoreCase = true) -> "Epic"
            description.contains("Rare", ignoreCase = true) -> "Rare"
            description.contains("Uncommon", ignoreCase = true) -> "Uncommon"
            else -> "Common"
        }
    }

    private fun extractImageUrl(displayData: JsonObject): String {
        val renderData = displayData.getAsJsonObject("renderData") ?: return ""
        val mainImage = renderData.getAsJsonArray("offerImages")?.get(0)?.asJsonObject
        return mainImage?.get("url")?.asString ?: ""
    }

    private fun getFallbackCatalog(): List<CatalogItem> {
        return listOf(
            CatalogItem("1", "CUBED Outfit", 1500, "Legendary", ""),
            CatalogItem("2", "Cube Assassin", 2000, "Epic", ""),
            CatalogItem("3", "Sideways Pickaxe", 800, "Rare", ""),
            CatalogItem("4", "Cube Back Bling", 500, "Uncommon", ""),
            CatalogItem("5", "Glider Pack", 1200, "Epic", "")
        )
    }

    suspend fun fetchVBucksBalance(apiUrl: String, accountId: String): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$apiUrl/fortnite/api/game/v2/profile/$accountId/client/QueryProfile?profileId=common_core")
                .post("{}".toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Failed to fetch V-Bucks: ${response.code}"))
            }

            val responseBody = response.body?.string() ?: return@withContext Result.failure(
                Exception("Empty response body")
            )

            val balance = parseVBucksBalance(responseBody)
            Result.success(balance)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseVBucksBalance(json: String): Int {
        try {
            val jsonObject = JsonParser.parseString(json).asJsonObject
            val profileChanges = jsonObject.getAsJsonArray("profileChanges") ?: return 0

            for (change in profileChanges) {
                val changeObj = change.asJsonObject
                val profile = changeObj.getAsJsonObject("profile") ?: continue
                val items = profile.getAsJsonObject("items") ?: continue

                var totalBalance = 0
                for ((_, itemValue) in items.entrySet()) {
                    val itemObj = itemValue.asJsonObject
                    val templateId = itemObj.get("templateId")?.asString ?: continue

                    if (templateId.contains("Currency:MtxPurchased") || templateId.contains("Currency:MtxGiveaway")) {
                        val quantity = itemObj.getAsJsonObject("attributes")?.get("quantity")?.asInt ?: 0
                        totalBalance += quantity
                    }
                }
                return totalBalance
            }
        } catch (e: Exception) {
            return 0
        }
        return 0
    }

    suspend fun fetchLeaderboard(apiUrl: String): Result<List<PlayerStats>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$apiUrl/api/leaderboard")
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.success(getFallbackLeaderboard())
            }

            val responseBody = response.body?.string() ?: return@withContext Result.success(
                getFallbackLeaderboard()
            )

            val leaderboard = parseLeaderboard(responseBody)
            Result.success(leaderboard)
        } catch (e: Exception) {
            Result.success(getFallbackLeaderboard())
        }
    }

    private fun parseLeaderboard(json: String): List<PlayerStats> {
        val players = mutableListOf<PlayerStats>()
        try {
            val jsonObject = JsonParser.parseString(json).asJsonObject
            val leaderboardArray = jsonObject.getAsJsonArray("leaderboard") ?: return players

            for (i in leaderboardArray.indices) {
                val playerObj = leaderboardArray[i].asJsonObject
                val username = playerObj.get("username")?.asString ?: "Unknown"
                val arenaPoints = playerObj.get("arenaPoints")?.asInt ?: 0
                val kills = playerObj.get("kills")?.asInt ?: 0
                val wins = playerObj.get("wins")?.asInt ?: 0

                players.add(
                    PlayerStats(
                        rank = i + 1,
                        username = username,
                        arenaPoints = arenaPoints,
                        kills = kills,
                        wins = wins
                    )
                )
            }
        } catch (e: Exception) {
            return getFallbackLeaderboard()
        }
        return players
    }

    private fun getFallbackLeaderboard(): List<PlayerStats> {
        return listOf(
            PlayerStats(1, "Ninja", 12500, 45000, 320),
            PlayerStats(2, "Tfue", 11800, 42000, 295),
            PlayerStats(3, "Bugha", 11200, 38000, 275),
            PlayerStats(4, "Clix", 10500, 35000, 250),
            PlayerStats(5, "Symfuhny", 9800, 32000, 230),
            PlayerStats(6, "NICKMERCS", 9200, 30000, 210),
            PlayerStats(7, "TimTheTatman", 8500, 28000, 190),
            PlayerStats(8, "DrLupo", 7800, 25000, 175),
            PlayerStats(9, "Valkyrae", 7200, 22000, 160),
            PlayerStats(10, "CouRageJD", 6500, 20000, 145)
        )
    }
}
