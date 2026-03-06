package com.abudsystem.techaudit2.data.local.entity
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "laboratorios")
@Parcelize
data class Laboratorio (
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey
    val id: String,
    val nombre: String,
    val edificio: String

): Parcelable
