package com.example.alquilerapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.repository.AlquilerRepository
import kotlinx.coroutines.launch

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

    fun eliminarHabitacion(habitacionId: String) {
        viewModelScope.launch {

            try {
                repository.eliminarHabitacion(habitacionId)
                habitaciones = repository.getHabitacionesPropietario()
            } catch (e: Exception) {
                errorMessage = "Error al eliminar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }

    fun editarHabitacion(habitacionId: String, habitacion: Habitacion) {
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
