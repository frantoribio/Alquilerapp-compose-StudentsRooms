package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquilerapp.viewmodel.ReservasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasHabitacionScreen(
    habitacionId: String,
    viewModel: ReservasViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    // Observar el flujo de reservas

    var reservas = viewModel.reservasHabitacion


    // Lanzar la carga al entrar en pantalla
    LaunchedEffect(habitacionId) {
        viewModel.cargarReservasPorHabitacion(habitacionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas de la Habitación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (reservas.isEmpty()) {
            Text(
                text = "No hay reservas para esta habitación.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(reservas) { reserva ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Reserva ID: ${reserva.id}")
                            Text("Entrada: ${reserva.fechaInicio}")
                            Text("Salida: ${reserva.fechaFin}")
                            Text("Estado: ${reserva.estadoReserva}")
                        }
                    }
                }
            }
        }
    }
}

