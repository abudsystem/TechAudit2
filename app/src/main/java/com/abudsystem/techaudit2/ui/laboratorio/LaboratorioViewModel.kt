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

    val loading = MutableLiveData<Boolean>()

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
        loading.value = true
        try {
            repository.sincronizar()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loading.value = false
    }
}
