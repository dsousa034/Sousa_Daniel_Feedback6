package com.example.feedback3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NovelaDetailScreen(
    novela: Novela,
    onFavoritoClick: () -> Unit,
    onAgregarResena: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = novela.titulo,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Autor: ${novela.autor}")
        Text(text = "Publicado en: ${novela.anoPublicacion}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sinopsis:", fontWeight = FontWeight.Bold)
        Text(text = novela.sinopsis)
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onFavoritoClick) {
                Text(text = if (novela.esFavorita) "Quitar de favoritos" else "Marcar como favorita")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onAgregarResena("¡Gran novela!") }) {
                Text(text = "Añadir reseña")
            }
        }
    }
}