package com.example.alquilerapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquilerapp.data.network.ApiService
import com.example.alquilerapp.viewmodel.ReservasViewModel
//import com.example.alquilerapp.data.network.RetrofitClientReserva
//import com.example.alquilerapp.repository.ReservaRepository
//import com.example.alquilerapp.viewmodel.ReservaViewModelFactory
//import com.example.alquilerapp.viewmodel.ReservasViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Composable para la pantalla de reservas.
 * @param idHabitacion El ID de la habitación a reservar.
 * @param onBack Función para volver atrás.
 * @return El composable de la pantalla de reservas.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(
    //apiService: ApiService,
    viewModel: ReservasViewModel,
    idHabitacion: String?,
    onBack: () -> Unit,
    navController: NavController,
) {
    val todayMillis = Instant.now().toEpochMilli()
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }
        }
    )
    var fechaEntrada by remember { mutableStateOf<LocalDate?>(null) }
    var fechaSalida by remember { mutableStateOf<LocalDate?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withLocale(Locale("es", "ES"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservar habitación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Selecciona tu rango de fechas",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                DatePicker(state = datePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                val selectedMillis = datePickerState.selectedDateMillis
                val selectedDate = selectedMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                }

                selectedDate?.let {
                    if (fechaEntrada == null) {
                        fechaEntrada = it
                    } else {
                        fechaSalida = it
                        if (fechaSalida!!.isBefore(fechaEntrada)) {
                            error = "La fecha de salida debe ser posterior a la de entrada"
                        } else {
                            error = null
                        }
                    }
                }

                idHabitacion?.let { Text("Habitación: $it") }
                fechaEntrada?.let { Text("Entrada: ${it.format(formatter)}") }
                fechaSalida?.let { Text("Salida: ${it.format(formatter)}") }

                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    if (fechaEntrada != null || fechaSalida != null) {
                        Button(onClick = {
                            fechaEntrada = null
                            fechaSalida = null
                            error = null
                            datePickerState.selectedDateMillis = null
                        }) {
                            Text("Limpiar selección")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    if (fechaEntrada != null && fechaSalida != null && error == null) {
                        Button(onClick = {
                            idHabitacion?.let { habId ->
                                viewModel.confirmarReserva(
                                    habitacionId = habId,
                                    entrada = fechaEntrada!!.toString(),
                                    salida = fechaSalida!!.toString(),
                                    estadoReserva = "PENDIENTE"
                                )

                                navController.navigate("reservasAlumno")

                            }
                        }) {
                            Text("Confirmar Reserva")
                        }
                    }
                }
            }
        }
    }
}