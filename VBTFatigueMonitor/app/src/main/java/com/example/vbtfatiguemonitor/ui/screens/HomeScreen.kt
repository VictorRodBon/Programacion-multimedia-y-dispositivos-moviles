package com.example.vbtfatiguemonitor.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vbtfatiguemonitor.data.local.SerieWithRepeticiones
import com.example.vbtfatiguemonitor.ui.viewmodel.SerieViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTraining: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: SerieViewModel = viewModel()
) {
    val series by viewModel.history.collectAsStateWithLifecycle()
    var serieToDelete by remember { mutableStateOf<SerieWithRepeticiones?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("VBT Fatigue Monitor - Historial") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToTraining) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Entrenamiento")
            }
        }
    ) { innerPadding ->
        if (series.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Todavía no hay series guardadas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pulsa el botón + para iniciar tu primer entrenamiento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = series, key = { it.serie.id }) { item ->
                    SerieCard(
                        item = item,
                        onClick = { onNavigateToDetail(item.serie.id) },
                        onDeleteClick = { serieToDelete = item }
                    )
                }
            }
        }
    }

    serieToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { serieToDelete = null },
            title = { Text("Eliminar serie") },
            text = {
                Text("¿Seguro que quieres eliminar la serie de \"${target.serie.ejercicio}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSerie(target.serie)
                        serieToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { serieToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SerieCard(
    item: SerieWithRepeticiones,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val serie = item.serie
    val fatigue = serie.fatigaAlcanzadaPct
    val fatigueColor = if (fatigue > 18f) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = serie.ejercicio,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fecha: ${serie.fecha}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Repeticiones: ${serie.repeticionesTotales}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = String.format(Locale.getDefault(), "Pérdida de velocidad: %.1f%%", fatigue),
                    style = MaterialTheme.typography.bodyMedium,
                    color = fatigueColor,
                    fontWeight = if (fatigue > 18f) FontWeight.Bold else FontWeight.Normal
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar serie de ${serie.ejercicio}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
