package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Composable para la pantalla del estudiante.
 * @param navController El controlador de navegación.
 * @param onLogout La función a ejecutar al cerrar sesión.
 * @param modifier El modificador para personalizar el diseño.
 * @return El composable de la pantalla del estudiante.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteScreen(
    navController: NavController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Panel del Estudiante") },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudentOption(
                icon = Icons.Default.Hotel,
                label = "Habitaciones",
                onClick = { navController.navigate("habitacionesReservar") }
            )
            StudentOption(
                icon = Icons.Default.Event,
                label = "Mis Reservas",
                onClick = { navController.navigate("misReservas") }
            )
        }
    }
}

/**
 * Composable para una opción del estudiante.
 * @param icon El icono de la opción.
 * @param label El texto de la opción.
 * @param onClick La función a ejecutar al hacer clic en la opción.
 */
@Composable
fun StudentOption(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = MaterialTheme.typography.titleMedium)
        }
    }
}