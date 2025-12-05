package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.UploadResponse
import com.example.alquilerapp.data.model.dto.CrearHabitacionDto
import com.example.alquilerapp.data.network.ApiService
import okhttp3.MultipartBody
import retrofit2.Response
import java.util.UUID

/**
 * Repositorio para operaciones relacionadas con habitaciones.
 * @param apiService El servicio API para acceder a los datos.
 * @return El repositorio de habitaciones.
 */
class AlquilerRepository(
    private val apiService: ApiService) {
    /**
     * Crea una nueva habitación.
     * @param dto El DTO que contiene los datos de la habitación a crear.
     */
    suspend fun crearHabitacion(dto: CrearHabitacionDto): Habitacion {
        return apiService.crearHabitacion(dto)
    }

    /**
     * Obtiene la lista de habitaciones del propietario.
     * @return La lista de habitaciones del propietario.
     */
    suspend fun getHabitacionesPropietario(): List<Habitacion> {
        return apiService.getHabitacionesPropietario()
    }

    /**
     * Sube una imagen a la API.
     * @param image La imagen a subir.
     * @return La respuesta que contiene la URL de la imagen subida.
     */
    suspend fun uploadImage(image: MultipartBody.Part, userId: MultipartBody.Part): Response<UploadResponse> {
        return apiService.subirImagen(image, userId)
    }

    /**
     * Elimina una habitación.
     * @param id El identificador único de la habitación a eliminar.
     */
    suspend fun eliminarHabitacion(id: UUID) {
        apiService.eliminarHabitacion(id)
    }

    /**
     * Edita una habitación existente.
     * @param id El identificador único de la habitación a editar.
     * @param habitacion El objeto que contiene los nuevos datos de la habitación.
     * @return La respuesta que contiene la habitación editada.
     */
    suspend fun editarHabitacion(id: UUID, habitacion: Habitacion): Response<Habitacion> {
        return apiService.editarHabitacion(id, habitacion)
    }
}