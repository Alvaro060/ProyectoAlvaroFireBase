package com.alvarodazacruces.proyectoalvarofirebase.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alvarodazacruces.proyectoalvarofirebase.data.AuthViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonListViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonSearchViewModel
import com.alvarodazacruces.proyectoalvarofirebase.screen.*

@Composable
fun Navegacion(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    pokemonSearchViewModel: PokemonSearchViewModel,
    firestoreViewModel: FirestoreViewModel
) {
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        if (authViewModel.isSignedIn()) {
            startDestination = "menu"
        } else {
            startDestination = "login"
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onSignInAnonymously = {
                    authViewModel.signInAnonymously()
                    navController.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate("menu") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }
        composable("menu") {
            MenuScreen(
                navController = navController,
                onLogout = {
                    authViewModel.logout()
                    pokemonSearchViewModel.clearState()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("pantalla_inicio") {
            PantallaDeInicio(
                viewModel = pokemonSearchViewModel,
                navController = navController,
                onNavigateToDetail = { pokemonId ->
                    navController.navigate("detalle_pokemon/$pokemonId")
                },
                onLogout = {
                    authViewModel.logout()
                    pokemonSearchViewModel.clearState()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("detalle_pokemon/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""
            DetallePokemonScreen(
                pokemonId = pokemonId,
                navigateToBack = { navController.popBackStack() }
            )
        }
        composable("pokemon_list") {
            val pokemonListViewModel: PokemonListViewModel = viewModel()
            PokemonListScreen(
                viewModel = pokemonListViewModel,
                onNavigateToDetail = { pokemonId ->
                    navController.navigate("detalle_pokemon/$pokemonId")
                }
            )
        }

        // Base de Datos

        composable("pantalla_inicio_base_de_datos") {
            PantallaDeInicioBaseDeDatos(
                navController = navController,
                onLogout = {
                    authViewModel.logout()
                    pokemonSearchViewModel.clearState()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToPokemons = {
                    navController.navigate("base_de_datos_pokemon_screen")
                },
                onNavigateToEntrenadores = {
                    navController.navigate("base_de_datos_entrenador_screen")
                }
            )
        }

        composable("base_de_datos_pokemon_screen") {
            BaseDeDatosPokemonScreen(
                navController = navController,
                onLogout = {
                    authViewModel.logout()
                    pokemonSearchViewModel.clearState()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToAddPokemon = {
                    navController.navigate("add_pokemon_screen")
                },
                onNavigateToListModifyPokemon = {
                    navController.navigate("lista_modificar_pokemon_screen")
                },
                onNavigateToVerListaPokemon = {
                    navController.navigate("ver_lista_pokemon_screen")
                },
                onNavigateToDeletePokemon = {
                    navController.navigate("eliminar_pokemon_screen")
                },
                viewModel = firestoreViewModel
            )
        }

        composable("add_pokemon_screen") {
            AgregarPokemonScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("lista_modificar_pokemon_screen") {
            ListaModificarPokemonScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("modify_pokemon_screen/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""
            ModificarPokemonScreen(
                viewModel = firestoreViewModel,
                navController = navController,
                pokemonId = pokemonId
            )
        }

        composable("ver_lista_pokemon_screen") {
            VerListaPokemonScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("eliminar_pokemon_screen") {
            EliminarPokemonScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("base_de_datos_entrenador_screen") {
            BaseDeDatosEntrenadorScreen(
                navController = navController,
                onLogout = {
                    authViewModel.logout()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToAddEntrenador = {
                    navController.navigate("add_entrenador_screen")
                },
                onNavigateToListModifyEntrenador = {
                    navController.navigate("lista_modificar_entrenador_screen")
                },
                onNavigateToVerListaEntrenadores = {
                    navController.navigate("ver_lista_entrenadores_screen")
                },
                onNavigateToDeleteEntrenador = {
                    navController.navigate("eliminar_entrenador_screen")
                },
                viewModel = firestoreViewModel
            )
        }

        composable("add_entrenador_screen") {
            AgregarEntrenadorScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("seleccionar_pokemon_agregar_entrenador_screen") {
            SeleccionarPokemonAgregarEntrenadorScreen(
                pokemonViewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("lista_modificar_entrenador_screen") {
            ListaModificarEntrenadorScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("modify_entrenador_screen/{entrenadorId}") { backStackEntry ->
            val entrenadorId = backStackEntry.arguments?.getString("entrenadorId") ?: ""
            ModificarEntrenadorScreen(
                navController = navController,
                entrenadorId = entrenadorId,
                viewModel = firestoreViewModel
            )
        }

        composable("ver_lista_entrenadores_screen") {
            VerListaEntrenadoresScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("eliminar_entrenador_screen") {
            EliminarEntrenadorScreen(
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

        composable("ver_lista_pokemons_entrenador/{entrenadorId}") { backStackEntry ->
            val entrenadorId = backStackEntry.arguments?.getString("entrenadorId") ?: ""

            VerListaPokemonsEntrenadorScreen(
                entrenadorId = entrenadorId,
                viewModel = firestoreViewModel,
                navController = navController
            )
        }

    }
}
