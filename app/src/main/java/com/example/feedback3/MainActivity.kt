package com.example.feedback3

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
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
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

class MainActivity : ComponentActivity() {
    private val viewModel = NovelaViewModel()
    private lateinit var bitmap: Bitmap
    private lateinit var imageView: ImageView

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
    override fun onDestroy() {
        super.onDestroy()
        imageView.setImageBitmap(null)
        bitmap.recycle()
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
fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun compressData(data: ByteArray): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    GZIPOutputStream(byteArrayOutputStream).use { gzipOutputStream ->
        gzipOutputStream.write(data)
    }
    return byteArrayOutputStream.toByteArray()
}

fun scheduleJob(context: Context) {
    val componentName = ComponentName(context, MyJobService::class.java)
    val jobInfo = JobInfo.Builder(123, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPeriodic(15 * 60 * 1000) // 15 minutos
        .build()

    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.schedule(jobInfo)
}