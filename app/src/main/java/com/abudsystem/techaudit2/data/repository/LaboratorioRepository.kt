package com.abudsystem.techaudit2.data.repository

import com.abudsystem.techaudit2.data.local.dao.LaboratorioDao
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import com.abudsystem.techaudit2.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class LaboratorioRepository(
    private val laboratorioDao: LaboratorioDao
) {

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

    // 🔹 Sincronizar desde MockAPI
    suspend fun sincronizarDesdeApi() {

        try {

            val apiLabs =
                RetrofitClient.apiService.getLaboratorios()

            if (apiLabs.isNotEmpty()) {

                laboratorioDao.insertAll(apiLabs)

            }

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }

}

/*
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

    suspend fun sincronizarDesdeApi() {

        val apiLabs =
            RetrofitClient.apiService.getLaboratorios()

        laboratorioDao.insertAll(apiLabs)
    }

}

 */