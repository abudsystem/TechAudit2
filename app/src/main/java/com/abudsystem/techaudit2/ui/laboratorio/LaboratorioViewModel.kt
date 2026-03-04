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

    init {
        val laboratorioDao =
            AuditDatabase.getDatabase(application).laboratorioDao()

        repository = LaboratorioRepository(laboratorioDao)

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

}