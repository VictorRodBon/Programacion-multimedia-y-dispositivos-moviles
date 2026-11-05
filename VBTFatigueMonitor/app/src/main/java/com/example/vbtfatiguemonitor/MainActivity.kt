package com.example.vbtfatiguemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.vbtfatiguemonitor.ui.navigation.AppNavigation
import com.example.vbtfatiguemonitor.ui.theme.VBTFatigueMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño de pantalla completa (Edge-to-Edge) moderno
        enableEdgeToEdge()

        setContent {
            // Aplica el tema personalizado de la aplicación
            VBTFatigueMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Grafo de navegación: Home -> Training -> Detail
                    AppNavigation()
                }
            }
        }
    }
}
