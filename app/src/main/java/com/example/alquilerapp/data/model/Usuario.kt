package com.example.alquilerapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Clase que representa un usuario en la aplicación.
 */
data class Usuario(
    val id: UUID? = null,
    val nombre: String? = null,
    val email: String? = null,
    @SerializedName("contraseña")
    val contrasena: String? = null,
    val rol: Rol? = null,
    @SerializedName("habitacionesPublicadas")
    val habitacionesPublicadas: List<Habitacion>? = null,
    @SerializedName("reservasRealizadas")
    val reservasRealizadas: List<Reserva>? = null
)