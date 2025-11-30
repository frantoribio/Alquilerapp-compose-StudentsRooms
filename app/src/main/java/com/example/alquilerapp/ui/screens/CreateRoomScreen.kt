package com.example.alquilerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alquilerapp.viewmodel.CreateRoomViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Composable para la creación de una nueva habitación.
 * @param viewModel El ViewModel asociado a esta pantalla.
 * @param onRoomCreated Función para volver atrás al éxito.
 * @param onBack Función para volver atrás.
 * @return El composable de la creación de una nueva habitación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    viewModel: CreateRoomViewModel = viewModel(),
    onRoomCreated: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onImageSelected(context, uri)
        }
    }
    val isSaving = viewModel.isSaving

    LaunchedEffect(viewModel.saveSuccess) {
        if (viewModel.saveSuccess) {
            Toast.makeText(context, "Habitación creada con éxito", Toast.LENGTH_SHORT).show()
            onRoomCreated()
        }
    }

    val isFormValid = remember(
        viewModel.roomTitle, viewModel.roomCity, viewModel.roomAddress, viewModel.roomPrice
    ) {
        viewModel.roomTitle.isNotBlank() &&
                viewModel.roomCity.isNotBlank() &&
                viewModel.roomAddress.isNotBlank() &&
                viewModel.roomPrice.toDoubleOrNull() != null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Habitación") },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isSaving) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Añadir scroll
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.roomTitle,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Título del Anuncio (*)") },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.roomCity,
                onValueChange = viewModel::onCityChange,
                label = { Text("Ciudad (*)") },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.roomAddress,
                onValueChange = viewModel::onAddressChange,
                label = { Text("Dirección de la Propiedad (*)") },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.roomPrice,
                onValueChange = viewModel::onPriceChange,
                label = { Text("Precio Mensual (*)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = { Text("€") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.roomDescription,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Descripción detallada") },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar imagen desde el dispositivo")
            }

            Button(
                onClick = { viewModel.createRoom(context) },
                enabled = !isSaving && isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Habitación")
                }
            }

            viewModel.errorMessage?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}