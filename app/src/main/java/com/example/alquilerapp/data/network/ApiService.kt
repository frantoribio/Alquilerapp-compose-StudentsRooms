package com.example.alquilerapp.data.network

import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.LoginRequest
import com.example.alquilerapp.data.model.LoginResponse
import com.example.alquilerapp.data.model.RegistroRequest
import com.example.alquilerapp.data.model.RegistroResponse
import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.data.model.ReservaRequest
import com.example.alquilerapp.data.model.ReservaResponse
import com.example.alquilerapp.data.model.UploadResponse
import com.example.alquilerapp.data.model.Usuario
import com.example.alquilerapp.data.model.dto.CrearHabitacionDto
import com.example.alquilerapp.data.model.dto.ReservaDTO
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

    /**
     * Obtiene la lista de usuarios.
     *
     * @return Una lista de usuarios.
     */
    @GET("usuarios")
    suspend fun listarUsuarios(): List<Usuario>

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario El objeto que contiene los datos del nuevo usuario.
     * @return Una respuesta que contiene el usuario creado.
     */
    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id El identificador único del usuario a actualizar.
     * @return Una respuesta que contiene el usuario actualizado.
     */
    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: UUID,
        @Body usuario: Usuario
    ): Response<Usuario>

    /**
     * Edita una habitación existente.
     *
     * @param id El identificador único de la habitación a editar.
     * @param habitacion El objeto que contiene los nuevos datos de la habitación.
     * @return Una respuesta que contiene la habitación editada.
     */
    @PUT("habitaciones/{id}")
    suspend fun editarHabitacion(
        @Path("id") id: UUID,
        @Body habitacion: Habitacion
    ): Response<Habitacion>

    /**
     * Elimina un usuario.
     *
     * @param id El identificador único del usuario a eliminar.
     * @return Una respuesta que indica si la eliminación fue exitosa.
     */
    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: UUID): Response<Void>

    /**
     * Crea una nueva habitación.
     *
     * @param nuevaHabitacion El objeto que contiene los datos de la nueva habitación.
     * @return La habitación creada.
     */
    @POST("habitaciones")
    suspend fun crearHabitacion(
        @Body nuevaHabitacion: CrearHabitacionDto
    ): Habitacion

    /**
     * Obtiene la lista de habitaciones del propietario.
     *
     * @return Una lista de habitaciones del propietario.
     */
    @GET("habitaciones/propietario")
    suspend fun getHabitacionesPropietario(): List<Habitacion>

    /**
     * Sube una imagen a la API.
     *
     * @param image La imagen a subir.
     * @return Una respuesta que contiene la URL de la imagen subida.
     */
    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<UploadResponse>

    /**
     * Sube una imagen a la API.
     *
     * @param image La imagen a subir.
     */
    @Multipart
    @POST("imagenes")
    suspend fun subirImagen(
        @Part image: MultipartBody.Part,
        @Part userId: MultipartBody.Part
    ): Response<UploadResponse>

    /**
     * Elimina una habitación.
     *
     * @param id El identificador único de la habitación a eliminar.
     */
    @DELETE("habitaciones/{id}")
    suspend fun eliminarHabitacion(@Path("id") id: UUID)

    /**
     * Obtiene la lista de reservas.
     *
     * @return Una lista de reservas.
     */
    @GET("reservas")
    suspend fun listarReservas(): Response<List<ReservaDTO>>

    /**
     * Crea una nueva reserva.
     *
     * @param reserva El objeto que contiene los datos de la nueva reserva.
     * @return Una respuesta que contiene la reserva creada.
     */
    @POST("reservas")
    suspend fun crearReserva(@Body reserva: ReservaRequest): Response<ReservaResponse>

    /**
     * Actualiza los datos de una reserva existente.
     *
     * @param id El identificador único de la reserva a actualizar.
     * @param reserva El objeto que contiene los nuevos datos de la reserva.
     * @return Una respuesta que contiene la reserva actualizada.
     */
    @PUT("reservas/{id}")
    suspend fun actualizarReserva(@Path("id") id: UUID, @Body reserva: Reserva): Response<ReservaDTO>

    /**
     * Elimina una reserva.
     *
     * @param id El identificador único de la reserva a eliminar.
     */
    @DELETE("reservas/{id}")
    suspend fun eliminarReserva(@Path("id") id:String)

    @GET("reservas/habitacion/{id}")
    suspend fun obtenerReservasPorHabitacion(
        @Path("id") habitacionId: String): List<ReservaDTO>

    @GET("reservas/usuario/{id}")
    suspend fun obtenerReservasPorAlumno(
        @Path("id") usuarioId: String): List<ReservaDTO>
}
