package com.example.alquilerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alquilerapp.repository.AlquilerRepository

/**
 * Fábrica para crear una instancia de PropietarioViewModel
 * ya que requiere una dependencia (AlquilerRepository).
 */
class PropietarioViewModelFactory(
    private val repository: AlquilerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropietarioViewModel::class.java)) {
            return PropietarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}