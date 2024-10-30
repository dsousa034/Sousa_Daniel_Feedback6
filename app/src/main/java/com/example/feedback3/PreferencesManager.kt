package com.example.feedback3

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun setTheme(isDarkMode: Boolean) {
        preferences.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun isDarkMode(): Boolean {
        return preferences.getBoolean("dark_mode", false)
    }

    fun toggleTheme() {
        val isDarkMode = isDarkMode()
        setTheme(!isDarkMode)
    }
}