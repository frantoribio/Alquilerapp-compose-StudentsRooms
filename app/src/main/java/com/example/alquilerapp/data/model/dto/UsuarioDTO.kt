package com.example.alquilerapp.data.model.dto

import java.util.UUID

/*
 * DTO (Data Transfer Object) para el usuario.
 */
data class UsuarioDTO(
    val id: UUID? = null,
    val nombre: String,
    val email: String,
    val contraseña: String? = null,
    val rol: String
)