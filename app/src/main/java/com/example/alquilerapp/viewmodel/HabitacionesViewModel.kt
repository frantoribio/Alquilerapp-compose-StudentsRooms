package com.example.alquilerapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.UploadResponse
import com.example.alquilerapp.data.network.RetrofitClient
import com.example.alquilerapp.repository.AlquilerRepository
import com.example.alquilerapp.repository.HabitacionesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

/**
 * ViewModel para la pantalla de Habitaciones
 */
class HabitacionesViewModel(
    private val alquilerRepo: AlquilerRepository? = null
) : ViewModel() {
    private val repo = HabitacionesRepository()
    private val _habitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    val habitaciones: StateFlow<List<Habitacion>> = _habitaciones
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun editarHabitacion(habitacion: Habitacion, id: UUID) {
        viewModelScope.launch {
            try {
                repo.editarHabitacion(id, habitacion)
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


    fun actualizarHabitacion(id: UUID, habitacion: Habitacion) {
        viewModelScope.launch {
            try {
                alquilerRepo?.actualizarHabitacion(id, habitacion)
                loadHabitaciones()
            } catch (e: Exception) {
                _errorMessage.value =
                    "Error al actualizar la habitación: ${e.message ?: "Desconocido"}"
            }
        }
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
}