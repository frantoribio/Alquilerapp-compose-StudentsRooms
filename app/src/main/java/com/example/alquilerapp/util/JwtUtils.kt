package com.example.alquilerapp.util

import android.util.Base64
import org.json.JSONObject

/**
 * Clase de utilidad para operaciones relacionadas con JWTs.
 */
object JwtUtils {
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
