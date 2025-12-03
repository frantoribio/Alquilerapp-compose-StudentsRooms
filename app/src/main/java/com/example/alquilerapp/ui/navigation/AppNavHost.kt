package com.example.alquilerapp.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alquilerapp.data.TokenStore
import com.example.alquilerapp.data.network.ApiServiceBuilder
import com.example.alquilerapp.repository.AlquilerRepository
import com.example.alquilerapp.repository.ReservaRepository
import com.example.alquilerapp.ui.components.BottomBar
import com.example.alquilerapp.ui.screens.AdminScreen
import com.example.alquilerapp.ui.screens.CrearUsuarioScreen
import com.example.alquilerapp.ui.screens.CreateRoomScreen
import com.example.alquilerapp.ui.screens.EditarHabitacionScreen
import com.example.alquilerapp.ui.screens.EditarUsuarioScreen
import com.example.alquilerapp.ui.screens.EstudianteScreen
import com.example.alquilerapp.ui.screens.HabitacionesAdminScreen
import com.example.alquilerapp.ui.screens.LandingScreen
import com.example.alquilerapp.ui.screens.LoginScreen
import com.example.alquilerapp.ui.screens.PropietarioScreen
import com.example.alquilerapp.ui.screens.RegistroScreen
import com.example.alquilerapp.ui.screens.ReservaScreen
import com.example.alquilerapp.ui.screens.ReservasAdminScreen
import com.example.alquilerapp.ui.screens.UsuariosAdminScreen
import com.example.alquilerapp.viewmodel.CreateRoomViewModelFactory
import com.example.alquilerapp.viewmodel.HabitacionesViewModel
import com.example.alquilerapp.viewmodel.HabitacionesViewModelFactory
import com.example.alquilerapp.viewmodel.LoginViewModel
import com.example.alquilerapp.viewmodel.PropietarioViewModel
import com.example.alquilerapp.viewmodel.PropietarioViewModelFactory
import com.example.alquilerapp.viewmodel.ReservasViewModel
import com.example.alquilerapp.viewmodel.UsuariosViewModel
import java.util.UUID

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    loginVM: LoginViewModel,
    usuariosVM: UsuariosViewModel,
    propietarioFactory: PropietarioViewModelFactory,
    createRoomFactory: CreateRoomViewModelFactory,
    habVM: HabitacionesViewModel,
    tokenStore: TokenStore
) {
    NavHost(
        navController = navController,
        startDestination = "landing",
        modifier = modifier
    ) {
        composable("landing") {
            LandingScreen(viewModel = viewModel(), onLoginClick = { navController.navigate("login") })
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
                apiService = ApiServiceBuilder.create(tokenStore),
                viewModel = viewModel(),
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

        /*composable("reservas") {
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
                    reservaRepository.eliminarReserva(reserva.id)
                }
            )

            navController.navigate("reservas"


            )

        }*/

        composable("editar_habitacion/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.let { UUID.fromString(it) }
            if (id != null) {
                EditarHabitacionScreen(
                    habitacionesViewModel = viewModel(),
                    navController = navController,
                    modifier = Modifier,
                    id = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

