package com.example.alquilerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alquilerapp.repository.UsuarioRepository

/**
 * Fábrica para crear una instancia de UsuariosViewModel
 * ya que requiere una dependencia (UsuarioRepository).
 */
class UsuariosViewModelFactory(
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuariosViewModel::class.java)) {
            return UsuariosViewModel(usuarioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}