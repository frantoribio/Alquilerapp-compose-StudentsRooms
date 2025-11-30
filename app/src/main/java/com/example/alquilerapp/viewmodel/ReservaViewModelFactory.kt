package com.example.alquilerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alquilerapp.repository.ReservaRepository

/**
 * Fábrica para crear una instancia de ReservasViewModel
 * ya que requiere una dependencia (ReservaRepository).
 */
class ReservaViewModelFactory(
    private val repository: ReservaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

