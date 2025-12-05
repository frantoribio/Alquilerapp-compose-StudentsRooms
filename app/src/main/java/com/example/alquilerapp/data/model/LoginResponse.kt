package com.example.alquilerapp.data.model

/**
 * Clase que representa la respuesta de inicio de sesión.
 */
data class LoginResponse(
    val email: String,
    val token: String,
    val rol: String,
    val id: String
)
