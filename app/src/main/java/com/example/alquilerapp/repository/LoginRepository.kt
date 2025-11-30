package com.example.alquilerapp.repository

import com.example.alquilerapp.data.model.LoginRequest
import com.example.alquilerapp.data.model.LoginResponse
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.network.RetrofitClient
import retrofit2.Response

/**
 * clase que se encarga de obtener los datos de la api para el login

 * @param api el servicio que se encarga de obtener los datos de la api
 * @return las habitaciones de la api
 *
 *
 */
class LoginRepository {
    private val api = RetrofitClient.instance

    /**
     * obtiene las habitaciones de la api
     * @return las habitaciones de la api
     */
    suspend fun getHabitaciones(): Response<List<Habitacion>> {
        return api.getHabitaciones()
    }

    /**
     * obtiene el login de la api
     * @param email el email del usuario
     * @param password la contraseña del usuario
     * @return el login de la api
     */
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }

}
