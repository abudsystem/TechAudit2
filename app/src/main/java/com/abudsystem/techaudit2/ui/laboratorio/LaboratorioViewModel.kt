package com.abudsystem.techaudit2.ui.laboratorio

import android.app.Application
import androidx.lifecycle.*
import com.abudsystem.techaudit2.data.local.database.AuditDatabase
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import com.abudsystem.techaudit2.data.repository.LaboratorioRepository
import kotlinx.coroutines.launch

class LaboratorioViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository: LaboratorioRepository

    val laboratorios: LiveData<List<Laboratorio>>

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _progreso = MutableLiveData<Int>()
    val progreso: LiveData<Int> get() = _progreso

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> get() = _mensaje

    init {
        val database = AuditDatabase.getDatabase(application)
        val laboratorioDao = database.laboratorioDao()
        val equipoDao = database.equipoDao()

        repository = LaboratorioRepository(laboratorioDao, equipoDao)

        laboratorios = repository.laboratorios.asLiveData()
    }

    fun insert(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.insert(laboratorio)
    }

    fun update(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.update(laboratorio)
    }

    fun delete(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.delete(laboratorio)
    }

    fun sincronizar() = viewModelScope.launch {
        _loading.value = true
        _progreso.value = 0
        _mensaje.value = null
        try {
            repository.sincronizar { p ->
                _progreso.postValue(p)
            }
            _mensaje.value = "Sincronización completa"
        } catch (e: Exception) {
            e.printStackTrace()
            _mensaje.value = "Error: ${e.message}"
        }
        _loading.value = false
    }
    
    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
