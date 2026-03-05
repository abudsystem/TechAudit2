package com.abudsystem.techaudit2.data.local.dao

import androidx.room.*
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import kotlinx.coroutines.flow.Flow

@Dao
interface LaboratorioDao {

    // INSERTAR
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(laboratorio: Laboratorio)

    // ACTUALIZAR
    @Update
    suspend fun update(laboratorio: Laboratorio)

    // ELIMINAR
    @Delete
    suspend fun delete(laboratorio: Laboratorio)

    // OBTENER TODOS (reactivo para UI)
    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>

    // OBTENER UNO POR ID
    @Query("SELECT * FROM laboratorios WHERE id = :id")
    suspend fun getById(id: Int): Laboratorio?

    // OBTENER TODOS PARA SINCRONIZAR (sin Flow)
    @Query("SELECT * FROM laboratorios")
    suspend fun getAllForSync(): List<Laboratorio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lista: List<Laboratorio>)

}