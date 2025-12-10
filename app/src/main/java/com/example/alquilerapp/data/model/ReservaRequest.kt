package com.example.alquilerapp.data.model

import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que representa la solicitud de una reserva.

 */
data class ReservaRequest (
    val habitacion: HabitacionId,
    //val alumno: UsuarioId,
    val fechaInicio: String,
    val fechaFin: String,
    val estadoReserva: String = "PENDIENTE"
)
data class HabitacionId(val id: String)
data class UsuarioId(val id: StateFlow<String?>)
