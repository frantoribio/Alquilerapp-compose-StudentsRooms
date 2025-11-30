package com.example.alquilerapp.util

import android.util.Base64
import org.json.JSONObject

/**
 * Clase de utilidad para operaciones relacionadas con JWTs.
 * Proporciona métodos para extraer información del token JWT.
 * @return El valor del claim o null si no se encuentra.
 */
object JwtUtils {

    /**
     * Extrae un claim (atributo) del token JWT.
     * @param token El token JWT.
     * @param claim El nombre del claim a extraer.
     * @return El valor del claim o null si no se encuentra.
     */
    fun extractClaim(token: String, claim: String): String? {
        try {
            val parts = token.split('.')
            if (parts.size < 2) return null
            val payload = parts[1]
            val decoded = String(Base64.decode(payload, Base64.URL_SAFE))
            val obj = JSONObject(decoded)
            return if (obj.has(claim)) obj.getString(claim) else null
        } catch (e: Exception) {
            return null
        }
    }
}
