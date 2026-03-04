package com.abudsystem.techaudit2.ui.equipo


import android.app.Application
import androidx.lifecycle.*
import com.abudsystem.techaudit2.data.local.database.AuditDatabase
import com.abudsystem.techaudit2.data.local.entity.Equipo
import com.abudsystem.techaudit2.data.repository.EquipoRepository
import kotlinx.coroutines.launch

class EquipoViewModel (application: Application)
    : AndroidViewModel(application){

    private val repository: EquipoRepository

    init {
        val equipoDao =
            AuditDatabase.getDatabase(application).equipoDao()

        repository = EquipoRepository(equipoDao)
    }

    // 🔹 Obtener equipos por laboratorio dinámicamente
    fun getEquiposByLaboratorio(labId: Int):
            LiveData<List<Equipo>> {
        return repository
            .getEquiposByLaboratorio(labId)
            .asLiveData()
    }

    fun insert(equipo: Equipo) = viewModelScope.launch {
        repository.insert(equipo)
    }

    fun update(equipo: Equipo) = viewModelScope.launch {
        repository.update(equipo)
    }

    fun delete(equipo: Equipo) = viewModelScope.launch {
        repository.delete(equipo)
    }

}
