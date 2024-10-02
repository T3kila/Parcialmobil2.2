package com.ufg.criptoapplication.compose


import UbicacionScreen
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import androidx.compose.material.icons.filled.PieChart
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable


//Interface
interface MyDestination {
    val icon: ImageVector
    val route: String
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    //Main
    composable(Main.route) {

        MainScreen(
            onLocationClick = {
                Log.d("mainGraph", "MainScreen onLocationClick")
                navController.navigateSingleTopTo(Ubicacion.route)
            },
            onMediaFilesClick = {
                Log.d("mainGraph", "MainScreen onMediaFilesClick")
                navController.navigateSingleTopTo(Galeria.route)
            },
            onNotificationsClick = {
                Log.d("mainGraph", "MainScreen onNotificationsClick")
                navController.navigateSingleTopTo(Notificaciones.route)
            },
            onTempCameraClick = {
                Log.d("mainGraph", "MainScreen onTempCameraClick")
                navController.navigateSingleTopTo(CamaraTemp.route)
            }
        )
    }

    //GPS
    composable(Ubicacion.route) {
        UbicacionScreen(onMainClickTo = {
            Log.d("mainGraph", "MainScreen onLocationClick")
            navController.navigateSingleTopTo(Main.route)
        }
        )
    }

    //Notificaciones
    composable(Notificaciones.route) {
        NotificacionScreen(onMainClickTo = {
            Log.d("mainGraph", "MainScreen onLocationClick")
            navController.navigateSingleTopTo(Main.route)
        }
        )
    }

    //Galeria
    composable(Galeria.route) {
        GaleriaScreen(onMainClickTo = {
            Log.d("mainGraph", "MainScreen onLocationClick")
            navController.navigateSingleTopTo(Main.route)
        }
        )
    }

    //Camara Temp
    composable(CamaraTemp.route) {
        CamaraTempScreen(onMainClickTo = {
            Log.d("mainGraph", "MainScreen onLocationClick")
            navController.navigateSingleTopTo(Main.route)
        }
        )
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }


object Main : MyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "Main"
}

object Ubicacion : MyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "Ubicacion"
}

object Galeria : MyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "Galeria"
}

object Notificaciones : MyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "Notificaciones"
}

object CamaraTemp : MyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "CamaraTemp"
}

object UbicacionTemp : MyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "UbicacionTemp"
}
