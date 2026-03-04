package com.abudsystem.techaudit2.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laboratorios")
data class Laboratorio (
    @PrimaryKey
    val id: String,
    val nombre: String,
    val edificio: String

)
