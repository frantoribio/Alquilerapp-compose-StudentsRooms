package com.example.alquilerapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.alquilerapp.data.model.Usuario
import com.example.alquilerapp.viewmodel.UsuariosViewModel
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Composable para la pantalla de gestión de usuarios del administrador.
 * @param viewModel El ViewModel asociado a esta pantalla.
 * @param onCrearUsuario Función para crear un nuevo usuario.
 * @param onEditarUsuario Función para editar un usuario.
 * @param onLogout Función para cerrar sesión.
 * @param onBack Función para volver atrás.
 * @param modifier El modificador para personalizar el diseño.
 * @return El composable de la pantalla de gestión de usuarios del administrador.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosAdminScreen(
    viewModel: UsuariosViewModel,
    onCrearUsuario: () -> Unit,
    onEditarUsuario: (Usuario) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuarios = viewModel.usuarios
    val loading = viewModel.loading
    val error = viewModel.errorMessage
    var showDialog by remember { mutableStateOf(false) }
    var usuarioAEliminar by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(onClick = onCrearUsuario) {
                Icon(Icons.Default.Add, contentDescription = "Crear nuevo usuario")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            } else if (error != null) {
                Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                Button(onClick = { viewModel.cargarUsuarios() }) {
                    Text("Reintentar")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                ) {
                    item {
                        Text("Usuarios registrados:", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                    }
                    items(usuarios) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Nombre: ${usuario.nombre}")
                                Text("Email: ${usuario.email}")
                                Text("Rol: ${usuario.rol}")
                                Row {
                                    Button(onClick = {
                                        usuarioAEliminar = usuario
                                        showDialog = true
                                    }) {
                                        Text("Eliminar")
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = {
                                        viewModel.seleccionarUsuario(usuario)
                                        onEditarUsuario(usuario)
                                    }) {
                                        Text("Editar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog && usuarioAEliminar != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar al usuario ${usuarioAEliminar!!.nombre}?") },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.eliminarUsuario(usuarioAEliminar!!.id!!)
                            showDialog = false
                        }) {
                            Text("Sí, eliminar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
