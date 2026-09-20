package com.ssfa.sudoku.minimax.ui.theme

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class AppThemeMode { LIGHT, DARK, SYSTEM }

@Composable
fun rememberThemeMode(prefs: SharedPreferences): State<AppThemeMode> = produceState(AppThemeMode.SYSTEM) {
    val saved = prefs.getString("theme_mode", AppThemeMode.SYSTEM.name)
    value = try { AppThemeMode.valueOf(saved!!) } catch (e: Exception) { AppThemeMode.SYSTEM }

    observeThemeChanges(prefs).collect { mode ->
        value = mode
    }
}

private fun observeThemeChanges(prefs: SharedPreferences): Flow<AppThemeMode> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "theme_mode") {
            val mode = try { AppThemeMode.valueOf(prefs.getString(key, "SYSTEM")!!) } catch (e: Exception) { AppThemeMode.SYSTEM }
            trySend(mode)
        }
    }
    prefs.registerOnSharedPreferenceChangeListener(listener)
    awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
}

@Composable
fun isDarkTheme(mode: AppThemeMode): Boolean = when (mode) {
    AppThemeMode.LIGHT -> false
    AppThemeMode.DARK -> true
    AppThemeMode.SYSTEM -> isSystemInDarkTheme()
}