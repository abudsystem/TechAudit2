package com.abudsystem.techaudit2.data.repository

import com.abudsystem.techaudit2.data.local.dao.EquipoDao
import com.abudsystem.techaudit2.data.local.entity.Equipo
import com.abudsystem.techaudit2.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow


class EquipoRepository (private val equipoDao: EquipoDao) {
    // Obtener equipos por laboratorio (clave del proyecto)
    fun getEquiposByLaboratorio(labId: String): Flow<List<Equipo>> {
        return equipoDao.getEquiposByLaboratorio(labId)
    }

    // Insertar
    suspend fun insert(equipo: Equipo) {
        equipoDao.insert(equipo)
    }

    // Actualizar
    suspend fun update(equipo: Equipo) {
        equipoDao.update(equipo)
        try {
            RetrofitClient.apiService.actualizarEquipo(equipo.id, equipo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Eliminar
    suspend fun delete(equipo: Equipo) {
        equipoDao.delete(equipo)
        try {
            // Eliminar de la API también
            RetrofitClient.apiService.eliminarEquipo(equipo.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Obtener por ID
    suspend fun getById(id: String): Equipo? {
        return equipoDao.getById(id)
    }

    // Para sincronización futura
    suspend fun getAllForSync(): List<Equipo> {
        return equipoDao.getAllForSync()
    }
}
