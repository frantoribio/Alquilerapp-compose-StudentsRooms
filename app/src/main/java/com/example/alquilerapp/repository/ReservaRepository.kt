package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.data.network.ApiService
import java.util.UUID

class ReservaRepository(private val apiService: ApiService) {

    //private val api = RetrofitClient.instance

    suspend fun obtenerReservas(): List<Reserva> {
        return apiService.listarReservas()
    }

    suspend fun crearReserva(reserva: Reserva) {
        apiService.crearReserva(reserva)
    }

    suspend fun actualizarReserva(id: UUID, reserva: Reserva) {
        apiService.actualizarReserva(id, reserva)
    }

    suspend fun eliminarReserva(id: UUID) {
        apiService.eliminarReserva(id)
    }
}
