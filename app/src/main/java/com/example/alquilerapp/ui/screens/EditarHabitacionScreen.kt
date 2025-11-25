package com.example.alquilerapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.viewmodel.HabitacionesViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarHabitacionScreen(
    context: Context = LocalContext.current,
    habitacionesViewModel: HabitacionesViewModel,
    navController: NavController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    id: UUID,
    onNavigateBack: () -> Unit
) {
    var titulo: String? by remember { mutableStateOf("") }
    var ciudad: String? by remember { mutableStateOf("") }
    var direccion: String? by remember { mutableStateOf("") }
    var precioMensual: Double? by remember { mutableStateOf(null) }
    var descripcion: String? by remember { mutableStateOf("") }
    var imagenesUrl: List<String> by remember { mutableStateOf(emptyList()) }
    var error by remember { mutableStateOf("") }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            habitacionesViewModel.onImageSelected(context, uri)
        }
    }


    LaunchedEffect(id) {
        val habitacion = habitacionesViewModel.obtenerHabitacionPorId(id)
        habitacion?.let { habitacion ->
            titulo = habitacion.titulo
            ciudad = habitacion.ciudad
            direccion = habitacion.direccion
            precioMensual = habitacion.precioMensual
            descripcion = habitacion.descripcion
            imagenesUrl = habitacion.imagenesUrl
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Editar Habitación", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        titulo?.let { it1 ->
            OutlinedTextField(
                value = it1,
                onValueChange = { titulo = it; error = "" },
                label = { Text("Título") },
                singleLine = true
            )
        }

        ciudad?.let { it1 ->
            OutlinedTextField(
                value = it1,
                onValueChange = { ciudad = it; error = "" },
                label = { Text("Ciudad") },
                singleLine = true
            )
        }

        direccion?.let { it1 ->
            OutlinedTextField(
                value = it1,
                onValueChange = { direccion = it; error = "" },
                label = { Text("Dirección") },
                singleLine = true
            )
        }

        precioMensual?.let { it1 ->
            OutlinedTextField(
                value = it1.toString(),
                onValueChange = {
                    precioMensual = it.toDoubleOrNull()
                    error = ""
                },
                label = { Text("Precio Mensual") },
                singleLine = true
            )
        }

        descripcion?.let { it1 ->
            OutlinedTextField(
                value = it1,
                onValueChange = { descripcion = it; error = "" },
                label = { Text("Descripción") },
                singleLine = true
            )
        }

        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar imagen desde el dispositivo")
        }

        if (error.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                if (titulo?.isBlank() == true ||
                    ciudad?.isBlank() == true ||
                    direccion?.isBlank() == true ||
                    precioMensual == null ||
                    descripcion?.isBlank() == true
                ) {
                    error = "Todos los campos son obligatorios"
                } else {
                    scope.launch {
                        habitacionesViewModel.actualizarHabitacion(
                            id = id,
                            habitacion = Habitacion(
                                id = id,
                                titulo = titulo!!,
                                ciudad = ciudad!!,
                                direccion = direccion!!,
                                precioMensual = precioMensual!!,
                                descripcion = descripcion!!,
                                imagenesUrl = imagenesUrl
                            )
                        )
                        navController.popBackStack() // vuelve a la lista
                    }
                }
            }
        ) {
            Text("Actualizar Habitación")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onNavigateBack) {
            Text("Cancelar")
        }
    }
}
