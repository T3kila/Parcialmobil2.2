package com.ufg.criptoapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity

//imports
import NavComposeTheme
import android.util.Log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Trae todos los Compose
import com.ufg.criptoapplication.compose.*
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        setContent {
            NavComposeTheme {

                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    Scaffold(

                    ) {
                        NavHost(
                            navController = navController,
                            startDestination= Main.route,
                            modifier = Modifier.padding(it),

                            ){
                            mainGraph(navController)

                        }

                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Lógica que confirme que la app fue detenida o minimizada
        Log.d("MainActivity", "onStop called")

    }

}