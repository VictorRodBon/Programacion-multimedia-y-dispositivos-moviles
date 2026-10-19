package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Creamos el controlador (el mando a distancia)
            val navController = rememberNavController()

            // 2. Definimos el NavHost (el contenedor que intercambia pantallas)
            NavHost(
                navController = navController,
                startDestination = "pantalla_a" // Ruta inicial
            ) {
                // 3. Registramos las rutas
                composable("pantalla_a") {
                    PantallaA(navController) // Llama al código del archivo PantallaA.kt
                }

                composable("pantalla_b") {
                    PantallaB(navController) // Llama al código del archivo PantallaB.kt
                }
            }
        }
    }
}