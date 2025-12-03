package com.example.alquilerapp.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Intercepta las solicitudes HTTP y agrega un encabezado de autorización con el token proporcionado.
 *
 */
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {

    /**
     * Intercepta la solicitud HTTP y agrega el encabezado de autorización si hay un token disponible.
     *@param chain El objeto Interceptor.Chain para continuar con la cadena de interceptores.
     *@return La respuesta de la solicitud HTTP modificada.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        Log.d("AuthInterceptor", "Token enviado: $token")
        Log.d("AuthInterceptor", "Request con Authorization: ${request.headers["Authorization"]}")

        return chain.proceed(request)
    }


}