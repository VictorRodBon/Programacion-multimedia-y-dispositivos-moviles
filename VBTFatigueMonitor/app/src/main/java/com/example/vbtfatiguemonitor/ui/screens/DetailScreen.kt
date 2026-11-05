package com.example.vbtfatiguemonitor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vbtfatiguemonitor.data.local.RepeticionEntity
import com.example.vbtfatiguemonitor.data.local.SerieWithRepeticiones
import com.example.vbtfatiguemonitor.ui.viewmodel.SerieViewModel
import kotlinx.coroutines.flow.map
import java.util.Locale

private sealed interface DetailUiState {
    data object Loading : DetailUiState
    data object NotFound : DetailUiState
    data class Found(val item: SerieWithRepeticiones) : DetailUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    serieId: Long,
    onBack: () -> Unit,
    viewModel: SerieViewModel = viewModel()
) {
    val uiState by remember(serieId) {
        viewModel.getSerie(serieId).map { item ->
            if (item == null) DetailUiState.NotFound else DetailUiState.Found(item)
        }
    }.collectAsStateWithLifecycle(initialValue = DetailUiState.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Serie #$serieId") },
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
        when (val state = uiState) {
            DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            DetailUiState.NotFound -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se ha encontrado la serie solicitada.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is DetailUiState.Found -> {
                DetailContent(
                    item = state.item,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun DetailContent(
    item: SerieWithRepeticiones,
    modifier: Modifier = Modifier
) {
    val serie = item.serie
    val reps = item.repeticiones.sortedBy { it.repNumber }
    val referenceVelocity = reps.firstOrNull()?.velocity ?: 0f
    val fatigueColor = if (serie.fatigaAlcanzadaPct > 18f) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = serie.ejercicio,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha: ${serie.fecha}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Repeticiones totales: ${serie.repeticionesTotales}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = String.format(
                            Locale.getDefault(),
                            "Pérdida de velocidad: %.1f%%",
                            serie.fatigaAlcanzadaPct
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = fatigueColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (reps.isEmpty()) {
            item {
                Text(
                    text = "Esta serie se guardó sin repeticiones registradas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            items(items = reps, key = { it.id }) { rep ->
                RepeticionRow(
                    rep = rep,
                    referenceVelocity = referenceVelocity
                )
            }
        }
    }
}

@Composable
private fun RepeticionRow(
    rep: RepeticionEntity,
    referenceVelocity: Float
) {
    val dropPct = if (referenceVelocity > 0f) {
        ((referenceVelocity - rep.velocity) / referenceVelocity) * 100f
    } else {
        0f
    }
    val indicatorColor = if (rep.isFatigueReached) {
        MaterialTheme.colorScheme.error
    } else {
        Color(0xFF2E7D32)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Repetición ${rep.repNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "Velocidad: %.2f m/s  ·  Caída: %.1f%%",
                        rep.velocity,
                        dropPct
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = indicatorColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (rep.isFatigueReached) "Fatiga" else "OK",
                    color = indicatorColor,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
