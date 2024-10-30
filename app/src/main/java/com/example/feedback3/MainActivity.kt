package com.example.feedback3

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var internalStorageManager: InternalStorageManager
    private lateinit var externalStorageManager: ExternalStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
        internalStorageManager = InternalStorageManager(this)
        externalStorageManager = ExternalStorageManager(this)

        // Apply the theme based on the preference
        val isDarkMode = preferencesManager.isDarkMode()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Request permissions for external storage
        requestStoragePermissions()

        setContent {
            NovelaApp()
        }
        scheduleJob()
        scheduleAlarm(this) // Schedule the alarm for daily sync
    }

    private fun requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Schedule JobScheduler for background sync
    private fun scheduleJob() {
        val componentName = ComponentName(this, DataSyncJobService::class.java)
        val jobInfo = JobInfo.Builder(1, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // Only Wi-Fi
            .setPeriodic(15 * 60 * 1000) // Every 15 minutes
            .build()

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }
}

fun backupDatabase(context: Context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        return
    }

    val dbFile = context.getDatabasePath("novelas.db")
    val backupFile = File(Environment.getExternalStorageDirectory(), "novelas_backup.db")

    try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            FileChannel.open(dbFile.toPath()).use { source: FileChannel ->
                FileChannel.open(backupFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { destination: FileChannel ->
                    destination.transferFrom(source, 0, source.size())
                }
            }
        } else {
            FileInputStream(dbFile).use { source ->
                FileOutputStream(backupFile).use { destination ->
                    destination.channel.transferFrom(source.channel, 0, source.channel.size())
                }
            }
        }
        Toast.makeText(context, "Backup successful", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Backup failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun restoreDatabase(context: Context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        return
    }

    val dbFile = context.getDatabasePath("novelas.db")
    val backupFile = File(Environment.getExternalStorageDirectory(), "novelas_backup.db")

    try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            FileChannel.open(backupFile.toPath()).use { source: FileChannel ->
                FileChannel.open(dbFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { destination: FileChannel ->
                    destination.transferFrom(source, 0, source.size())
                }
            }
        } else {
            FileInputStream(backupFile).use { source ->
                FileOutputStream(dbFile).use { destination ->
                    destination.channel.transferFrom(source.channel, 0, source.channel.size())
                }
            }
        }
        Toast.makeText(context, "Restore successful", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Restore failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelaApp() {
    val navController = rememberNavController()
    val viewModel: NovelaViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Gestión de Novelas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavigationHost(navController = navController, viewModel = viewModel)
            }
        }
    )
}

@Composable
fun NavigationHost(navController: NavHostController, viewModel: NovelaViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            PantallaPrincipal(
                novelas = viewModel.novelas,
                onAgregarClick = { navController.navigate("agregar") },
                onEliminarClick = { novela: Novela -> viewModel.eliminarNovela(novela) },
                onVerDetallesClick = { novela: Novela ->
                    navController.navigate("detalles/${novela.titulo}")
                },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            val context = androidx.compose.ui.platform.LocalContext.current
            SettingsScreen(
                context = context,
                onBackup = { backupDatabase(context) },
                onRestore = { restoreDatabase(context) },
                onBack = { navController.popBackStack() }
            )
        }
        composable("agregar") {
            AgregarNovela { novela: Novela ->
                viewModel.agregarNovela(novela)
                navController.popBackStack()
            }
        }
        composable("detalles/{titulo}") { backStackEntry ->
            val titulo = backStackEntry.arguments?.getString("titulo") ?: "Título no disponible"
            val novela = viewModel.novelas.firstOrNull { it.titulo == titulo }
            novela?.let {
                DetallesNovela(
                    novela = it,
                    onMarcarFavorita = { novela: Novela ->
                        viewModel.marcarFavorita(novela)
                    },
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}