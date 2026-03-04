package com.abudsystem.techaudit2.util
import androidx.room.TypeConverter
import com.abudsystem.techaudit2.data.local.entity.EstadoEquipo

class Converters {
    // Enum → String
    @TypeConverter
    fun fromEstado(estado: EstadoEquipo): String {
        return estado.name
    }

    // String → Enum
    @TypeConverter
    fun toEstado(value: String): EstadoEquipo {
        return try {
            EstadoEquipo.valueOf(value)
        } catch (e: IllegalArgumentException) {
            EstadoEquipo.PENDIENTE
        }
    }
}