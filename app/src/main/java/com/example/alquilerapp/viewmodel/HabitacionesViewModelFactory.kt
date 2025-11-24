package com.example.alquilerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alquilerapp.repository.AlquilerRepository

class HabitacionesViewModelFactory(
    private val repository: AlquilerRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitacionesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitacionesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
