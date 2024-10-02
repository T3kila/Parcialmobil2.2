package com.ufg.criptoapplication.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.ufg.criptoapplication.R
import java.io.File
import java.util.concurrent.Executors


@Composable
internal fun CamaraTempScreen(onMainClickTo: () -> Unit) {

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var lastImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    var shouldShowCamera by remember { mutableStateOf(false) }
    var shouldShowPhoto by remember { mutableStateOf(false) }

    // Contexto y permisos
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

    // Lanzadores para solicitar permisos
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        cameraPermissionGranted = isGranted
    }

    // Comprobar permisos al cargar la pantalla
    LaunchedEffect(Unit) {
        cameraPermissionGranted = ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
    }

    // Directorio de salida para la cámara
    val outputDirectory = getOutputDirectory(context)
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Volver al menú principal
        Button(
            onClick = { onMainClickTo() },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Volver al Menú Principal", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estado de permisos
        Text(text = "Estado de Permisos", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
        PermissionStatus("Permiso Cámara", cameraPermissionGranted)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para obtener ubicación GPS
        Button(
            onClick = {
                if (cameraPermissionGranted) {
                    shouldShowCamera = true
                } else {
                    cameraPermissionLauncher.launch(cameraPermission)
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF03DAC5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Tomar Foto Camara (Acceso TEMPORAL)", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        // Mostrar cámara si debe mostrarse
        if (shouldShowCamera) {
            CameraView(
                outputDirectory = outputDirectory,
                executor = cameraExecutor,
                onImageCaptured = { uri ->
                    cameraImageUri = uri
                    lastImageUri = uri
                    shouldShowCamera = false
                    shouldShowPhoto = true
                },
                onError = { /* Manejar error */ }
            )
        }

        // Mostrar la imagen capturada
        if (shouldShowPhoto && lastImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(lastImageUri),
                contentDescription = "My Image"
            )
        }

        // Justificación del permiso
        if (!cameraPermissionGranted) {
            Text(
                text = "La aplicación necesita acceso a tu cámara para permitirte capturar fotos directamente. Esto es esencial para que puedas interactuar con contenido multimedia dentro de la app y aprovechar al máximo sus funcionalidades visuales.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PermissionStatus(permissionName: String, isGranted: Boolean) {
    val textColor = if (isGranted) Color.Green else Color.Red
    val backgroundColor = if (isGranted) Color(0xFF1E1E1E) else Color(0xFFFFE0E0)
    val text = if (isGranted) "Concedido" else "Denegado"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$permissionName: $text",
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun getOutputDirectory(context: Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
}