package com.alvarodazacruces.proyectoalvarofirebase.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alvarodazacruces.proyectoalvarofirebase.data.AuthViewModel
import com.alvarodazacruces.proyectoalvarofirebase.screen.LoginScreen
import com.alvarodazacruces.proyectoalvarofirebase.screen.SignUpScreen
import com.alvarodazacruces.proyectoalvarofirebase.screen.DetallePokemonScreen
import com.alvarodazacruces.proyectoalvarofirebase.screen.PantallaDeInicio
import com.alvarodazacruces.proyectoalvarofirebase.screen.LogOutScreen
import com.alvarodazacruces.proyectoalvarofirebase.screen.PokemonSearchViewModel

@Composable
fun Navegacion(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    pokemonSearchViewModel: PokemonSearchViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("pantalla_inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onSignInAnonymously = {
                    authViewModel.signInAnonymously()
                    navController.navigate("pantalla_inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate("pantalla_inicio") {
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
        composable("pantalla_inicio") {
            PantallaDeInicio(
                viewModel = pokemonSearchViewModel,
                onNavigateToDetail = { pokemonId ->
                    navController.navigate("detalle_pokemon/$pokemonId")
                },
                onLogout = {
                    navController.navigate("logout")
                }
            )
        }
        composable("detalle_pokemon/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""
            DetallePokemonScreen(
                pokemonId = pokemonId,
                navigateToBack = { navController.popBackStack() } // Para volver atrás
            )
        }
        composable("logout") {
            LogOutScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}