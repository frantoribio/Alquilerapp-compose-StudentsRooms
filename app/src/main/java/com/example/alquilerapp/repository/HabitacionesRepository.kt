package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.LoginRequest
import com.example.alquilerapp.data.model.LoginResponse
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.UploadResponse
import com.example.alquilerapp.data.network.RetrofitClient
import okhttp3.MultipartBody
import retrofit2.Response
import java.util.UUID

/**
 * clase que se encarga de obtener los datos de la api
 */
class HabitacionesRepository {
    private val api = RetrofitClient.instance

    /**
     * obtiene las habitaciones de la api
     */
    suspend fun getHabitaciones(): Response<List<Habitacion>> {
        return api.getHabitaciones()
    }

    /**
     * obtiene el login de la api
     */
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }

    suspend fun uploadImage(image: MultipartBody.Part): Response<UploadResponse> {
        return api.uploadImage(image)
    }

    suspend fun editarHabitacion(id: UUID, habitacion: Habitacion) {
        api.editarHabitacion(id, habitacion)
    }

    suspend fun eliminarHabitacion(habitacionId: UUID) {
        api.eliminarHabitacion(habitacionId)
    }
}
