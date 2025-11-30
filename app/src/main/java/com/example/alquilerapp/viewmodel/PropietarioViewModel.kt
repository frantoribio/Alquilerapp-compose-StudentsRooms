package com.example.alquilerapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.repository.AlquilerRepository
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel para la pantalla de habitaciones del propietario.
 * @param repository Repositorio para interactuar con la API.
 * @return El ViewModel para la pantalla de habitaciones del propietario.
 * @property habitaciones Lista de habitaciones del propietario.
 * @property isLoading Indica si se está cargando.
 * @property errorMessage Mensaje de error en caso de que ocurra.
 * @property cargarHabitacionesPropietario Función para cargar las habitaciones del propietario.
 * @property eliminarHabitacion Función para eliminar una habitación.
 * @property editarHabitacion Función para editar una habitación.
 * @property repository Repositorio para interactuar con la API.
 * @property isLoading Indica si se está cargando.
 * @property eliminarHabitacion Función para eliminar una habitación.
 * @property editarHabitacion Función para editar una habitación.
 * @property viewModelScope Alcance del ViewModel.
 */
class PropietarioViewModel(
    private val repository: AlquilerRepository
) : ViewModel() {
    var habitaciones by mutableStateOf<List<Habitacion>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        cargarHabitacionesPropietario()
    }

    /**
     * Carga las habitaciones asociadas al propietario autenticado.
     * Se asume que el backend filtra por el usuario autenticado.
     */
    fun cargarHabitacionesPropietario() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                habitaciones = repository.getHabitacionesPropietario()
            } catch (e: Exception) {
                errorMessage = "Error al cargar las habitaciones: ${e.message ?: "Desconocido"}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Elimina una habitación.
     * @param habitacionId ID de la habitación a eliminar.
     */
    fun eliminarHabitacion(habitacionId: UUID) {
        viewModelScope.launch {

            try {
                repository.eliminarHabitacion(habitacionId)
                habitaciones = repository.getHabitacionesPropietario()
            } catch (e: Exception) {
                errorMessage = "Error al eliminar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }

    /**
     * Edita una habitación.
     * @param habitacionId ID de la habitación a editar.
     * @param habitacion Habitación actualizada.
     * @return Habitación actualizada.
     */
    fun editarHabitacion(habitacionId: UUID, habitacion: Habitacion) {
        viewModelScope.launch {
            try {
                repository.editarHabitacion(habitacionId, habitacion)
                habitaciones = repository.getHabitacionesPropietario()
            } catch (e: Exception) {
                errorMessage = "Error al editar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }
}
