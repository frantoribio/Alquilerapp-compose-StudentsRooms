package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alquilerapp.data.model.dto.ReservaDTO
import com.example.alquilerapp.viewmodel.ReservasViewModel

/**
 * Composable para la pantalla de gestión de reservas del administrador.
 * @param viewModel El ViewModel asociado a esta pantalla.
 * @param onBack Función para volver atrás.
 * @param onEditReserva Función para editar una reserva.
 * @param onDeleteReserva Función para eliminar una reserva.
 * @return El composable de la pantalla de gestión de reservas del administrador.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasAdminScreen(
    viewModel: ReservasViewModel,
    onBack: () -> Unit,
    onEditReserva: (ReservaDTO) -> Unit,
    onDeleteReserva: (ReservaDTO) -> Unit = { reserva -> viewModel.eliminarReserva(reserva.id) }
) {
    val reservas = viewModel.reservas
    val loading = viewModel.loading
    val error = viewModel.errorMessage
    var expanded by remember { mutableStateOf(false) }
    var alumnoIdFiltro by remember { mutableStateOf("") }
    var habitacionIdFiltro by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var reservaAEliminar: ReservaDTO? by remember { mutableStateOf(null) }


    LaunchedEffect(Unit) {
        viewModel.loadReservas()
    }

    val reservasFiltradas = reservas.filter { reserva ->
        (alumnoIdFiltro.isBlank() || reserva.alumnoId.toString().contains(alumnoIdFiltro)) &&
                (habitacionIdFiltro.isBlank() || reserva.habitacionId.toString().contains(habitacionIdFiltro))
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Reservas") },
                actions = {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                OutlinedTextField(
                                    value = alumnoIdFiltro,
                                    onValueChange = { alumnoIdFiltro = it },
                                    label = { Text("Id Alumno") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = habitacionIdFiltro,
                                    onValueChange = { habitacionIdFiltro = it },
                                    label = { Text("Id Habitación") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(onClick = {
                                        alumnoIdFiltro = ""
                                        habitacionIdFiltro = ""
                                        expanded = false
                                    }) {
                                        Text("Limpiar") }
                                    Button(onClick = { expanded = false }) {
                                        Text("Aplicar") }
                                }
                            }
                        }
                    }
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    items(reservasFiltradas) { reserva ->
                        var expandedCard by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ID: ${reserva.id}")
                                Text("Habitación: ${reserva.habitacionId}")
                                Text("Propietario: ${reserva.propietarioEmail}")
                                Text("Alumno: ${reserva.alumnoId}")
                                Text("Estado: ${reserva.estadoReserva}")
                                Text("Fecha de entrada: ${reserva.fechaInicio}")
                                Text("Fecha de salida: ${reserva.fechaFin}")

                                Spacer(Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = { expandedCard = true }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                                    }
                                    DropdownMenu(
                                        expanded = expandedCard,
                                        onDismissRequest = { expandedCard = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Editar") },
                                            onClick = {
                                                expandedCard = false
                                                onEditReserva(reserva)
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Eliminar") },
                                            onClick = {
                                                expandedCard = false
                                                reservaAEliminar = reserva
                                                showDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog && reservaAEliminar != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Seguro que deseas eliminar la reserva de ${reservaAEliminar!!.id}?") },
                confirmButton = {
                    Button(onClick = {
                        onDeleteReserva(reservaAEliminar!!)
                        showDialog = false
                    }) { Text("Sí, eliminar") }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }
}