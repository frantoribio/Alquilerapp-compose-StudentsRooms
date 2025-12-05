package com.example.alquilerapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alquilerapp.data.model.HabitacionId
import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.data.model.ReservaRequest
import com.example.alquilerapp.data.model.UsuarioId
import com.example.alquilerapp.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID



/**
 * ViewModel para la pantalla de Reservas
 * @param reservaRepository Repositorio para interactuar con la API
 * @return ReservasViewModel
 * @property reservas Lista de reservas
 * @property loading Indica si se está cargando
 * @property errorMessage Mensaje de error en caso de que ocurra
 * @property reservaSeleccionada Reserva seleccionada
 * @property loadReservas Función para cargar reservas
 * @property eliminarReserva Función para eliminar una reserva
 * @property actualizarReserva Función para actualizar una reserva
 */
class ReservasViewModel(
    private val reservaRepository: ReservaRepository,
    private val loginViewModel: LoginViewModel
) : ViewModel() {

    var reservas by mutableStateOf<List<Reserva>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var reservaSeleccionada by mutableStateOf<Reserva?>(null)
        private set

    private val _alumnoId = MutableStateFlow<String?>(null)
    val alumnoId: StateFlow<String?> = _alumnoId

    fun setAlumnoId(id: String) {
        _alumnoId.value = id
    }

    fun loadReservas() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            try {
                val response = reservaRepository.obtenerReservas()
                if (response.isSuccessful) {
                    reservas = response.body() ?: emptyList()
                } else {
                    errorMessage = "Error al cargar reservas: ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = "Error al cargar reservas: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    fun eliminarReserva(id: UUID?) {
        viewModelScope.launch {
            try {
                reservaRepository.eliminarReserva(id)
                loadReservas()
            } catch (e: Exception) {
                errorMessage = "Error al eliminar: ${e.message}"
            }
        }
    }

    fun actualizarReserva(id: UUID, reserva: Reserva) {
        viewModelScope.launch {
            try {
                reservaRepository.actualizarReserva(id, reserva)
                loadReservas()
            } catch (e: Exception) {
                errorMessage = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun seleccionarReserva(reserva: Reserva) {
        reservaSeleccionada = reserva
    }

    // ✅ Confirmar reserva limpio
    fun confirmarReserva(
        habitacionId: String,
        entrada: String,
        salida: String,
        estadoReserva: String
    ) {
        val alumnoId = loginViewModel.alumnoId.value ?: return

        viewModelScope.launch {
            try {
                val reservaRequest = ReservaRequest(
                    habitacion = HabitacionId(habitacionId),
                    alumno = UsuarioId(alumnoId),
                    fechaInicio = entrada,
                    fechaFin = salida,
                    estadoReserva = estadoReserva
                )

                val response = reservaRepository.crearReserva(reservaRequest)
                if (response.isSuccessful) {
                    val reserva = response.body()
                    if (reserva != null) {
                        loadReservas()
                    } else {
                        errorMessage = "Error al confirmar reserva: Respuesta nula"
                    }
                } else {
                    loadReservas()
                    errorMessage = "Error al confirmar reserva: ${response.code()}"
                }


            } catch (e: Exception) {
                errorMessage = "Error al confirmar reserva: ${e.message}"
            }
        }
    }

   /* fun getReservasByHabitacion(habitacionId: String): List<Reserva> {
        var reservas = reservas
        if (reservas.isEmpty()) {
            reservas = loadReservas().toMutableList()
        }

        return reservas.filter {
            it.habitacion.id.toString() == habitacionId
        }
    }

    fun getReservasByAlumno(alumnoId: String): List<Reserva> {
        return reservas.filter {
            it.alumno.id.toString() == alumnoId
        }
    }*/
}

