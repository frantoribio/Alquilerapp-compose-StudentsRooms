package com.example.alquilerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.repository.AlquilerRepository
import com.example.alquilerapp.repository.HabitacionesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel para la pantalla de Habitaciones
 */
class HabitacionesViewModel(
    private val alquilerRepo: AlquilerRepository? = null
) : ViewModel() {

    // si no te pasan un repo, usas el por defecto
    private val repo = HabitacionesRepository()

    private val _habitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    val habitaciones: StateFlow<List<Habitacion>> = _habitaciones

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun editarHabitacion(habitacion: Habitacion, id: UUID) {
        viewModelScope.launch {
            try {
                repo.editarHabitacion(habitacion.id, habitacion)
                loadHabitaciones()
            } catch (e: Exception) {
                _errorMessage.value = "Error al editar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }

    fun loadHabitaciones(token: String? = null) {
        viewModelScope.launch {
            try {
                val resp = repo.getHabitaciones()
                if (resp.isSuccessful) {
                    _habitaciones.value = resp.body() ?: emptyList()
                } else {
                    _habitaciones.value = emptyList()
                }
            } catch (e: Exception) {
                _habitaciones.value = emptyList()
            }
        }
    }

    fun eliminarHabitacion(habitacionId: String) {
        viewModelScope.launch {
            try {
                alquilerRepo?.eliminarHabitacion(habitacionId)
                loadHabitaciones()
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }

    fun obtenerHabitacionPorId(id: UUID): Habitacion? {
        return habitaciones.value.find { it.id == id.toString() }
    }

    fun actualizarHabitacion(id: UUID, habitacion: Habitacion) {
        viewModelScope.launch {
            try {
                alquilerRepo?.actualizarHabitacion(habitacion.id, habitacion)
                loadHabitaciones()
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }
}

