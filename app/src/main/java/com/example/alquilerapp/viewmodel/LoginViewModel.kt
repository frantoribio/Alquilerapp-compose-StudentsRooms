package com.example.alquilerapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquilerapp.data.TokenStore
import com.example.alquilerapp.data.model.RegistroRequest
import com.example.alquilerapp.data.network.RetrofitClient
import com.example.alquilerapp.repository.LoginRepository
import com.example.alquilerapp.util.JwtUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Clase de ViewModel para la autenticación.
 * @param application La aplicación que usa este ViewModel.
 * @return El ViewModel para la autenticación.
 * @property repo Repositorio para interactuar con la API.
 * @property store Almacen de tokens.
 * @property _role MutableStateFlow para el rol del usuario.
 * @property role StateFlow para el rol del usuario.
 * @property _loading MutableStateFlow para indicar si se está cargando.
 * @property loading StateFlow para indicar si se está cargando.
 * @property _loginError MutableStateFlow para el mensaje de error de inicio de sesión.
 * @property loginError StateFlow para el mensaje de error de inicio de sesión.
 * @property login Función para iniciar sesión.
 * @property logout Función para cerrar sesión.
 * @property registrar Función para registrar un nuevo usuario.
 * @property onResult Una función de devolución de llamada que se llama con el resultado del registro.
 * @property request El objeto de solicitud de registro.
 * @property response La respuesta del servidor.
 * @property body El cuerpo de la respuesta.
 * @property token El token de autenticación.
 * @property extracted El rol extraído del token.
 * @property role El rol del usuario.
 * @property e El objeto de excepción.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = LoginRepository()
    private val store = TokenStore(application.applicationContext)
    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError


    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _loginError.value = null // ← Reinicia el error antes de intentar

            try {
                val resp = repo.login(email, password)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    val token = body?.token
                    var role = body?.rol
                    if (!token.isNullOrEmpty() && role.isNullOrBlank()) {
                        val extracted = JwtUtils.extractClaim(token, "role") ?: JwtUtils.extractClaim(token, "roles")
                        if (!extracted.isNullOrBlank()) role = extracted
                    }
                    if (!token.isNullOrEmpty()) {
                        store.saveToken(token, role)
                        Log.d("LoginViewModel", "Token guardado: $token, rol: $role")
                        _role.value = role
                    } else {
                        _loginError.value = "Token inválido o faltante"
                    }
                } else {
                    _loginError.value = "Credenciales incorrectas"
                }

            } catch (e: Exception) {
                _loginError.value = "Error de conexión: ${e.message}"
            }
            _loading.value = false
        }
    }

    /**
     * Cierra la sesión actual.
     */
    fun logout() {
        _role.value = null
        println("Sesión cerrada y estado reseteado.")
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param nombre El nombre del usuario.
     * @param email El correo electrónico del usuario.
     * @param contraseña La contraseña del usuario.
     * @param rol El rol del usuario (PROPIETARIO o ALUMNO").
     * @param onResult Una función de devolución de llamada que se llama con el resultado del registro.
     */
    fun registrar(nombre: String, email: String, contraseña: String, rol: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = RegistroRequest(nombre, email, contraseña, rol)
                val response = RetrofitClient.instance.registrarUsuario(request)
                if (response.isSuccessful) {
                    onResult(true, response.body()?.mensaje ?: "Registrado correctamente")
                } else {
                    onResult(false, "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(false, "Excepción: ${e.message}")
            }
        }
    }
}
