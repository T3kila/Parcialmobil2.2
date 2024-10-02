package com.ufg.criptoapplication.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun MainScreen(
    onLocationClick: () -> Unit,
    onMediaFilesClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onTempCameraClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp,
            mainAxisAlignment = FlowMainAxisAlignment.Center
        ) {
            MenuButton("Temporal Cámara", onTempCameraClick, Icons.Default.CameraAlt)
            MenuButton("Temporal Ubicación", onLocationClick, Icons.Default.LocationOn)
            Divider()

            MenuButton("Archivos Multimedia", onMediaFilesClick, Icons.Default.PhotoLibrary)
            MenuButton("Notificaciones", onNotificationsClick, Icons.Default.Notifications)

        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    var isPressed by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFF3700B3) else Color(0xFF6200EE)
    )

    Button(
        onClick = {
            isPressed = !isPressed
            onClick()
        },
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp)
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}