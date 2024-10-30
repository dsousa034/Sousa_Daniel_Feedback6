package com.example.feedback3

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen(
    context: Context,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    onBack: () -> Unit
) {
    val preferencesManager = PreferencesManager(context)
    val isDarkMode = preferencesManager.isDarkMode()

    Column {
        Button(onClick = onBackup) {
            Text("Backup Data")
        }
        Button(onClick = onRestore) {
            Text("Restore Data")
        }
        Button(onClick = {
            preferencesManager.toggleTheme()
            (context as MainActivity).recreate() // Recreate activity to apply theme change
        }) {
            Text(if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode")
        }
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}