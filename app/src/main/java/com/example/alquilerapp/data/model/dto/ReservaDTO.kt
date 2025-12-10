package com.example.alquilerapp.data.model.dto

data class ReservaDTO (
    val id: String,
    val habitacionId: String,
    val alumnoId: String,
    val alumnoEmail: String?,
    val propietarioEmail: String?,
    val propietarioId: String?,
    val fechaInicio: String,
    val fechaFin: String,
    val estadoReserva: String
)
