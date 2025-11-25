package com.example.alquilerapp.data.network

import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.LoginRequest
import com.example.alquilerapp.data.model.LoginResponse
import com.example.alquilerapp.data.model.RegistroRequest
import com.example.alquilerapp.data.model.RegistroResponse
import com.example.alquilerapp.data.model.UploadResponse
import com.example.alquilerapp.data.model.Usuario
import com.example.alquilerapp.data.model.dto.CrearHabitacionDto
import okhttp3.MultipartBody

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

/**
 * Interfaz que define los puntos finales de la API para el alquiler de habitaciones.
 *
 */
interface ApiService {

    /**
     * Obtiene la lista de habitaciones disponibles para alquilar.
     *
     * @return Una respuesta que contiene la lista de habitaciones.
     */
    @GET("api/habitaciones")
    suspend fun getHabitaciones(): Response<List<Habitacion>>

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param request El objeto que contiene las credenciales de inicio de sesión.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * Registra un nuevo usuario.
     *
     * @param request El objeto que contiene los datos del nuevo usuario.
     */
    @POST("api/usuarios/registro")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<RegistroResponse>

    @GET("usuarios")
    suspend fun listarUsuarios(): List<Usuario>

    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: UUID, @Body usuario: Usuario): Response<Usuario>

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: UUID): Response<Void>

    @POST("habitaciones")
    suspend fun crearHabitacion(
        @Body nuevaHabitacion: CrearHabitacionDto
    ): Habitacion

    @GET("habitaciones/propietario")
    suspend fun getHabitacionesPropietario(): List<Habitacion>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<UploadResponse>

    @Multipart
    @POST("imagenes")
    suspend fun subirImagen(
        @Part image: MultipartBody.Part,
        @Part userId: MultipartBody.Part
    ): Response<UploadResponse>

    @DELETE("habitaciones/{id}")
    suspend fun eliminarHabitacion(@Path("id") id: UUID)

    /*@PUT("habitaciones/{id}")
    suspend fun editarHabitacion(@Path("id") id: UUID, @Body habitacion: Habitacion)
*/
    @PUT("habitaciones/{id}")
    suspend fun editarHabitacion(
        @Path("id") id: UUID,
        @Body habitacion: Habitacion
    ): Response<Habitacion>


}

