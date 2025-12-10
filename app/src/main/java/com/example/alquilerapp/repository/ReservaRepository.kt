package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.data.model.ReservaRequest
import com.example.alquilerapp.data.model.ReservaResponse
import com.example.alquilerapp.data.model.dto.ReservaDTO
import com.example.alquilerapp.data.network.ApiService
import retrofit2.Response
import java.util.UUID

/**
 * Clase que se encarga de obtener los datos de la api
 * @param apiService el servicio que se encarga de obtener los datos de la api
 * @return las reservas de la api
 */
class ReservaRepository(private val apiService: ApiService) {



    /**
     * obtiene las reservas de la api
     * @return las reservas de la api
     */
    suspend fun obtenerReservas(): Response<List<ReservaDTO>> {
        return apiService.listarReservas()
    }

    /**
     * crea una reserva en la api
     * @param reserva la reserva a crear
     * @return la reserva creada
     */

    suspend fun crearReserva(reserva: ReservaRequest): Response<ReservaResponse> {
        return apiService.crearReserva(reserva)
    }

    /**
     * actualiza una reserva en la api
     * @param id el id de la reserva a actualizar
     * @param reserva la reserva a actualizar
     */
    suspend fun actualizarReserva(id: UUID, reserva: Reserva) {
        apiService.actualizarReserva(id, reserva)
    }

    /**
     * elimina una reserva en la api
     * @param id el id de la reserva a eliminar
     */
    suspend fun eliminarReserva(id: String) {
        apiService.eliminarReserva(id)
    }

    fun filtrarReservasPorAlumno(reservas: List<ReservaDTO>, alumnoId: String): List<ReservaDTO> {
        return reservas.filter { it.alumnoId.toString() == alumnoId }
    }



    suspend fun obtenerReservaPorHabitacion(id: String): List<ReservaDTO> {
        return apiService.obtenerReservasPorHabitacion(id)
    }

    suspend fun obtenerReservaPorAlumno(id: String): List<ReservaDTO> {
        return apiService.obtenerReservasPorAlumno(id)
    }

}