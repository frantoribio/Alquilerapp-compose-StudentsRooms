package com.example.alquilerapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquilerapp.data.model.Habitacion
import com.example.alquilerapp.viewmodel.HabitacionesViewModel
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Composable para la pantalla de edición de habitaciones.
 * @param habitacionesViewModel El ViewModel de habitaciones.
 * @param navController El controlador de navegación.
 * @param modifier El modificador para personalizar el diseño.
 * @param context El contexto de la aplicación.
 * @param id El identificador de la habitación a editar.
 * @param onNavigateBack La función a ejecutar al navegar hacia atrás.
 * @return El composable de la pantalla de edición de habitaciones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarHabitacionScreen(
    habitacionesViewModel: HabitacionesViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    id: UUID?,
    onNavigateBack: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var precioMensual by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenesUrl by remember { mutableStateOf(emptyList<String>()) }
    var error by remember { mutableStateOf("") }
    val habitacion by habitacionesViewModel.habitacionSeleccionada.collectAsState()

    LaunchedEffect(id) {
        habitacionesViewModel.cargarHabitacion(id!!)
    }

    LaunchedEffect(habitacion) {
        //val habitacion = habitacionesViewModel.obtenerHabitacionPorId(id!!)
        habitacion?.let {
            titulo = it.titulo
            ciudad = it.ciudad
            direccion = it.direccion
            precioMensual = it.precioMensual.toString()
            descripcion = it.descripcion
            imagenesUrl = it.imagenesUrl
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

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it; error = "" },
            label = { Text("Título") },
            singleLine = true
        )

        OutlinedTextField(
            value = ciudad,
            onValueChange = { ciudad = it; error = "" },
            label = { Text("Ciudad") },
            singleLine = true
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it; error = "" },
            label = { Text("Dirección") },
            singleLine = true
        )

        OutlinedTextField(
            value = precioMensual,
            onValueChange = { precioMensual = it; error = "" },
            label = { Text("Precio Mensual") },
            singleLine = true
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it; error = "" },
            label = { Text("Descripción") },
            singleLine = true
        )

        if (error.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                val precio = precioMensual.toDoubleOrNull()
                when {
                    titulo.isBlank() || ciudad.isBlank() || direccion.isBlank() || descripcion.isBlank() -> {
                        error = "Todos los campos son obligatorios"
                    }
                    precio == null -> {
                        error = "El precio debe ser numérico"
                    }
                    else -> {
                        scope.launch {
                            habitacionesViewModel.editarHabitacion(
                                id = id!!,
                                habitacion = Habitacion(
                                    id = id,
                                    titulo = titulo,
                                    ciudad = ciudad,
                                    direccion = direccion,
                                    precioMensual = precio,
                                    descripcion = descripcion,
                                    imagenesUrl = imagenesUrl
                                )
                            )
                            navController.previousBackStackEntry?.savedStateHandle?.set("shouldRefresh", true)
                            navController.popBackStack()
                        }
                    }
                }
            }
        ) {
            Text("Actualizar Habitación")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onNavigateBack) {
            Text("Cancelar")
        }
    }
}
