package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun PantallaA(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Estás en la Pantalla A")
        Button(onClick = {
            // Ordenamos navegar a la ruta "pantalla_b"
            navController.navigate("pantalla_b")
        }) {
            Text("Ir a Pantalla B")
        }
    }
}