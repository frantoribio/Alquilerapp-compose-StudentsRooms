package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.Usuario
import com.example.alquilerapp.data.network.ApiService
import java.util.UUID

/**
 * Clase que se encarga de obtener los datos de la api
 * @param apiService el servicio que se encarga de obtener los datos de la api
 * @return los usuarios de la api
 */
class UsuarioRepository(private val apiService: ApiService) {

    /**
     * obtiene los usuarios de la api
     * @return los usuarios de la api
     */
    suspend fun obtenerUsuarios(): List<Usuario> {
        return apiService.listarUsuarios()
    }

    /**
     * actualiza un usuario en la api
     * @param usuario el usuario a actualizar
     * @return el usuario actualizado
     */
    suspend fun actualizarUsuario(id: UUID, usuario: Usuario) {
        apiService.actualizarUsuario(id, usuario)
    }

    /**
     * elimina un usuario en la api
     * @param id el id del usuario a eliminar
     */
    suspend fun eliminarUsuario(id: UUID) {
        apiService.eliminarUsuario(id)
    }
}
