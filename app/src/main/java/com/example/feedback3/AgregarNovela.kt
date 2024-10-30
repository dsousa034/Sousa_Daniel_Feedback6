@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.feedback3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedback3.ui.theme.BackgroundColor
import com.example.feedback3.ui.theme.PrimaryColor
import com.example.feedback3.ui.theme.SurfaceColor
import com.example.feedback3.ui.theme.PrimaryLightColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarNovela(onAgregarNovela: (Novela) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        Text(
            text = "Agregar Novela",
            style = MaterialTheme.typography.headlineSmall,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = SurfaceColor,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = PrimaryLightColor
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = autor,
            onValueChange = { autor = it },
            label = { Text("Autor") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = SurfaceColor,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = PrimaryLightColor
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text("Año de Publicación") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = SurfaceColor,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = PrimaryLightColor
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = sinopsis,
            onValueChange = { sinopsis = it },
            label = { Text("Sinopsis") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = SurfaceColor,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = PrimaryLightColor
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (titulo.isNotEmpty() && autor.isNotEmpty() && anio.isNotEmpty() && sinopsis.isNotEmpty()) {
                    onAgregarNovela(Novela(titulo, autor, anio.toInt(), sinopsis))
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Agregar Novela", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAgregarNovela() {
    AgregarNovela(onAgregarNovela = {})
}