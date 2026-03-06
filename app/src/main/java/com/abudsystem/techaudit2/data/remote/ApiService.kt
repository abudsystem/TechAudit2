package com.abudsystem.techaudit2.data.remote
import com.abudsystem.techaudit2.data.local.entity.Equipo
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("laboratorios")
    suspend fun getLaboratorios(): List<Laboratorio>

    @POST("laboratorios")
    suspend fun crearLaboratorio(
        @Body laboratorio: Laboratorio
    ): Laboratorio

    @PUT("laboratorios/{id}")
    suspend fun actualizarLaboratorio(
        @Path("id") id: String,
        @Body laboratorio: Laboratorio
    ): Laboratorio

    @DELETE("laboratorios/{id}")
    suspend fun eliminarLaboratorio(
        @Path("id") id: String
    )

    // Endpoints para Equipos
    @GET("equipos")
    suspend fun getEquipos(): List<Equipo>

    @POST("equipos")
    suspend fun crearEquipo(
        @Body equipo: Equipo
    ): Equipo

    @PUT("equipos/{id}")
    suspend fun actualizarEquipo(
        @Path("id") id: String,
        @Body equipo: Equipo
    ): Equipo

    @DELETE("equipos/{id}")
    suspend fun eliminarEquipo(
        @Path("id") id: String
    )
}
