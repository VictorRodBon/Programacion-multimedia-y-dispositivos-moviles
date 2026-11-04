package com.example.pruebanavegacion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun PantallaA(navControler: NavHostController) {
    Column() {
        Text("Estamos en la pantalla A");
        Button(onClick = {
            navControler.navigate(pantalla_b);
        }){
            Text("Ir a pantalla B");
        }
    }
}