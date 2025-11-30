package com.example.alquilerapp.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.UploadResponse
import com.example.alquilerapp.data.network.RetrofitClient
import com.example.alquilerapp.repository.AlquilerRepository
import com.example.alquilerapp.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

/**
 * ViewModel para la pantalla de Habitaciones
 * @param alquilerRepo Repositorio para interactuar con la API
 * @return HabitacionesViewModel
 * @property habitaciones Lista de habitaciones
 * @property errorMessage Mensaje de error en caso de que ocurra
 * @property habitacionSeleccionada Habitación seleccionada
 * @property loadHabitaciones Función para cargar habitaciones
 * @property eliminarHabitacion Función para eliminar una habitación
 * @property obtenerHabitacionPorId Función para obtener una habitación por su ID
 * @property editarHabitacion Función para editar una habitación
 * @property onImageSelected Función para seleccionar una imagen
 * @property cargarHabitacion Función para cargar una habitación
 * @property _habitaciones MutableStateFlow para habitaciones
 * @property _errorMessage MutableStateFlow para mensajes de error
 * @property _habitacionSeleccionada MutableStateFlow para habitación seleccionada
 * @property habitaciones StateFlow para habitaciones
 * @property errorMessage StateFlow para mensajes de error
 * @property habitacionSeleccionada StateFlow para habitación seleccionada
 * @property alquilerRepo Repositorio para interactuar con la API
 * @property repo Repositorio para interactuar con la API
 * @property viewModelScope Alcance del ViewModel
 * @property loadHabitaciones Función para cargar habitaciones
 * @property eliminarHabitacion Función para eliminar una habitación
 * @property obtenerHabitacionPorId Función para obtener una habitación por su ID
 * @property editarHabitacion Función para editar una habitación
 * @property onImageSelected Función para seleccionar una imagen
 * @property cargarHabitacion Función para cargar una habitación
 * @property _imagenesUrl MutableStateFlow para URLs de imágenes
 * @property imagenesUrl StateFlow para URLs de imágenes
 * @property api ApiService para interactuar con la API
 * @property response Respuesta de la API
 * @property imageUrl URL de la imagen
 * @property inputStream Flujo de entrada de la imagen
 * @property fileBytes Bytes de la imagen
 * @property requestBody Parte de la solicitud para la imagen
 * @property multipart Cuerpo de la solicitud para la imagen
 * @property contentResolver Resuelve el contenido de la URI
 * @property inputStream Flujo de entrada de la imagen
 * @property fileBytes Bytes de la imagen
 * @property requestBody Parte de la solicitud para la imagen
 * @property multipart Cuerpo de la solicitud para la imagen
 * @property api ApiService para interactuar con la API
 * @property response Respuesta de la API
 * @property imageUrl URL de la imagen
 * @property inputStream Flujo de entrada de la imagen
 * @property fileBytes Bytes de la imagen
 * @property requestBody Parte de la solicitud para la imagen
 * @property multipart Cuerpo de la solicitud para la imagen
 * @property contentResolver Resuelve el contenido de la URI

 */
class HabitacionesViewModel(
    private val alquilerRepo: AlquilerRepository
) : ViewModel() {
    private val repo = LoginRepository()
    private val _habitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    val habitaciones: StateFlow<List<Habitacion>> = _habitaciones
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _habitacionSeleccionada = MutableStateFlow<Habitacion?>(null)
    val habitacionSeleccionada: StateFlow<Habitacion?> = _habitacionSeleccionada


    fun editarHabitacion(habitacion: Habitacion, id: UUID) {
        viewModelScope.launch {
            try {
                val resp = alquilerRepo.editarHabitacion(id, habitacion)
                if (resp.isSuccessful) {
                    val updated = resp.body()
                    _habitaciones.value = _habitaciones.value.map {
                        if (it.id == id) updated ?: habitacion else it
                    }
                    Log.d("HabitacionesViewModel", "Habitación actualizada: ${updated}")
                } else {
                    val errorBody = resp.errorBody()?.string()
                    _errorMessage.value = "Error al editar: ${resp.code()} - $errorBody"
                    Log.e("HabitacionesViewModel", "Error al editar: ${resp.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al editar: ${e.message}"
                Log.e("HabitacionesViewModel", "Excepción al editar", e)
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

    fun eliminarHabitacion(habitacionId: UUID) {
        viewModelScope.launch {
            try {
                alquilerRepo?.eliminarHabitacion(habitacionId)
                loadHabitaciones()
            } catch (e: Exception) {
                _errorMessage.value =
                    "Error al eliminar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
    }

    fun obtenerHabitacionPorId(id: UUID): Habitacion? {
        return habitaciones.value.find { it.id == id }
    }


    fun onImageSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(uri) ?: return@launch
                val fileBytes = inputStream.readBytes()
                val requestBody = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData(
                    "file", "imagen.jpg", requestBody
                )
                val api = RetrofitClient.instance
                val response = api.uploadImage(multipart)
                val _imagenesUrl = MutableStateFlow<List<String>>(emptyList())
                // tu ApiService
                if (response.isSuccessful) {
                    val imageUrl: UploadResponse = response.body()!!
                    // 🔹 Actualiza la lista de imágenes
                    val current = _imagenesUrl.value.toMutableList()
                    current.add(imageUrl.toString())
                    _imagenesUrl.value = current
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al subir imagen: ${e.message}"
            }
        }
    }

    fun cargarHabitacion(id: UUID) {
        viewModelScope.launch {
            val habitacion = obtenerHabitacionPorId(id)
            _habitacionSeleccionada.value = habitacion
        }
    }
}