package com.example.vbtfatiguemonitor.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbtfatiguemonitor.data.local.AppDatabase
import com.example.vbtfatiguemonitor.data.local.RepeticionEntity
import com.example.vbtfatiguemonitor.data.local.SerieEntity
import com.example.vbtfatiguemonitor.data.local.SerieWithRepeticiones
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SerieViewModel(application: Application) : AndroidViewModel(application) {

    private val serieDao = AppDatabase.getDatabase(application).serieDao()

    /** Historial completo, actualizado automáticamente por Room. */
    val history: StateFlow<List<SerieWithRepeticiones>> =
        serieDao.getAllSeriesWithRepeticiones()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /** Observa una serie concreta (para la pantalla de detalle). */
    fun getSerie(serieId: Long): Flow<SerieWithRepeticiones?> =
        serieDao.getSerieById(serieId)

    /**
     * Guarda una serie con sus repeticiones. [onSaved] se ejecuta en el hilo
     * principal una vez que la transacción de Room ha terminado, de modo que
     * sea seguro navegar hacia atrás desde la pantalla de entrenamiento.
     */
    fun saveSerieWithRepeticiones(
        serie: SerieEntity,
        repeticiones: List<RepeticionEntity>,
        onSaved: (Long) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                serieDao.insertSerieWithRepeticiones(serie, repeticiones)
            }.onSuccess { serieId ->
                onSaved(serieId)
            }.onFailure { error ->
                onError(error)
            }
        }
    }

    fun deleteSerie(serie: SerieEntity) {
        viewModelScope.launch {
            serieDao.deleteSerie(serie)
        }
    }

    fun deleteSerieById(serieId: Long) {
        viewModelScope.launch {
            serieDao.deleteSerieById(serieId)
        }
    }
}
