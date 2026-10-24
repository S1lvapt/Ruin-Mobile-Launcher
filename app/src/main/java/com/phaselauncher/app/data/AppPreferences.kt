package com.phaselauncher.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "phase_launcher_prefs")

class AppPreferences(private val context: Context) {

    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val DISCORD_USERNAME = stringPreferencesKey("discord_username")
        val DISCORD_AVATAR_URL = stringPreferencesKey("discord_avatar_url")
        val DISCORD_ID = stringPreferencesKey("discord_id")
        val LIQUID_GLASS_ENABLED = booleanPreferencesKey("liquid_glass_enabled")
        val API_URL = stringPreferencesKey("api_url")
        val LOGIN_URL = stringPreferencesKey("login_url")
        val SELECTED_BUILD = stringPreferencesKey("selected_build")
        val TARGET_PACKAGE = stringPreferencesKey("target_package")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    val discordUsername: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DISCORD_USERNAME] ?: ""
        }

    val discordAvatarUrl: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DISCORD_AVATAR_URL] ?: ""
        }

    val discordId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DISCORD_ID] ?: ""
        }

    val liquidGlassEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LIQUID_GLASS_ENABLED] ?: false
        }

    val apiUrl: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.API_URL] ?: "http://172.24.189.149:3551"
        }

    val loginUrl: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LOGIN_URL] ?: "http://172.24.189.149:8080/login"
        }

    val selectedBuild: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SELECTED_BUILD] ?: "Build 18.40 (Chapter 2 Season 8)"
        }

    val targetPackage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.TARGET_PACKAGE] ?: "com.ruin.fortnite"
        }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = value
        }
    }

    suspend fun setDiscordUsername(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DISCORD_USERNAME] = value
        }
    }

    suspend fun setDiscordAvatarUrl(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DISCORD_AVATAR_URL] = value
        }
    }

    suspend fun setDiscordId(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DISCORD_ID] = value
        }
    }

    suspend fun setLiquidGlassEnabled(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIQUID_GLASS_ENABLED] = value
        }
    }

    suspend fun setApiUrl(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.API_URL] = value
        }
    }

    suspend fun setLoginUrl(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOGIN_URL] = value
        }
    }

    suspend fun setSelectedBuild(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_BUILD] = value
        }
    }

    suspend fun setTargetPackage(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_PACKAGE] = value
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = false
            preferences[PreferencesKeys.DISCORD_USERNAME] = ""
            preferences[PreferencesKeys.DISCORD_AVATAR_URL] = ""
            preferences[PreferencesKeys.DISCORD_ID] = ""
        }
    }
}
