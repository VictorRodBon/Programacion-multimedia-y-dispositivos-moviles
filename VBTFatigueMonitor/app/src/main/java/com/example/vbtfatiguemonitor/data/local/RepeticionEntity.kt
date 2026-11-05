package com.example.vbtfatiguemonitor.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "repeticiones",
    foreignKeys = [
        ForeignKey(
            entity = SerieEntity::class,
            parentColumns = ["id"],
            childColumns = ["serieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("serieId")]
)
data class RepeticionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val serieId: Long,
    val repNumber: Int,
    val velocity: Float,
    val isFatigueReached: Boolean
)