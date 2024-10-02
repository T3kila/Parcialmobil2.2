package com.ufg.criptoapplication.compose

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter


@Composable
internal fun GaleriaScreen(onMainClickTo: () -> Unit) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var galleryPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Define the launcher for gallery access
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }


    // Request permission for gallery access
    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        galleryPermissionGranted = isGranted
        if (isGranted) {
            openGallery() // Open gallery if permission is granted
        }
    }

    // Check if permission is granted initially
    LaunchedEffect(Unit) {
        galleryPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onMainClickTo() },
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Volver al Menú Principal", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(text = "Estado de Permisos", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
        PermissionStatus("Permiso Galería", galleryPermissionGranted)

        Button(
            onClick = {
                if (galleryPermissionGranted) {
                    openGallery()
                } else {
                    galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            },
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF03DAC5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Abrir Galería", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        // Justificación del permiso
        if (!galleryPermissionGranted) {
            Text(
                text = "La aplicación necesita acceso a tu galería para permitirte seleccionar y compartir imágenes. Esto es esencial para que puedas interactuar con el contenido visual y aprovechar al máximo las funcionalidades de la aplicación que requieren imágenes.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.body2
            )
        }

        imageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Text(
                text = "No hay imagen seleccionada",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body1
            )
        }
    }
}
