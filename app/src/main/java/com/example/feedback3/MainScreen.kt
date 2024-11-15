package com.example.feedback3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(onAddNovelaClick: () -> Unit, onManageNovelasClick: () -> Unit, onFavoritasClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Gestor de Novelas",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddNovelaClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir Novelas")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onManageNovelasClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Listar Novelas")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onFavoritasClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Novelas Favoritas")
        }
    }
}