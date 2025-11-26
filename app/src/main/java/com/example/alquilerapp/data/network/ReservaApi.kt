package com.example.alquilerapp.data.network

import com.example.alquilerapp.data.model.ReservaRequest
import com.example.alquilerapp.data.model.ReservaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID
import com.example.alquilerapp.data.model.Reserva


interface ReservaApi {
    @POST("reservas")
    suspend fun crearReserva(@Body reserva: ReservaRequest): Response<ReservaResponse>

    @GET("reservas")
    suspend fun listarReservas(): Response<List<Reserva>>

    @PUT("reservas/{id}")
    suspend fun actualizarReserva(@Path("id") id: UUID, @Body reserva: ReservaRequest): Response<ReservaResponse>
}
