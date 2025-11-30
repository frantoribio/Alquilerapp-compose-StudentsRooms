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

/**
 * Interfaz que define los puntos finales de la API para las reservas.
 */
interface ReservaApi {
    /**
     * Crea una nueva reserva.
     *
     * @param reserva La reserva a crear.
     * @return La respuesta de la operación.
     */
    @POST("reservas")
    suspend fun crearReserva(@Body reserva: ReservaRequest): Response<ReservaResponse>

    /**
     * Obtiene la lista de reservas.
     *
     * @return La respuesta de la operación.
     */
    @GET("reservas")
    suspend fun listarReservas(): Response<List<Reserva>>

    /**
     * Actualiza una reserva existente.
     *
     * @param id El identificador de la reserva a actualizar.
     */
    @PUT("reservas/{id}")
    suspend fun actualizarReserva(@Path("id") id: UUID, @Body reserva: ReservaRequest): Response<ReservaResponse>
}
