package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.alquilerapp.data.model.getEmulatedImageUrl
import com.example.alquilerapp.viewmodel.HabitacionesViewModel
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import java.util.UUID


/**
 * función que muestra el panel de estudiante
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteScreen(
    viewModel: HabitacionesViewModel,
    onLogout: () -> Unit,
    onReservarClick: (idHabitacion: UUID) -> Unit,
    modifier: Modifier = Modifier

) {
    val habitaciones by viewModel.habitaciones.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var ciudadFiltro by remember { mutableStateOf("") }
    var precioMaximo by remember { mutableStateOf("") }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loadHabitaciones()
    }

    val habitacionesFiltradas = habitaciones.filter { hab ->
        (ciudadFiltro.isBlank() || hab.ciudad.normalizado().contains(ciudadFiltro.normalizado())) &&
                (precioMaximo.toFloatOrNull()?.let { hab.precioMensual <= it } ?: true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Estudiante") },
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
                                    value = precioMaximo,
                                    onValueChange = { precioMaximo = it },
                                    label = { Text("Precio máximo (€)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(onClick = {
                                        ciudadFiltro = ""
                                        precioMaximo = ""
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
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }


    ) { padding ->
        LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
            items(habitacionesFiltradas) { hab ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = hab.getEmulatedImageUrl(),
                            contentDescription = "Imagen de ${hab.direccion}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(hab.ciudad, style = MaterialTheme.typography.headlineLarge)
                        Spacer(Modifier.height(16.dp))
                        Text(hab.direccion, style = MaterialTheme.typography.headlineLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("Precio: €${hab.precioMensual} / mes")
                        Spacer(Modifier.height(8.dp))
                        Text(hab.descripcion)
                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { onReservarClick(hab.id) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Reservar")
                        }
                    }
                }

            }
            item {
                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 64.dp
                    )
                )
            }
        }
    }
}

/**
 * función que muestra el panel de confirmación de reserva del estudiante
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaConfirmadaScreen(
    idHabitacion: String?,
    onBack: () -> Unit // función para volver atrás
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reserva Confirmada") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Reserva confirmada", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("ID de habitación: ${idHabitacion ?: "Desconocido"}")
            }
        }
    }
}
