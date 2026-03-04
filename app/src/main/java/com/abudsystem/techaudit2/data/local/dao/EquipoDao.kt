package com.abudsystem.techaudit2.data.local.dao

import androidx.room.*
import com.abudsystem.techaudit2.data.local.entity.Equipo
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoDao {
    // INSERTAR
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equipo: Equipo)

    // ACTUALIZAR
    @Update
    suspend fun update(equipo: Equipo)

    // ELIMINAR
    @Delete
    suspend fun delete(equipo: Equipo)

    // OBTENER EQUIPOS POR LABORATORIO (REQUERIMIENTO CLAVE)
    @Query("""
        SELECT * FROM equipos
        WHERE laboratorioId = :labId
    """)
    fun getEquiposByLaboratorio(labId: Int): Flow<List<Equipo>>

    // OBTENER UNO POR ID
    @Query("SELECT * FROM equipos WHERE id = :id")
    suspend fun getById(id: Int): Equipo?

    // OBTENER TODOS PARA SINCRONIZAR
    @Query("SELECT * FROM equipos")
    suspend fun getAllForSync(): List<Equipo>

    // OPCIONAL: BORRAR TODOS (útil pruebas)
    @Query("DELETE FROM equipos")
    suspend fun deleteAll()
}