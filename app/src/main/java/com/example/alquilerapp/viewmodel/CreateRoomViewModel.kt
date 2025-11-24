package com.example.alquilerapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.TokenStore
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.dto.CrearHabitacionDto
import com.example.alquilerapp.repository.AlquilerRepository
import com.example.alquilerapp.util.JwtUtils
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.UUID
import kotlin.let

class CreateRoomViewModel(
    private val repository: AlquilerRepository
) : ViewModel() {
    var roomTitle by mutableStateOf("")
        private set
    var roomCity by mutableStateOf("")
        private set
    var roomAddress by mutableStateOf("")
        private set
    var roomPrice by mutableStateOf("") // Se mantiene como String para la entrada de texto
        private set
    var roomDescription by mutableStateOf("")
        private set
    var imageUrl by mutableStateOf("") // Campo para una URL de imagen
        private set
    var isSaving by mutableStateOf(false)
        private set
    var saveSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var habitacionActual: Habitacion? = null
        private set

    fun cargarHabitacionPorId(id: UUID) {
        viewModelScope.launch {
            try {
                habitacionActual = obtenerHabitacionPorId(id)
            } catch (e: Exception) {

            }
        }
    }

    private fun obtenerHabitacionPorId(id: UUID): Habitacion? {
        return habitacionActual
    }

    fun actualizarHabitacion(habitacion: Habitacion) {
        viewModelScope.launch {
            try {
                actualizarHabitacion(habitacion)
            } catch (e: Exception) {

            }
        }
    }

    fun eliminarHabitacion(id: UUID) {
        viewModelScope.launch {
            try {
                repository.eliminarHabitacion(id)
            } catch (e: Exception) {

            }
        }
    }

    private fun AlquilerRepository.eliminarHabitacion(id: UUID) {}

    fun onTitleChange(newValue: String) { roomTitle = newValue }
    fun onCityChange(newValue: String) { roomCity = newValue }
    fun onAddressChange(newValue: String) { roomAddress = newValue }
    fun onDescriptionChange(newValue: String) { roomDescription = newValue }
    fun onImageUrlChange(newValue: String) { imageUrl = newValue }
    fun onImageSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val token = TokenStore(context).getToken()
                val userId = getUserIdFromToken(token)

                if (userId == null) {
                    errorMessage = "No se pudo obtener el ID del usuario"
                    return@launch
                }

                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", tempFile.name, requestFile)
                val userIdPart = MultipartBody.Part.createFormData("userId", userId)

                val response = repository.uploadImage(imagePart, userIdPart)
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.url ?: ""
                    onImageUrlChange(imageUrl)
                } else {
                    errorMessage = "Error al subir imagen: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Excepción al subir imagen: ${e.message}"
            }
        }
    }
    fun onPriceChange(newValue: String) {
        // Permite la entrada de números y el punto decimal
        if (newValue.all { it.isDigit() || it == '.' }) {
            roomPrice = newValue
        }
    }

    fun createRoom(context: Context) {
        val priceValue = roomPrice.toDoubleOrNull()

        if (roomTitle.isBlank() || roomCity.isBlank() || roomAddress.isBlank() || priceValue == null) {
            errorMessage = "El título, la ciudad, la dirección y el precio son obligatorios."
            return
        }

        viewModelScope.launch {
            isSaving = true
            errorMessage = null
            try {
                val token = TokenStore(context).getToken() // ← ahora dentro de la corrutina
                val userId = getUserIdFromToken(token)

                if (userId == null) {
                    errorMessage = "No se pudo obtener el ID del usuario"
                    isSaving = false
                    return@launch
                }

                val imagesList = if (imageUrl.isNotBlank()) listOf(imageUrl) else emptyList()

                val roomDto = CrearHabitacionDto(
                    titulo = roomTitle,
                    ciudad = roomCity,
                    direccion = roomAddress,
                    precioMensual = priceValue,
                    descripcion = roomDescription,
                    imagenesUrl = imagesList,
                    propietarioId = userId
                )

                repository.crearHabitacion(roomDto)
                saveSuccess = true
            } catch (e: Exception) {
                errorMessage = "Error al crear la habitación: ${e.message ?: "Error desconocido"}"
            } finally {
                isSaving = false
            }
        }
    }

    fun getUserIdFromToken(token: String?): String? {
        return token?.let { JwtUtils.extractClaim(it, "sub") }
    }

}