package com.example.ejemploroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="usuario")
data class Usuario (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)