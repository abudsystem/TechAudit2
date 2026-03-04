package com.abudsystem.techaudit2.data.repository

import com.abudsystem.techaudit2.data.local.dao.LaboratorioDao
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import kotlinx.coroutines.flow.Flow

class LaboratorioRepository ( private val laboratorioDao: LaboratorioDao ){

    // Obtener todos (para UI)
    val laboratorios: Flow<List<Laboratorio>> =
        laboratorioDao.getAllLaboratorios()

    // Insertar
    suspend fun insert(laboratorio: Laboratorio) {
        laboratorioDao.insert(laboratorio)
    }

    // Actualizar
    suspend fun update(laboratorio: Laboratorio) {
        laboratorioDao.update(laboratorio)
    }

    // Eliminar
    suspend fun delete(laboratorio: Laboratorio) {
        laboratorioDao.delete(laboratorio)
    }

    // Obtener por ID
    suspend fun getById(id: Int): Laboratorio? {
        return laboratorioDao.getById(id)
    }

    // Para sincronización futura
    suspend fun getAllForSync(): List<Laboratorio> {
        return laboratorioDao.getAllForSync()
    }

}