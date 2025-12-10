package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquilerapp.viewmodel.ReservasViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteReservasScreen(
    alumnoId: String,
    viewModel: ReservasViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Cargar todas las reservas al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadReservas()
    }

    val todasLasReservas = viewModel.reservas
    val reservasFiltradas = todasLasReservas.filter {
        it.alumnoId.trim().equals(alumnoId.trim(), ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas del alumno") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (reservasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay reservas para este alumno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(reservasFiltradas) { reserva ->
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Confirmar eliminación") },
                            text = { Text("¿Seguro que quieres eliminar esta reserva?") },
                            confirmButton = {
                                Button(onClick = {
                                    viewModel.eliminarReserva(reserva.id)
                                    showDialog = false
                                }) {
                                    Text("Eliminar")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Información de la reserva
                            Text("Reserva ID: ${reserva.id}")
                            Text("Habitación: ${reserva.habitacionId}")
                            Text("Estado: ${reserva.estadoReserva}")
                            Text("Entrada: ${reserva.fechaInicio}")
                            Text("Salida: ${reserva.fechaFin}")

                            // 🔽 Papelera abajo a la derecha
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                IconButton(onClick = { showDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Eliminar reserva",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant // gris
                                    )
                                }
                            }
                        }
                    }
                    }
                }
            }
        }
    }

