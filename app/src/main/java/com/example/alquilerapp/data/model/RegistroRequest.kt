package com.example.alquilerapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Clase de registro de usuario
 */
data class RegistroRequest(
    val nombre: String?,
    val email: String?,
    @SerializedName("contraseña")
    val contraseña: String?,
    val rol: String?
)
