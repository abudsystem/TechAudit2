package com.abudsystem.techaudit2.data.repository

import com.abudsystem.techaudit2.data.local.dao.EquipoDao
import com.abudsystem.techaudit2.data.local.dao.LaboratorioDao
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import com.abudsystem.techaudit2.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class LaboratorioRepository(
    private val laboratorioDao: LaboratorioDao,
    private val equipoDao: EquipoDao
) {

    val laboratorios: Flow<List<Laboratorio>> =
        laboratorioDao.getAllLaboratorios()

    suspend fun insert(laboratorio: Laboratorio) {
        laboratorioDao.insert(laboratorio)
    }

    suspend fun update(laboratorio: Laboratorio) {
        laboratorioDao.update(laboratorio)
        try {
            RetrofitClient.apiService.actualizarLaboratorio(laboratorio.id, laboratorio)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(laboratorio: Laboratorio) {
        laboratorioDao.delete(laboratorio)
        try {
            // También borramos en la API
            RetrofitClient.apiService.eliminarLaboratorio(laboratorio.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getById(id: String): Laboratorio? {
        return laboratorioDao.getById(id)
    }

    suspend fun getAllForSync(): List<Laboratorio> {
        return laboratorioDao.getAllForSync()
    }

    suspend fun sincronizar() {
        // 1. SINCRONIZAR LABORATORIOS
        val laboratoriosRemotos = RetrofitClient.apiService.getLaboratorios()
        val laboratoriosLocales = laboratorioDao.getAllForSync()

        // Remoto a Local
        for (remoto in laboratoriosRemotos) {
            val local = laboratorioDao.getById(remoto.id)
            if (local == null) {
                laboratorioDao.insert(remoto)
            } else if (local != remoto) {
                laboratorioDao.update(remoto)
            }
        }

        // Local a Remoto (Evitar duplicados por ID de MockAPI)
        for (local in laboratoriosLocales) {
            val existeEnServidor = laboratoriosRemotos.any { it.id == local.id }
            if (!existeEnServidor) {
                try {
                    // Al crear en MockAPI, nos devuelve el objeto con el ID real del servidor
                    val labCreado = RetrofitClient.apiService.crearLaboratorio(local)
                    // Si el ID cambió, borramos el local viejo e insertamos el nuevo
                    if (labCreado.id != local.id) {
                        laboratorioDao.delete(local)
                        laboratorioDao.insert(labCreado)
                        
                        // Actualizar equipos vinculados al nuevo ID del laboratorio
                        val equiposDelLab = equipoDao.getEquiposByLaboratorioSync(local.id)
                        for (equipo in equiposDelLab) {
                            val equipoActualizado = equipo.copy(laboratorioId = labCreado.id)
                            equipoDao.update(equipoActualizado)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // 2. SINCRONIZAR EQUIPOS
        val equiposRemotos = RetrofitClient.apiService.getEquipos()
        val equiposLocales = equipoDao.getAllForSync()

        for (remoto in equiposRemotos) {
            val local = equipoDao.getById(remoto.id)
            if (local == null) {
                equipoDao.insert(remoto)
            } else if (local != remoto) {
                equipoDao.update(remoto)
            }
        }

        for (local in equiposLocales) {
            val existeEnServidor = equiposRemotos.any { it.id == local.id }
            if (!existeEnServidor) {
                try {
                    val equipoCreado = RetrofitClient.apiService.crearEquipo(local)
                    if (equipoCreado.id != local.id) {
                        equipoDao.delete(local)
                        equipoDao.insert(equipoCreado)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        
        // 3. LIMPIEZA LOCAL (Si algo se borró en el servidor, borrarlo aquí)
        // Esto evita que lo borrado en el servidor reaparezca
        val laboratoriosRemotosNuevos = RetrofitClient.apiService.getLaboratorios()
        for (local in laboratoriosLocales) {
            if (!laboratoriosRemotosNuevos.any { it.id == local.id }) {
                laboratorioDao.delete(local)
            }
        }
        
        val equiposRemotosNuevos = RetrofitClient.apiService.getEquipos()
        for (local in equiposLocales) {
            if (!equiposRemotosNuevos.any { it.id == local.id }) {
                equipoDao.delete(local)
            }
        }
    }
}
