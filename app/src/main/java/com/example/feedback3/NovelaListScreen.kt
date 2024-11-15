package com.example.feedback3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NovelaListScreen(
    viewModel: NovelaViewModel,
    onNovelaClick: (Novela) -> Unit
) {
    val novelas by viewModel.novelas.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                val nuevaNovela = Novela(
                    titulo = "Nuevo Titulo",
                    autor = "Nuevo Autor",
                    anoPublicacion = 2023,
                    sinopsis = "Nueva Sinopsis"
                )
                viewModel.agregarNovela(nuevaNovela)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Novela")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(novelas) { novela ->
                NovelaItem(
                    novela = novela,
                    onClick = { onNovelaClick(novela) },
                    onEliminarClick = { viewModel.eliminarNovela(novela.id.toString()) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}