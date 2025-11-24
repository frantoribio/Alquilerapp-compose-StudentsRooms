package com.example.alquilerapp.di

import android.content.Context
import com.example.alquilerapp.data.TokenStore
import com.example.alquilerapp.data.network.ApiService
import com.example.alquilerapp.data.network.ApiServiceBuilder
import com.example.alquilerapp.network.AuthInterceptor
import com.example.alquilerapp.repository.UsuarioRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class AppContainer(private val applicationContext: Context) {

    private val tokenStore: TokenStore = TokenStore(applicationContext)
    private val tokenProvider: () -> String? = {
        runBlocking {
            tokenStore.tokenFlow.first()
        }
    }
    private val authInterceptor: AuthInterceptor = AuthInterceptor(tokenProvider)
    val apiService: ApiService = ApiServiceBuilder.create(authInterceptor)
    val usuarioRepository: UsuarioRepository = UsuarioRepository(apiService)
}
