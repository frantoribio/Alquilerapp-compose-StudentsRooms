package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alquilerapp.data.model.dto.ReservaDTO

@Composable
fun ListaReservasAlumno(reservasAlumno: List<ReservaDTO>) {
    if (reservasAlumno.isEmpty()) {
        Text(
            text = "No hay reservas para este alumno.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
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
    }
}

