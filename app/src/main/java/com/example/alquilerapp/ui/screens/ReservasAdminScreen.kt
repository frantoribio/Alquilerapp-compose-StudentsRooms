package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alquilerapp.data.model.Reserva
import com.example.alquilerapp.viewmodel.ReservasViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasAdminScreen(
    viewModel: ReservasViewModel,
    onBack: () -> Unit,
    onEditReserva: (Reserva) -> Unit,
    onDeleteReserva: (Reserva) -> Unit
) {
    val reservas = viewModel.reservas
    val loading = viewModel.loading
    val error = viewModel.errorMessage


    var expanded by remember { mutableStateOf(false) }
    var ciudadFiltro by remember { mutableStateOf("") }
    var fechaFiltro by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }



    LaunchedEffect(Unit) {
        viewModel.loadReservas()
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
                                    value = ciudadFiltro,
                                    onValueChange = { ciudadFiltro = it },
                                    label = { Text("Ciudad") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = fechaFiltro,
                                    onValueChange = { fechaFiltro = it },
                                    label = { Text("Fecha (YYYY-MM-DD)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(onClick = {
                                        ciudadFiltro = ""
                                        fechaFiltro = ""
                                        expanded = false
                                    }) {
                                        Text("Limpiar")
                                    }
                                    Button(onClick = { expanded = false }) {
                                        Text("Aplicar")
                                    }
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
        LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
            items(reservas.size) { res ->
                var expandedCard by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Fecha de entrada: ")
                        Text("Fecha de salida: ")


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
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Eliminar") },
                                    onClick = {
                                        expandedCard = false
                                        showDialog = true
                                    }
                                )
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
                text = {
                    Text("¿Seguro que deseas eliminar la reserva de ${reservaAEliminar!!} ?")
                },
                confirmButton = {
                    Button(onClick = {
                        onDeleteReserva(reservaAEliminar!!)
                        showDialog = false
                    }) {
                        Text("Sí, eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
