package com.example.ejemploscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploScaffoldCompleto()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EjemploScaffoldCompleto() {
    Scaffold(
        // 1. LA BARRA SUPERIOR (El Techo)
        topBar = {
            TopAppBar(
                title = { Text("Ejemplo Scaffold") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        },

        // 2. EL BOTÓN FLOTANTE (La Acción Principal)
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Acción */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
            }
        },

        // 3. LA BARRA INFERIOR (El Menú)
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Inicio  |  Buscar  |  Biblioteca"
                )
            }
        }ç
    ) { innerPadding ->
        // 4. EL CONTENIDO PRINCIPAL (El salón de la casa)
        // Usamos el padding que nos da el Scaffold para que nada se solape
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Estamos dentro del área de contenido")
        }
    }
}
