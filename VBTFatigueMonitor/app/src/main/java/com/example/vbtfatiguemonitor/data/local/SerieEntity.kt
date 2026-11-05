package com.example.vbtfatiguemonitor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity(tableName = "series")
data class SerieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ejercicio: String,
    val fecha: String, // Guardada en formato "YYYY-MM-DD HH:mm"
    val repeticionesTotales: Int,
    val fatigaAlcanzadaPct: Float // Porcentaje de pérdida de velocidad (ej. 18.5%)
)



data class SerieWithRepeticiones(
    @Embedded val serie: SerieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "serieId"
    )
    val repeticiones: List<RepeticionEntity>
)