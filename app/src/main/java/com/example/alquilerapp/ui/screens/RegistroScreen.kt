package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquilerapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla de registro de usuario.
 *
 * @param registroViewModel El ViewModel para manejar la lógica del registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    registroViewModel: LoginViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var contraseñaVisible by remember { mutableStateOf(false) }
    var rolSeleccionado by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    val roles = listOf("PROPIETARIO", "ALUMNO")

    fun esEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                error = ""
            },
            label = { Text("Nombre") },
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = ""
            },
            label = { Text("Email") },
            singleLine = true
        )

        OutlinedTextField(
            value = contraseña,
            onValueChange = {
                contraseña = it
                error = ""
            },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (contraseñaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (contraseñaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (contraseñaVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { contraseñaVisible = !contraseñaVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = rolSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rol") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { rol ->
                    DropdownMenuItem(
                        text = { Text(rol) },
                        onClick = {
                            rolSeleccionado = rol
                            expanded = false
                            error = ""
                        }
                    )
                }
            }
        }

        if (error.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        val scope = rememberCoroutineScope()


        Button(
            onClick = {
                if (nombre.isBlank() || email.isBlank() || contraseña.isBlank() || rolSeleccionado.isBlank()) {
                    error = "Todos los campos son obligatorios"
                } else if (!esEmailValido(email)) {
                    error = "El email introducido no es válido"
                } else {
                    scope.launch {
                        registroViewModel.registrar(nombre, email, contraseña, rolSeleccionado) { success, mensaje ->
                            if (success) {
                                navController.navigate("login")
                            } else {
                                error = mensaje
                            }
                        }
                    }
                }
            }
        ) {
            Text("Registrar")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onNavigateBack) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}