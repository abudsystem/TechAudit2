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

    suspend fun sincronizar(onProgress: (Int) -> Unit) {
        var progresoActual = 0
        val totalPasos = 4

        // 1. LABORATORIOS REMOTOS -> LOCAL
        val laboratoriosRemotos = RetrofitClient.apiService.getLaboratorios()
        for (remoto in laboratoriosRemotos) {
            val local = laboratorioDao.getById(remoto.id)
            if (local == null) {
                laboratorioDao.insert(remoto)
            } else if (local != remoto) {
                laboratorioDao.update(remoto)
            }
        }
        progresoActual++
        onProgress((progresoActual * 100) / totalPasos)

        // 2. LABORATORIOS LOCALES -> REMOTOS
        val laboratoriosLocales = laboratorioDao.getAllForSync()
        for (local in laboratoriosLocales) {
            val existeEnServidor = laboratoriosRemotos.any { it.id == local.id }
            if (!existeEnServidor) {
                try {
                    val labCreado = RetrofitClient.apiService.crearLaboratorio(local)
                    if (labCreado.id != local.id) {
                        // Solución al CASCADE: Obtener equipos antes de tocar el lab
                        val equiposDelLab = equipoDao.getEquiposByLaboratorioSync(local.id)
                        
                        // Insertar el nuevo laboratorio
                        laboratorioDao.insert(labCreado)
                        
                        // Mover equipos al nuevo ID (insertamos copias con nuevo ID)
                        for (equipo in equiposDelLab) {
                            equipoDao.insert(equipo.copy(laboratorioId = labCreado.id))
                        }
                        
                        // Borrar el lab viejo (esto limpia los equipos con ID viejo por CASCADE)
                        laboratorioDao.delete(local)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        progresoActual++
        onProgress((progresoActual * 100) / totalPasos)

        // 3. EQUIPOS REMOTOS -> LOCAL
        val equiposRemotos = RetrofitClient.apiService.getEquipos()
        val labsActualesIds = laboratorioDao.getAllForSync().map { it.id }.toSet()
        
        for (remoto in equiposRemotos) {
            // Validar FK antes de insertar: Solo si el laboratorioId existe localmente
            if (labsActualesIds.contains(remoto.laboratorioId)) {
                val local = equipoDao.getById(remoto.id)
                if (local == null) {
                    equipoDao.insert(remoto)
                } else if (local != remoto) {
                    equipoDao.update(remoto)
                }
            }
        }
        progresoActual++
        onProgress((progresoActual * 100) / totalPasos)

        // 4. EQUIPOS LOCALES -> REMOTOS
        val equiposLocales = equipoDao.getAllForSync()
        for (local in equiposLocales) {
            val existeEnServidor = equiposRemotos.any { it.id == local.id }
            if (!existeEnServidor) {
                try {
                    val equipoCreado = RetrofitClient.apiService.crearEquipo(local)
                    if (equipoCreado.id != local.id) {
                        // Reemplazar localmente con el ID que asignó el servidor
                        equipoDao.insert(equipoCreado)
                        equipoDao.delete(local)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        
        // Limpieza final: Borrar localmente lo que ya no existe en el servidor
        val labsFinales = RetrofitClient.apiService.getLaboratorios()
        for (local in laboratoriosLocales) {
            if (!labsFinales.any { it.id == local.id }) {
                laboratorioDao.delete(local)
            }
        }
        
        val equiposFinales = RetrofitClient.apiService.getEquipos()
        val equiposLocalesActuales = equipoDao.getAllForSync()
        for (local in equiposLocalesActuales) {
            if (!equiposFinales.any { it.id == local.id }) {
                equipoDao.delete(local)
            }
        }

        onProgress(100)
    }
}
