package com.example.vbtfatiguemonitor.ui.screens

import android.content.Context
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vbtfatiguemonitor.data.local.RepeticionEntity
import com.example.vbtfatiguemonitor.data.local.SerieEntity
import com.example.vbtfatiguemonitor.sensor.AccelerationTracker
import com.example.vbtfatiguemonitor.ui.viewmodel.SerieViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FATIGUE_THRESHOLD_PCT = 18f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    onFinishTraining: () -> Unit,
    onBack: () -> Unit,
    viewModel: SerieViewModel = viewModel()
) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val toneGenerator = remember {
        runCatching { ToneGenerator(AudioManager.STREAM_MUSIC, 80) }.getOrNull()
    }

    var exerciseName by rememberSaveable { mutableStateOf("Sentadilla") }
    var isTraining by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }
    var reps by remember { mutableStateOf<List<RepeticionEntity>>(emptyList()) }
    var currentVelocity by remember { mutableStateOf(0f) }
    var referenceVelocity by remember { mutableStateOf(0f) }
    var worstDrop by remember { mutableStateOf(0f) }
    var fatigueAlert by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val tracker = remember {
        AccelerationTracker(sensorManager) { velocity ->
            val repNumber = reps.size + 1
            val reference = if (reps.isEmpty()) velocity else reps.first().velocity
            val dropPct = if (reps.isEmpty() || reference <= 0f) {
                0f
            } else {
                ((reference - velocity) / reference) * 100f
            }
            val fatigued = reps.isNotEmpty() && dropPct > FATIGUE_THRESHOLD_PCT

            if (fatigued && !fatigueAlert) {
                // Aviso sonoro único al cruzar el umbral de fatiga.
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 300)
            }

            reps = reps + RepeticionEntity(
                serieId = 0L,
                repNumber = repNumber,
                velocity = velocity,
                isFatigueReached = fatigued
            )
            currentVelocity = velocity
            referenceVelocity = reference
            if (reps.size > 1) {
                worstDrop = maxOf(worstDrop, dropPct)
            }
            if (fatigued) {
                fatigueAlert = true
            }
        }
    }

    // El sensor se registra al empezar y se desregistra SIEMPRE al parar o salir.
    DisposableEffect(isTraining) {
        if (isTraining) {
            val sensorAvailable = tracker.start()
            if (!sensorAvailable) {
                message = "Este dispositivo no dispone de acelerómetro."
                isTraining = false
            }
        } else {
            tracker.stop()
        }
        onDispose { tracker.stop() }
    }

    DisposableEffect(Unit) {
        onDispose { toneGenerator?.release() }
    }

    val startSeries = {
        reps = emptyList()
        currentVelocity = 0f
        referenceVelocity = 0f
        worstDrop = 0f
        fatigueAlert = false
        message = null
        isTraining = true
    }

    val finishAndSave = {
        if (!saving) {
            if (reps.isEmpty()) {
                message = "No se detectaron repeticiones. La serie no se ha guardado."
                isTraining = false
            } else {
                saving = true
                isTraining = false
                val serie = SerieEntity(
                    ejercicio = exerciseName.trim().ifBlank { "Ejercicio" },
                    fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()),
                    repeticionesTotales = reps.size,
                    fatigaAlcanzadaPct = worstDrop
                )
                viewModel.saveSerieWithRepeticiones(
                    serie = serie,
                    repeticiones = reps,
                    onSaved = {
                        saving = false
                        onFinishTraining()
                    },
                    onError = {
                        saving = false
                        message = "No se pudo guardar la serie. Inténtalo de nuevo."
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrenamiento VBT en Vivo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = exerciseName,
                onValueChange = { exerciseName = it },
                label = { Text("Nombre del ejercicio") },
                singleLine = true,
                enabled = !isTraining && !saving,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val feedbackColor = if (fatigueAlert) {
                MaterialTheme.colorScheme.error
            } else {
                Color(0xFF2E7D32)
            }

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        color = feedbackColor.copy(alpha = 0.20f),
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Velocidad actual",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "%.2f m/s", currentVelocity),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isTraining) "Serie en curso" else "Serie detenida",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn(label = "Repeticiones", value = reps.size.toString())
                StatColumn(
                    label = "Referencia",
                    value = String.format(Locale.getDefault(), "%.2f m/s", referenceVelocity)
                )
                StatColumn(
                    label = "Peor caída",
                    value = String.format(Locale.getDefault(), "%.1f%%", worstDrop),
                    valueColor = if (worstDrop > FATIGUE_THRESHOLD_PCT) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (fatigueAlert) {
                Text(
                    text = "⚠️ Fatiga detectada: caída de velocidad superior al 18%.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Velocidad estable. La primera repetición es la referencia (100%).",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        saving -> Unit
                        isTraining -> finishAndSave()
                        else -> startSeries()
                    }
                },
                enabled = !saving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTraining) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = when {
                        saving -> "Guardando..."
                        isTraining -> "Finalizar y Guardar Serie"
                        else -> "Iniciar Serie"
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatColumn(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
