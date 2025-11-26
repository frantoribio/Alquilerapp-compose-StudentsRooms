package com.example.alquilerapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alquilerapp.data.TokenStore
import com.example.alquilerapp.data.network.ApiServiceBuilder
import com.example.alquilerapp.repository.AlquilerRepository
import com.example.alquilerapp.repository.ReservaRepository
import com.example.alquilerapp.repository.UsuarioRepository
import com.example.alquilerapp.ui.components.BottomBar
import com.example.alquilerapp.ui.screens.*
import com.example.alquilerapp.viewmodel.CreateRoomViewModelFactory
import com.example.alquilerapp.viewmodel.HabitacionesViewModel
import com.example.alquilerapp.viewmodel.HabitacionesViewModelFactory
import com.example.alquilerapp.viewmodel.LoginViewModel
import com.example.alquilerapp.viewmodel.PropietarioViewModel
import com.example.alquilerapp.viewmodel.PropietarioViewModelFactory
import com.example.alquilerapp.viewmodel.ReservaViewModelFactory
import com.example.alquilerapp.viewmodel.ReservasViewModel
//import com.example.alquilerapp.viewmodel.ReservasViewModel
import com.example.alquilerapp.viewmodel.UsuariosViewModel
import com.example.alquilerapp.viewmodel.UsuariosViewModelFactory
import java.util.UUID

/**
 * MainActivity principal que configura la navegación y el tema de la aplicación.
 */
class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    val habVM: HabitacionesViewModel = viewModel()
                    val loginVM: LoginViewModel = viewModel()
                    val onLogout: () -> Unit = {
                        loginVM.logout()
                        navController.navigate("login") {
                            popUpTo("landing") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    val context = LocalContext.current
                    val tokenStore = remember { TokenStore(context) }
                    val apiService = remember { ApiServiceBuilder.create(tokenStore) }
                    val alquilerRepository = remember { AlquilerRepository(apiService) }
                    val createRoomFactory = remember {
                        CreateRoomViewModelFactory(alquilerRepository)
                    }
                    val propietarioFactory = remember {
                        PropietarioViewModelFactory(alquilerRepository)
                    }
                    val usuariosVM: UsuariosViewModel = viewModel(factory = UsuariosViewModelFactory(UsuarioRepository(apiService)))

                    NavHost(navController = navController, startDestination = "landing") {

                        composable("landing") {
                            LandingScreen(viewModel = habVM, onLoginClick = { navController.navigate("login") })
                        }

                        composable("login") {
                            Scaffold(
                                bottomBar = { BottomBar(navController) }
                            ) { padding ->
                                LoginScreen(
                                    viewModel = loginVM,
                                    onRoleNavigate = { role ->
                                        when (role.uppercase()) {
                                            "ADMIN" -> navController.navigate("administrador") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                            "PROPIETARIO" -> navController.navigate("propietario") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                            "ALUMNO" -> navController.navigate("alumno") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    },
                                    onNavigateToRegistro = { navController.navigate("registro") },
                                    modifier = Modifier.padding(padding)
                                )
                            }
                        }

                        composable("registro") {
                            Scaffold(bottomBar = { BottomBar(navController) }) { padding ->
                                RegistroScreen(
                                    registroViewModel = loginVM,
                                    navController = navController,
                                    modifier = Modifier.padding(padding),
                                    onNavigateBack = { navController.navigate("login") }
                                )
                            }
                        }

                        composable("admin") {
                            Scaffold(bottomBar = { BottomBar(navController) }) { padding ->
                                UsuariosAdminScreen(
                                    viewModel = usuariosVM,
                                    onCrearUsuario = { navController.navigate("usuarioForm") },
                                    onEditarUsuario = { usuario -> navController.navigate("usuarioForm?id=${usuario.id}") },
                                    onLogout = onLogout,
                                    onBack = { navController.navigate("administrador") },
                                    modifier = Modifier.padding(padding)
                                )
                            }
                        }

                        composable("administrador") {
                            Scaffold(bottomBar = { BottomBar(navController) }) { padding ->
                                AdminScreen(
                                    navController = navController,
                                    onLogout = onLogout,
                                    modifier = Modifier.padding(padding)
                                )
                            }
                        }

                        composable("propietario") {
                            Scaffold(
                                bottomBar = { BottomBar(navController) }
                            ) { padding ->
                                val propietarioVM: PropietarioViewModel = viewModel(factory = propietarioFactory)

                                PropietarioScreen(
                                    viewModel = viewModel(factory = propietarioFactory),
                                    onLogout = onLogout,
                                    onNavigateToCreateRoom = { navController.navigate("create_room_screen") },
                                    onEditRoom = { habitacion ->
                                        navController.navigate("editar_habitacion/${habitacion.id}")
                                    },
                                    onDeleteRoom = { habitacion ->
                                        propietarioVM.eliminarHabitacion(habitacion.id)
                                    },
                                    modifier = Modifier.padding(padding),
                                    navController = navController
                                )
                            }
                        }

                        composable("create_room_screen") {
                            CreateRoomScreen(
                                viewModel = viewModel(factory = createRoomFactory),
                                onRoomCreated = {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("shouldRefresh", true)
                                    navController.popBackStack()
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("alumno") {
                            Scaffold(
                                bottomBar = { BottomBar(navController) }
                            ) { padding ->
                                EstudianteScreen(
                                    viewModel = habVM,
                                    onLogout = onLogout,
                                    onReservarClick = { idHabitacion ->
                                        navController.navigate("reservaConfirmada/$idHabitacion")
                                    },
                                    modifier = Modifier.padding(padding)

                                )
                            }
                        }

                        composable("reservaConfirmada/{idHabitacion}") { backStackEntry ->
                            val idHabitacion = backStackEntry.arguments?.getString("idHabitacion")
                            ReservaScreen(
                                idHabitacion = idHabitacion,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("usuarioForm") {
                            CrearUsuarioScreen(
                                usuariosViewModel = usuariosVM,
                                navController = navController,
                                modifier = Modifier,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("usuarioForm?id={id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.let { UUID.fromString(it) }
                            EditarUsuarioScreen(
                                usuariosViewModel = usuariosVM,
                                navController = navController,
                                modifier = Modifier,
                                id = id,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }


                        composable("habitaciones") {
                            val repository = AlquilerRepository(
                                apiService = ApiServiceBuilder.create(tokenStore)
                            )
                            val viewModel: HabitacionesViewModel = viewModel(
                                factory = HabitacionesViewModelFactory(repository)
                            )
                            HabitacionesAdminScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onEditRoom = { habitacion ->
                                    navController.navigate("editar_habitacion/${habitacion.id}")
                                },
                                onDeleteRoom = { habitacion ->
                                    viewModel.eliminarHabitacion(habitacion.id)
                                }
                            )
                        }

                        composable("reservas") {
                            val reservaRepository = ReservaRepository(
                                apiService = ApiServiceBuilder.create(tokenStore)
                            )
                            ReservasAdminScreen(
                                viewModel = ReservasViewModel(reservaRepository),
                                onBack = { navController.popBackStack() },
                                onEditReserva = { reserva ->
                                    navController.navigate("editar_reserva/${reserva.id}")
                                },
                                onDeleteReserva = { reserva ->



                                }


                            )

                        }

                        composable("editar_habitacion/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.let { UUID.fromString(it) }
                            if (id != null) {
                                EditarHabitacionScreen(
                                    habitacionesViewModel = habVM,
                                    navController = navController,
                                    modifier = Modifier,
                                    id = id,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
