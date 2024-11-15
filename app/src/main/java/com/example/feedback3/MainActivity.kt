package com.example.feedback3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val viewModel = NovelaViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showRegistrationScreen by remember { mutableStateOf(true) }
            var showAdminScreen by remember { mutableStateOf(false) }
            var showDialog by remember { mutableStateOf(false) }
            var showFavoritasScreen by remember { mutableStateOf(false) }

            if (showRegistrationScreen) {
                RegistrationScreen { nombre, apellido ->
                    viewModel.registrarUsuario(nombre, apellido)
                    showRegistrationScreen = false
                }
            } else {
                when {
                    showAdminScreen -> {
                        AdminNovelasScreen(
                            viewModel = viewModel,
                            onBackClick = { showAdminScreen = false }
                        )
                    }
                    showDialog -> {
                        AddNovelaDialog(
                            onDismiss = { showDialog = false },
                            onSave = { novela ->
                                viewModel.agregarNovela(novela)
                                showDialog = false
                            },
                            onBackClick = { showDialog = false }
                        )
                    }
                    showFavoritasScreen -> {
                        FavoritasScreen(
                            viewModel = viewModel,
                            onBackClick = { showFavoritasScreen = false }
                        )
                    }
                    else -> {
                        MainScreen(
                            onAddNovelaClick = { showDialog = true },
                            onManageNovelasClick = { showAdminScreen = true },
                            onFavoritasClick = { showFavoritasScreen = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddNovelaDialog(onDismiss: () -> Unit, onSave: (Novela) -> Unit, onBackClick: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anoPublicacion by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Añadir Novela") },
        text = {
            Column {
                TextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") }
                )
                TextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") }
                )
                TextField(
                    value = anoPublicacion,
                    onValueChange = { anoPublicacion = it },
                    label = { Text("Año de Publicación") }
                )
                TextField(
                    value = sinopsis,
                    onValueChange = { sinopsis = it },
                    label = { Text("Sinopsis") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text("Volver a la pantalla principal")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val nuevaNovela = Novela(
                    id = System.currentTimeMillis().toInt(),
                    titulo = titulo,
                    autor = autor,
                    anoPublicacion = anoPublicacion.toIntOrNull() ?: 0,
                    sinopsis = sinopsis
                )
                onSave(nuevaNovela)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AdminNovelasScreen(viewModel: NovelaViewModel, onBackClick: () -> Unit) {
    val novelas by viewModel.novelas.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Listar Novelas",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text("Volver a la pantalla principal")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (novelas.isEmpty()) {
            Text(text = "No hay ninguna novela guardada")
        } else {
            LazyColumn {
                items(novelas) { novela ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = novela.titulo, fontWeight = FontWeight.Bold)
                            Text(text = "Autor: ${novela.autor}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Button(onClick = { viewModel.toggleFavorito(novela.id.toString(), novela.esFavorita) }) {
                                    Text(text = if (novela.esFavorita) "Quitar de favoritos" else "Añadir a favoritos")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { viewModel.eliminarNovela(novela.id.toString()) }) {
                                    Text(text = "Eliminar novela")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun FavoritasScreen(viewModel: NovelaViewModel, onBackClick: () -> Unit) {
    val novelas by viewModel.novelas.collectAsState()
    val favoritas = novelas.filter { it.esFavorita }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Novelas Favoritas",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text("Volver a la pantalla principal")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (favoritas.isEmpty()) {
            Text(text = "Aún no se han agregado novelas favoritas")
        } else {
            LazyColumn {
                items(favoritas) { novela ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = novela.titulo, fontWeight = FontWeight.Bold)
                            Text(text = "Autor: ${novela.autor}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Button(onClick = { viewModel.toggleFavorito(novela.id.toString(), novela.esFavorita) }) {
                                    Text(text = if (novela.esFavorita) "Quitar de favoritos" else "Añadir a favoritos")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { viewModel.eliminarNovela(novela.id.toString()) }) {
                                    Text(text = "Eliminar novela")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun RegistrationScreen(onRegister: (String, String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

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
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onRegister(nombre, apellido) }) {
            Text("Registrar")
        }
    }
}