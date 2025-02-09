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
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonListViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonSearchViewModel
import com.alvarodazacruces.proyectoalvarofirebase.screen.*

@Composable
fun Navegacion(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    pokemonSearchViewModel: PokemonSearchViewModel
) {
    // Variable para almacenar el destino inicial
    var startDestination by remember { mutableStateOf("login") }

    // Verificar el estado de autenticación al inicio
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
            MenuScreen(navController = navController)
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
        composable("base_de_datos_pokemon_screen") {
            // Aquí obtenemos el ViewModel de Firestore para esta pantalla
            val firestoreViewModel: FirestorePokemonViewModel = viewModel()

            // Pasamos las funciones de navegación como parámetros a la pantalla
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
                onNavigateToModifyPokemon = {
                    navController.navigate("modify_pokemon_screen")
                },
                onNavigateToListPokemon = {
                    navController.navigate("pokemon_list_screen")
                },
                onNavigateToDeletePokemon = {
                    navController.navigate("delete_pokemon_screen")
                },
                viewModel = firestoreViewModel // Pasamos el ViewModel
            )
        }
        composable("add_pokemon_screen") {
            val firestoreViewModel: FirestorePokemonViewModel = viewModel() // Obtenemos el ViewModel
            AgregarPokemonScreen(
                viewModel = firestoreViewModel, // Pasamos el ViewModel a la pantalla
                navController = navController
            )
        }




    }
}