package com.example.alquilerapp.data.model

/**
 * Clase que representa una reserva.
 *
 */
data class Reserva(
    val id: String? = null,
    val fechaInicio: String? = null,
    val fechaFin: String? = null,
    val habitacionId: String? = null,
    val alumnoId: String? = null,
    val estadoReserva: String? = null

)