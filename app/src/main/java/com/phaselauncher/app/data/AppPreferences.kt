package com.phaselauncher.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "phase_prefs")

class AppPreferences(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val DISCORD_USERNAME = stringPreferencesKey("discord_username")
        val DISCORD_AVATAR = stringPreferencesKey("discord_avatar")
        val DISCORD_ID = stringPreferencesKey("discord_id")
        val LIQUID_GLASS = booleanPreferencesKey("liquid_glass")
        val API_BASE_URL = stringPreferencesKey("custom_api_url")
        val LOGIN_URL = stringPreferencesKey("login_url")
        val SELECTED_BUILD = stringPreferencesKey("selected_build")
        val TARGET_PACKAGE = stringPreferencesKey("target_package")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val discordUsername: Flow<String> = context.dataStore.data.map { it[DISCORD_USERNAME] ?: "Player" }
    val discordAvatarUrl: Flow<String> = context.dataStore.data.map { it[DISCORD_AVATAR] ?: "https://cdn.discordapp.com/embed/avatars/0.png" }
    val discordId: Flow<String> = context.dataStore.data.map { it[DISCORD_ID] ?: "" }
    val liquidGlassEnabled: Flow<Boolean> = context.dataStore.data.map { it[LIQUID_GLASS] ?: false }
    val apiUrl: Flow<String> = context.dataStore.data.map { it[API_BASE_URL] ?: "http://172.24.189.149:3551" }
    val loginUrl: Flow<String> = context.dataStore.data.map { it[LOGIN_URL] ?: "http://172.24.189.149:8080/login" }
    val selectedBuild: Flow<String> = context.dataStore.data.map { it[SELECTED_BUILD] ?: "Build 18.40 (Chapter 2 Season 8)" }
    val targetPackage: Flow<String> = context.dataStore.data.map { it[TARGET_PACKAGE] ?: "com.ruin.fortnite" }

    suspend fun saveDiscordSession(username: String, avatarUrl: String, id: String) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[DISCORD_USERNAME] = username
            it[DISCORD_AVATAR] = avatarUrl
            it[DISCORD_ID] = id
        }
    }

    suspend fun logout() {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = false
            it[DISCORD_USERNAME] = "Player"
            it[DISCORD_AVATAR] = "https://cdn.discordapp.com/embed/avatars/0.png"
            it[DISCORD_ID] = ""
        }
    }

    suspend fun saveSelectedBuild(build: String) {
        context.dataStore.edit { it[SELECTED_BUILD] = build }
    }

    suspend fun saveApiUrl(url: String) {
        context.dataStore.edit { it[API_BASE_URL] = url }
    }

    suspend fun saveLoginUrl(url: String) {
        context.dataStore.edit { it[LOGIN_URL] = url }
    }

    suspend fun saveTargetPackage(pkg: String) {
        context.dataStore.edit { it[TARGET_PACKAGE] = pkg }
    }

    suspend fun setLiquidGlassEnabled(enabled: Boolean) {
        context.dataStore.edit { it[LIQUID_GLASS] = enabled }
    }
}
