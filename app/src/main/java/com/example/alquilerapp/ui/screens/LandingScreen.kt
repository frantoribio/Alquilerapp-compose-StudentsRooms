package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alquilerapp.viewmodel.HabitacionesViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.example.alquilerapp.data.model.getEmulatedImageUrl
import java.text.Normalizer

/**
 * Composable para la pantalla inicial.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(viewModel: HabitacionesViewModel, onLoginClick: () -> Unit) {
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
                title = { Text("Alquiler de Habitaciones") },
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
                    IconButton(onClick = onLoginClick) {
                        Icon(Icons.Default.Person, contentDescription = "Login")
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
                        Spacer(Modifier.height(4.dp))
                        Text(hab.direccion, style = MaterialTheme.typography.headlineLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("Precio: €${hab.precioMensual} / mes")
                        Spacer(Modifier.height(8.dp))
                        Text(hab.descripcion)
                    }
                }
            }
        }
    }
}

fun String.normalizado(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()
}
