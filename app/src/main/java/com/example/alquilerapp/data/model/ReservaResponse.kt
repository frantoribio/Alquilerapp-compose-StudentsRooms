package com.example.alquilerapp.data.model

/**
 * Clase que representa la respuesta de una reserva.
 *
 */
data class ReservaResponse (
    val id: String,
    val habitacion: Habitacion,
    val fechaInicio: String,
    val fechaFin: String,
    val estadoReserva: String
)
