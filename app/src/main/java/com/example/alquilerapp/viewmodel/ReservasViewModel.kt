package com.example.alquilerapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.repository.ReservaRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    private val reservaRepository: ReservaRepository
) : ViewModel() {

    var reservas by mutableStateOf<List<Reserva>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var reservaSeleccionada by mutableStateOf<Reserva?>(null)
        private set


    fun loadReservas() {
        viewModelScope.launch {
            loading = true
            errorMessage = null
            try {
                reservas = reservaRepository.obtenerReservas()
            } catch (e: Exception) {
                errorMessage = "Error al cargar reservas: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    fun eliminarReserva(id: UUID) {
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


    fun crearReserva(
        habitacionId: UUID,
        usuarioId: UUID,
        entrada: String,
        salida: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {
                onResult(false, "Excepción: ${e.message}")
            }
        }
    }


    fun obtenerReservaPorId(id: UUID): Reserva? {
        return reservas.find { it.id == id }
    }

    fun confirmarReserva(
        habitacionId: String,
        alumnoId: String,
        entrada: LocalDate,
        salida: LocalDate,
        estadoReserva: String
    ) {
        viewModelScope.launch {
            /*try {
                val reservaRequest = ReservaRequest(
                    habitacion = habitacionId,
                    alumnoId = alumnoId,
                    fechaInicio = entrada,
                    fechaFin = salida,
                    entrada = entrada,
                    salida = salida,
                    estadoReserva = "PENDIENTE"
                )

                val response = RetrofitClient.instance.confirmarReserva(reservaRequest)
                if (response.isSuccessful) {
                    val reserva = response.body()
                }
            } catch (e: Exception) {*/

        }
    }
}



