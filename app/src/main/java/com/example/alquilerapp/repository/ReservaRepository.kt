package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.data.network.ApiService
import com.example.alquilerapp.data.network.RetrofitClient
import java.util.UUID

/**
 * Clase que se encarga de obtener los datos de la api
 * @param apiService el servicio que se encarga de obtener los datos de la api
 * @return las reservas de la api
 */
class ReservaRepository(private val apiService: ApiService) {

    private val api = RetrofitClient.instance

    /**
     * obtiene las reservas de la api
     * @return las reservas de la api
     */
    suspend fun obtenerReservas(): List<Reserva> {
        return apiService.listarReservas()
    }

    /**
     * crea una reserva en la api
     * @param reserva la reserva a crear
     * @return la reserva creada
     */
    suspend fun crearReserva(reserva: Reserva) {
        apiService.crearReserva(reserva)
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
    suspend fun eliminarReserva(id: UUID) {
        apiService.eliminarReserva(id)
    }
}
