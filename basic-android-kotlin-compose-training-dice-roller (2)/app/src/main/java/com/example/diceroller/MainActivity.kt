package com.example.diceroller

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme

// Etiqueta global para filtrar en Logcat
private const val TAG = "DICE_DEBUG"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: La app se está creando")
        setContent {
            DiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp()
                }
            }
        }
    }

    // --- CICLO DE VIDA PARA DEBUG ---
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Has salido o minimizado la app")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Has vuelto a la app")
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    // Variable de estado que vamos a espiar con el Debugger
    var result by remember { mutableStateOf(1) }

    // Experimento 1: Error forzado.
    // Si 'result' fuera 7, imageResource valdría 0 y la app haría CRASH.
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> 0
    }

    // Lógica dinámica: El botón cambia a AZUL si sale un 6
    val botonColor = if (result == 6) Color.Blue else Color.Red

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString()
        )

        Text(
            text = "Número: $result",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                // PON UN BREAKPOINT AQUÍ (Punto rojo en la línea de abajo)
                result = (1..7).random()

                // Experimento 2: Log dinámico
                Log.d(TAG, "Valor generado: $result")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = botonColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text(text = stringResource(R.string.roll), fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    )
}