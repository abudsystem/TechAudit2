package com.abudsystem.techaudit2.data.local.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EstadoEquipo {
    OPERATIVO,
    DANIADO,
    PENDIENTE
}

@Entity(
    tableName = "equipos",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["laboratorioId"])]
)



@Parcelize
data class Equipo (
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey
    val id: String,
    val nombre: String,
    val estado: EstadoEquipo = EstadoEquipo.PENDIENTE,
    val laboratorioId: String
): Parcelable