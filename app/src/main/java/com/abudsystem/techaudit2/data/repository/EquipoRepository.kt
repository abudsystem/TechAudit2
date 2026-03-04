package com.abudsystem.techaudit2.data.repository

import com.abudsystem.techaudit2.data.local.dao.EquipoDao
import com.abudsystem.techaudit2.data.local.entity.Equipo
import kotlinx.coroutines.flow.Flow


class EquipoRepository (private val equipoDao: EquipoDao) {
    // Obtener equipos por laboratorio (clave del proyecto)
    fun getEquiposByLaboratorio(labId: Int): Flow<List<Equipo>> {
        return equipoDao.getEquiposByLaboratorio(labId)
    }

    // Insertar
    suspend fun insert(equipo: Equipo) {
        equipoDao.insert(equipo)
    }

    // Actualizar
    suspend fun update(equipo: Equipo) {
        equipoDao.update(equipo)
    }

    // Eliminar
    suspend fun delete(equipo: Equipo) {
        equipoDao.delete(equipo)
    }

    // Obtener por ID
    suspend fun getById(id: Int): Equipo? {
        return equipoDao.getById(id)
    }

    // Para sincronización futura
    suspend fun getAllForSync(): List<Equipo> {
        return equipoDao.getAllForSync()
    }
}