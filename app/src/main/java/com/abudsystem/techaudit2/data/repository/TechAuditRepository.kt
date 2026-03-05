package com.abudsystem.techaudit2.data.repository


import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import com.abudsystem.techaudit2.data.remote.RetrofitClient

class TechAuditRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getLaboratorios(): List<Laboratorio> {
        return apiService.getLaboratorios()
    }

}