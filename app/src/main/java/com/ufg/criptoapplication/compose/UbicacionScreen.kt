import android.Manifest
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@Composable
internal fun UbicacionScreen(onMainClickTo: () -> Unit) {

    var coordinates by remember { mutableStateOf<String?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    // For Fused Location
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        locationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        locationPermissionGranted = ContextCompat.checkSelfPermission(context, locationPermission) == PackageManager.PERMISSION_GRANTED
    }

    fun getLocation() {
        if (locationPermissionGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                coordinates = location?.let { "Lat: ${it.latitude}, Lon: ${it.longitude}" } ?: "No se pudo obtener la ubicación"
            }
        } else {
            locationPermissionLauncher.launch(locationPermission)
        }
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
        PermissionStatus("Permiso GPS", locationPermissionGranted)

        Button(
            onClick = { getLocation() },
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF03DAC5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Obtener Ubicación GPS", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        OutlinedTextField(
            value = coordinates ?: "Coordenadas no disponibles",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label = { Text("Coordenadas") },
            shape = RoundedCornerShape(12.dp)
        )
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
