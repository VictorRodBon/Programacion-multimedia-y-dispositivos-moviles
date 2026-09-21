package org.educa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.educa.ui.theme.AppContadorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContadorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Changed to render MiPantalla inside Scaffold bounds
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MiPantalla()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MiPantallaPreview() {
    AppContadorTheme {
        MiPantalla()
    }
}

@Composable
fun MiPantalla() {
    // Estado del contador
    var contador by remember { mutableIntStateOf(0) }

    // Estado del texto del botón
    var textoBoton by remember { mutableStateOf("Incrementa contador") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto que cambia el label del botón
        Text(
            text = "Pulsa aquí",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    textoBoton = "Contador: $contador"
                }
        )

        // Botón que incrementa el contador
        val colorBoton = if(contador%2==0)Color.Blue else Color.Red
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
            onClick = {
                contador += 1
                textoBoton = "Contador: $contador"
            }
        ) {
            Text(textoBoton)
        }
    }
}