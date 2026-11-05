package com.example.vbtfatiguemonitor.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieDao {

    // CREATE
    @Insert
    suspend fun insertSerie(serie: SerieEntity): Long

    @Insert
    suspend fun insertRepeticiones(repeticiones: List<RepeticionEntity>)

    /**
     * Inserta una serie y todas sus repeticiones de forma atómica. Las repeticiones
     * recibidas pueden venir con serieId = 0; se sustituye por el id generado por Room.
     */
    @Transaction
    suspend fun insertSerieWithRepeticiones(
        serie: SerieEntity,
        repeticiones: List<RepeticionEntity>
    ): Long {
        val serieId = insertSerie(serie)
        insertRepeticiones(repeticiones.map { it.copy(serieId = serieId) })
        return serieId
    }

    // READ
    @Transaction
    @Query("SELECT * FROM series ORDER BY fecha DESC, id DESC")
    fun getAllSeriesWithRepeticiones(): Flow<List<SerieWithRepeticiones>>

    @Transaction
    @Query("SELECT * FROM series WHERE id = :serieId")
    fun getSerieById(serieId: Long): Flow<SerieWithRepeticiones?>

    // UPDATE
    @Update
    suspend fun updateSerie(serie: SerieEntity)

    // DELETE
    @Delete
    suspend fun deleteSerie(serie: SerieEntity)

    @Query("DELETE FROM series WHERE id = :serieId")
    suspend fun deleteSerieById(serieId: Long)
}
