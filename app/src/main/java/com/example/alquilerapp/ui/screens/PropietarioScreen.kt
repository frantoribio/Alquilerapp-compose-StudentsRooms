package com.example.alquilerapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.data.model.getEmulatedImageUrl
import com.example.alquilerapp.viewmodel.PropietarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropietarioScreen(
    onLogout: () -> Unit,
    onNavigateToCreateRoom: () -> Unit,
    onEditRoom: (Habitacion) -> Unit,
    onDeleteRoom: (Habitacion) -> Unit,
    viewModel: PropietarioViewModel = viewModel(),
    modifier: Modifier,
    navController: NavController
) {
    val state = viewModel.habitaciones
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val shouldRefresh = savedStateHandle?.get<Boolean>("shouldRefresh") == true

    var showDialog by remember { mutableStateOf(false) }
    var habitacionAEliminar by remember { mutableStateOf<Habitacion?>(null) }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.cargarHabitacionesPropietario()
            savedStateHandle?.remove<Boolean>("shouldRefresh")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mis Propiedades") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateRoom) {
                Icon(Icons.Filled.Add, contentDescription = "Crear nueva habitación")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            } else if (errorMessage != null) {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                Button(onClick = viewModel::cargarHabitacionesPropietario) {
                    Text("Reintentar")
                }
            } else if (state.isEmpty()) {
                Text("No tienes habitaciones listadas. ¡Crea una!", modifier = Modifier.padding(24.dp))
            }

            if (state.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text("Habitaciones Listadas:", style = MaterialTheme.typography.titleMedium)
                        Divider(Modifier.padding(vertical = 8.dp))
                    }
                    items(state) { habitacion ->
                        HabitacionListItem(
                            habitacion = habitacion,
                            onEditRoom = onEditRoom,
                            onDeleteRoom = {
                                habitacionAEliminar = it
                                showDialog = true
                            }
                        )
                    }
                }
            }

            if (showDialog && habitacionAEliminar != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirmar eliminación") },
                    text = {
                        Text("¿Estás seguro de que deseas eliminar la habitación \"${habitacionAEliminar!!.titulo}\"?")
                    },
                    confirmButton = {
                        Button(onClick = {
                            onDeleteRoom(habitacionAEliminar!!)
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
}

@SuppressLint("DefaultLocale")
@Composable
fun HabitacionListItem(
    habitacion: Habitacion,
    onEditRoom: (Habitacion) -> Unit,
    onDeleteRoom: (Habitacion) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            habitacion.getEmulatedImageUrl()?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen de la habitación",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(habitacion.titulo, style = MaterialTheme.typography.titleLarge)
            Text("${habitacion.ciudad} - ${habitacion.direccion}")
            Text(
                "Precio: ${String.format("%.2f", habitacion.precioMensual)}€/mes",
                style = MaterialTheme.typography.bodyMedium
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            onEditRoom(habitacion)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            onDeleteRoom(habitacion)
                        }
                    )
                }
            }
        }
    }
}