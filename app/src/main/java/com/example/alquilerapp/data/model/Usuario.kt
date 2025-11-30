package com.example.alquilerapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Clase que representa un usuario en la aplicación.
 * @param id El identificador único del usuario.
 * @param nombre El nombre del usuario.
 * @param email El correo electrónico del usuario.
 * @param contraseña La contraseña del usuario.
 * @param rol El rol del usuario.
 * @param habitacionesPublicadas Las habitaciones publicadas por el usuario.
 * @param reservasRealizadas Las reservas realizadas por el usuario.
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