package com.example.feedback3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaPrincipal(
    novelas: List<Novela>,
    onAgregarClick: () -> Unit,
    onEliminarClick: (Novela) -> Unit,
    onVerDetallesClick: (Novela) -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Novela")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gestión de Novelas",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Configuración")
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(novelas) { novela ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onVerDetallesClick(novela) }
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = novela.titulo, style = MaterialTheme.typography.titleLarge)
                                Text(text = "Autor: ${novela.autor}")
                            }
                            if (novela.esFavorita) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Favorita",
                                    tint = Color.Yellow
                                )
                            }
                            IconButton(onClick = { onEliminarClick(novela) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar Novela")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaPrincipal() {
    PantallaPrincipal(
        novelas = listOf(
            Novela("Título 1", "Autor 1", 2021, "Sinopsis 1", esFavorita = true),
            Novela("Título 2", "Autor 2", 2022, "Sinopsis 2", esFavorita = false)
        ),
        onAgregarClick = {},
        onEliminarClick = {},
        onVerDetallesClick = {},
        onSettingsClick = {}
    )
}