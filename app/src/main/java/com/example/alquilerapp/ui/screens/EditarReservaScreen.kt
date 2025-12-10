package com.example.alquilerapp.ui.screens

import android.content.Context
import android.graphics.fonts.FontStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alquilerapp.viewmodel.ReservasViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarReservaScreen(
    reservasViewModel: ReservasViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    id: String?,
    onNavigateBack: () -> Unit
) {
    var error by remember { mutableStateOf("") }
    val reserva by reservasViewModel.reservaSeleccionada.collectAsState()

    LaunchedEffect(id) {
        reservasViewModel.cargarReserva(id!!)
    }

    LaunchedEffect(reserva) {
        reserva?.let {
            // Aquí podrías manejar la reserva cargada
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Reserva") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = "EN CONSTRUCCIÓN!!",
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

