package com.example.alquilerapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.HabitacionId
import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.data.model.ReservaRequest
import com.example.alquilerapp.data.model.dto.ReservaDTO
import com.example.alquilerapp.repository.LoginRepository
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

    private val repo = LoginRepository()

    // Variable reactiva principal para la pantalla de Reservas (general)
    var reservas by mutableStateOf<List<ReservaDTO>>(emptyList())
        private set

    // StateFlow general (parece redundante con 'reservas' mutableStateOf, pero lo mantengo)
    private val _reservass = MutableStateFlow<List<ReservaDTO>>(emptyList())
    val reservass: StateFlow<List<ReservaDTO>> = _reservass

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // *** ESTADO CLAVE PARA EstudianteReservasScreen ***
    private val _reservasAlumnos = MutableStateFlow<List<ReservaDTO>>(emptyList())
    // Esta es la variable que la UI observa (collectAsState())
    val reservasAlumnos: StateFlow<List<ReservaDTO>> = _reservasAlumnos
    // *Se eliminó la variable 'reservasAlumno' mutableStateOf, ya que es redundante*

    // Variables para otras vistas
    var reservasHabitacion by mutableStateOf<List<ReservaDTO>>(emptyList())
        private set

    private val _reservaSeleccionada = MutableStateFlow<ReservaDTO?>(null)
    val reservaSeleccionada: StateFlow<ReservaDTO?> = _reservaSeleccionada


    // *** FUNCIÓN DE CARGA CORREGIDA Y ACTIVA ***
    fun cargarReservasPorAlumno(alumnoId: String) {
        viewModelScope.launch {
            try {
                // 1. Obtener los datos del repositorio
                val reservas = reservaRepository.obtenerReservaPorAlumno(alumnoId)

                // 2. Actualizar el StateFlow para que la UI se recomponga
                _reservasAlumnos.value = reservas

            } catch (e: Exception) {
                Log.e("ReservasViewModel", "Error cargando reservas del alumno", e)
                // En caso de error, limpiar la lista
                _reservasAlumnos.value = emptyList()
            }
        }
    }


    fun cargarReservasPorHabitacion(habitacionId: String) {
        viewModelScope.launch {
            try {
                val response = reservaRepository.obtenerReservaPorHabitacion(habitacionId)
                reservasHabitacion = response
            } catch (e: Exception) {
                reservasHabitacion = emptyList()
            }
        }
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
    fun obtenerReservaPorId(id: String): ReservaDTO? {
        return reservass.value.find { it.id == id }
    }

    fun cargarReserva(id: String) {
        viewModelScope.launch {
            val reserva = obtenerReservaPorId(id)
            _reservaSeleccionada.value = reserva
        }
    }

    fun eliminarReserva(id: String) {
        viewModelScope.launch {
            try {
                reservaRepository.eliminarReserva(id)
                loadReservas()
            } catch (e: Exception) {
                errorMessage = "Error al eliminar: ${e.message}"
            }
        }
    }

    fun confirmarReserva(
        habitacionId: String,
        entrada: String,
        salida: String,
        estadoReserva: String
    ) {
        viewModelScope.launch {
            try {
                val reservaRequest = ReservaRequest(
                    habitacion = HabitacionId(habitacionId),
                    //alumno = UsuarioId(loginViewModel.alumnoId),
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
}

/*
class ReservasViewModel(
    private val reservaRepository: ReservaRepository,
    private val loginViewModel: LoginViewModel
) : ViewModel() {

    private val repo = LoginRepository()

    var reservas by mutableStateOf<List<ReservaDTO>>(emptyList())
        private set

    private val _reservass = MutableStateFlow<List<ReservaDTO>>(emptyList())
    val reservass: StateFlow<List<ReservaDTO>> = _reservass


    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    */
/*var reservaSeleccionada by mutableStateOf<ReservaDTO?>(null)
        private set*//*


    var reservasAlumno: List<ReservaDTO> by mutableStateOf<List<ReservaDTO>>(emptyList())
        private set

    var reservasHabitacion by mutableStateOf<List<ReservaDTO>>(emptyList())
        private set

    private val _reservasAlumnos = MutableStateFlow<List<ReservaDTO>>(emptyList())
    var reservasAlumnos: StateFlow<List<ReservaDTO>> = _reservasAlumnos

    private val _reservaSeleccionada = MutableStateFlow<ReservaDTO?>(null)
    val reservaSeleccionada: StateFlow<ReservaDTO?> = _reservaSeleccionada

   */
/* fun cargaReservasPorAlumno(alumnoId: String) {
        viewModelScope.launch {
            try {
                val reservas = reservaRepository.obtenerReservaPorAlumno(alumnoId)
                _reservasAlumnos.value = reservas
            } catch (e: Exception) {
                Log.e("ReservasViewModel", "Error cargando reservas", e)
            }
        }
    }*//*




    fun cargarReservasPorHabitacion(habitacionId: String) {
        viewModelScope.launch {
            try {
                val response = reservaRepository.obtenerReservaPorHabitacion(habitacionId)
                reservasHabitacion = response
            } catch (e: Exception) {
                reservasHabitacion = emptyList()
            }
        }
    }

    */
/*fun cargarReservasPorAlumno(alumnoId: String) {
        viewModelScope.launch {
            try {
                val response = reservaRepository.obtenerReservaPorAlumno(alumnoId)
                reservasAlumno = response
            } catch (e: Exception) {
                reservasAlumno = emptyList()

            }
        }
    }*//*


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
    fun obtenerReservaPorId(id: String): ReservaDTO? {
        return reservass.value.find { it.id == id }
    }

    fun cargarReserva(id: String) {
        viewModelScope.launch {
            val reserva = obtenerReservaPorId(id)
            _reservaSeleccionada.value = reserva
        }

    }


    */
/*fun obtenerReservasDeAlumno(alumnoId: String): List<ReservaDTO> {
        return reservaRepository.filtrarReservasPorAlumno(
            reservas.filter { it.alumnoId.toString() == alumnoId }, alumnoId
        )
    }*//*





    fun eliminarReserva(id: String) {
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

    fun confirmarReserva(
        habitacionId: String,
        entrada: String,
        salida: String,
        estadoReserva: String
    ) {
        viewModelScope.launch {
            try {
                val reservaRequest = ReservaRequest(
                    habitacion = HabitacionId(habitacionId),
                    //alumno = UsuarioId(loginViewModel.alumnoId),
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

    fun cargarReservasPorAlumno(alumnoId: String) {
        viewModelScope.launch {
            try {
                val reservas = reservaRepository.obtenerReservaPorAlumno(alumnoId)
                _reservasAlumnos.value = reservas
            } catch (e: Exception) {
                _reservasAlumnos.value = emptyList()
            }
        }
    }

}

*/
