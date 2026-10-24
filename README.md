# Phase Launcher

A production-ready native Android launcher inspired by Rewind Launcher for Fortnite, built entirely in Kotlin and Jetpack Compose (Material 3).

## Features

### Core Architecture
- **Package Name**: `com.phaselauncher.app`
- **Language**: Kotlin + Jetpack Compose (Material 3)
- **Target SDK**: 34 (Android 14)
- **Min SDK**: 26 (Android 8.0)

### Visual Theme
- **Season Theme**: Chapter 2 Season 8 (CUBED / The Sideways) - Build 18.40
- **Color Palette**:
  - Deep obsidian black (#080B11)
  - Neon cyan (#00E5FF)
  - Electric purple (#7928CA)
  - Electric blue (#0066FF)
  - Discord Blurple (#5865F2)
  - Error Red (#FF4D4D)
  - V-Bucks Gold (#FFD700)

### Authentication Flow
- Discord login via WebView
- Persistent session using DataStore
- Automatic login on app launch if previously authenticated
- Manual logout in Settings screen

### Screens
1. **Welcome Screen**: Discord login with WebView integration
2. **Home Screen**: Chapter 2 Season 8 hero banner, build selector, Play button
3. **Shop Screen**: Live Item Shop with V-Bucks balance and catalog grid
4. **Leaderboard Screen**: Searchable player rankings with stats
5. **Settings Screen**: User profile, Liquid Glass Mode toggle, server configuration

### Navigation
- Floating capsule navigation bar (Island style dock)
- Smooth 60/120fps screen transitions
- Liquid Glass mode option (translucent frosted glass effect)
- Discord avatar circle in navigation bar

### API Integration
- **Catalog Endpoint**: `http://172.24.189.149:3551/fortnite/api/storefront/v2/catalog`
- **V-Bucks Balance**: POST to `.../QueryProfile?profileId=common_core`
- **Leaderboard**: GET to `.../api/leaderboard` with fallback dataset
- **Discord Login**: `http://172.24.189.149:8080/login`

### Game Launch
- Target package: `com.ruin.fortnite`
- Package visibility query for Android 11+
- Toast error message if game not installed

## Project Structure

```
app/
├── src/main/
│   ├── java/com/phaselauncher/app/
│   │   ├── MainActivity.kt
│   │   ├── data/
│   │   │   ├── AppPreferences.kt
│   │   │   └── ReloadApiClient.kt
│   │   ├── ui/
│   │   │   ├── components/
│   │   │   │   └── GamingComponents.kt
│   │   │   ├── navigation/
│   │   │   │   └── AppNavigation.kt
│   │   │   ├── screens/
│   │   │   │   ├── WelcomeScreen.kt
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── ShopScreen.kt
│   │   │   │   ├── LeaderboardScreen.kt
│   │   │   │   └── SettingsScreen.kt
│   │   │   └── theme/
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   ├── res/
│   │   ├── drawable/
│   │   │   └── ic_vbucks.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   └── themes.xml
│   │   └── mipmap-anydpi-v26/
│   │       ├── ic_launcher.xml
│   │       └── ic_launcher_round.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## Dependencies

- **Compose BOM**: 2024.06.00
- **Material 3**: Stable
- **Material Icons Extended**
- **Navigation Compose**: 2.7.7
- **Coil Compose**: 2.6.0 (Image loading)
- **DataStore Preferences**: 1.1.1 (Persistent storage)
- **OkHttp**: 4.12.0 (HTTP client)
- **Gson**: 2.10.1 (JSON parsing)

## Building the Project

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Build Steps
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Build the project: Build > Make Project
4. Run on emulator or device: Run > Run 'app'

## Permissions

- `INTERNET`: Required for API calls and Discord login
- `ACCESS_NETWORK_STATE`: Network connectivity checks
- `usesCleartextTraffic`: Enabled for local HTTP servers (ports 8080, 3551)

## Configuration

All server endpoints and settings are configurable via the Settings screen:
- API URL (default: `http://172.24.189.149:3551`)
- Login URL (default: `http://172.24.189.149:8080/login`)
- Target Package (default: `com.ruin.fortnite`)
- Liquid Glass Mode toggle

## Notes

- The FAQ screen has been permanently removed as requested
- All screens are fully implemented with no TODOs or placeholders
- Project is ready to compile immediately
- Fallback datasets are provided for API failures
# Ruin-Mobile-Launcher
