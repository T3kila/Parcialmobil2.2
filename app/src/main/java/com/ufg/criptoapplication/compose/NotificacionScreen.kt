package com.ufg.criptoapplication.compose

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat

@Composable
internal fun NotificacionScreen(onMainClickTo: () -> Unit) {
    var notificationPermissionGranted by remember { mutableStateOf(false) }


    // Contexto y permisos
    val context = LocalContext.current
    val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

    // Lanzadores para solicitar permisos
    val notificationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        notificationPermissionGranted = isGranted
    }

    // Comprobar permisos al cargar la pantalla
    LaunchedEffect(Unit) {
        notificationPermissionGranted = ContextCompat.checkSelfPermission(context, notificationPermission) == PackageManager.PERMISSION_GRANTED
    }

    // Función para mostrar notificación de prueba
    fun sendTestNotification() {
        if (notificationPermissionGranted) {
            val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            val channelId = "test_channel"
            val notificationId = 1

            // Crear un canal de notificación (requerido desde Android O)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Canal de Pruebas", NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Canal para notificaciones de prueba"
                }
                notificationManager?.createNotificationChannel(channel)
            }

            // Crear notificación
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Notificación de prueba")
                .setContentText("Esta es una notificación de prueba.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            // Enviar notificación
            notificationManager?.notify(notificationId, notification)
        } else {
            notificationPermissionLauncher.launch(notificationPermission)
        }
    }

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
        PermissionStatus("Permiso Notificación", notificationPermissionGranted)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para enviar notificación de prueba
        Button(
            onClick = { sendTestNotification() },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF03DAC5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Enviar Notificación de Prueba", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        // Justificación del permiso
        if (!notificationPermissionGranted) {
            Text(
                text = "Esta aplicación necesita permiso para enviar notificaciones para informarte sobre actualizaciones y eventos importantes. Por favor, concede el permiso para una mejor experiencia.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
