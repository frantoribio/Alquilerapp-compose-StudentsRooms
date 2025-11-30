package com.example.alquilerapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.RegistroRequest
import com.example.alquilerapp.data.model.Usuario
import com.example.alquilerapp.data.network.RetrofitClient
import com.example.alquilerapp.repository.UsuarioRepository
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel para la pantalla de Usuarios
 * @param usuarioRepository Repositorio para interactuar con la API
 * @return UsuariosViewModel
 * @property usuarios Lista de usuarios
 * @property loading Indica si se está cargando
 * @property errorMessage Mensaje de error en caso de que ocurra
 * @property usuarioSeleccionado Usuario seleccionado
 * @property cargarUsuarios Función para cargar usuarios
 * @property eliminarUsuario Función para eliminar un usuario
 * @property actualizarUsuario Función para actualizar un usuario
 * @property seleccionarUsuario Función para seleccionar un usuario
 * @property crearUsuario Función para crear un usuario
 * @property obtenerUsuarioPorId Función para obtener un usuario por su ID
 */
class UsuariosViewModel(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    var usuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var usuarioSeleccionado by mutableStateOf<Usuario?>(null)
        private set

    /**
     * Carga los usuarios desde el repositorio.
     * Se asume que el backend filtra por el usuario autenticado.
     */
    fun cargarUsuarios() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            try {
                usuarios = usuarioRepository.obtenerUsuarios()
            } catch (e: Exception) {
                errorMessage = "Error al cargar usuarios: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    /**
     * Elimina un usuario.
     * @param id ID del usuario a eliminar.
     */
    fun eliminarUsuario(id: UUID) {
        viewModelScope.launch {
            try {
                usuarioRepository.eliminarUsuario(id)
                cargarUsuarios()

            } catch (e: Exception) {
                errorMessage = "Error al eliminar: ${e.message}"
            }
        }
    }

    /**
     * Actualiza un usuario.
     * @param id ID del usuario a actualizar.
     * @param usuario Usuario actualizado.
     */
    fun actualizarUsuario(id: UUID, usuario: Usuario) {
        viewModelScope.launch {
            try {
                usuarioRepository.actualizarUsuario(id, usuario)
                cargarUsuarios()
            } catch (e: Exception) {
                errorMessage = "Error al actualizar: ${e.message}"
            }
        }
    }

    /**
     * Selecciona un usuario.
     * @param usuario Usuario seleccionado.
     */
    fun seleccionarUsuario(usuario: Usuario) {
        usuarioSeleccionado = usuario
    }

    /**
     * Crea un usuario.
     * @param nombre Nombre del usuario.
     * @param email Email del usuario.
     * @param contraseña Contraseña del usuario.
     * @param rol Rol del usuario.
     * @param onResult Callback para notificar el resultado de la operación.
     * @return El resultado de la operación.
     */
    fun crearUsuario(nombre: String?, email: String?, contraseña: String?, rol: String?, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = RegistroRequest(nombre, email, contraseña, rol)
                val response = RetrofitClient.instance.registrarUsuario(request)
                if (response.isSuccessful) {
                    onResult(true, response.body()?.mensaje ?: "Creado correctamente")
                } else {
                    onResult(false, "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(false, "Excepción: ${e.message}")
            }
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id ID del usuario a obtener.
     * @return El usuario encontrado, o null si no se encuentra.
     */
    fun obtenerUsuarioPorId(id: UUID): Usuario? {
        return usuarios.find { it.id == id }
    }
}