package com.example.alquilerapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "alquiler_prefs")

/**
 * Clase para almacenar el token en las preferencias compartidas.
 */
class TokenStore(private val context: Context) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val ROLE_KEY = stringPreferencesKey("user_role")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    val roleFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ROLE_KEY]
    }

    /**
     * Guarda el token en las preferencias compartidas.
     */
    suspend fun saveToken(token: String, role: String?) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            if (role != null) prefs[ROLE_KEY] = role
        }
    }

    /**
     * Borra el token de las preferencias compartidas.
     */
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(ROLE_KEY)
        }
    }

    /**
     * Obtiene el token almacenado en las preferencias compartidas.
     */
    suspend fun getToken() : String? {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }.first()}
}
