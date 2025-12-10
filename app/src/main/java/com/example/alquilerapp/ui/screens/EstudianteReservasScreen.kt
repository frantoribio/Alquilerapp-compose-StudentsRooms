package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alquilerapp.viewmodel.ReservasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteReservasScreen(
    alumnoId: String,
    viewModel: ReservasViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(alumnoId) {
        viewModel.cargarReservasPorAlumno(alumnoId)
    }

    val reservasAlumno by viewModel.reservasAlumnos.collectAsState()

    //var reservasAlumno = viewModel.reservasAlumno


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas del alumno") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("alumno") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (reservasAlumno.isEmpty()) {
            Text(
                text = "No hay reservas para este alumno.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(reservasAlumno) { reserva ->
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
                            Text("Id del alumno: ${reserva.alumnoId}")

                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(padding)) {
                ListaReservasAlumno(reservasAlumno)


            }
        }
    }
}

