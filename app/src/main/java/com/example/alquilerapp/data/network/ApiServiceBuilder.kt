package com.example.alquilerapp.data.network

import com.example.alquilerapp.network.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.alquilerapp.data.TokenStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

/**
 * Builder para crear una instancia de ApiService con un interceptor de autenticación.
 */
object ApiServiceBuilder {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    /**
     * Crea una instancia de ApiService con un interceptor de autenticación.
     *
     */
    fun create(tokenStore: TokenStore): ApiService {
        val tokenProvider: () -> String? = {
            runBlocking {
                tokenStore.tokenFlow.firstOrNull()
            }
        }
        val interceptor = AuthInterceptor(tokenProvider)
        return create(interceptor)
    }

    /**
     * Crea una instancia de ApiService con un interceptor de autenticación.
     *
     * @param authInterceptor El interceptor de autenticación a utilizar.
     */
    fun create(authInterceptor: AuthInterceptor): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}