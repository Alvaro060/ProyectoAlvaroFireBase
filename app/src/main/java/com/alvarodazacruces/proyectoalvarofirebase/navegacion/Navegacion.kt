package com.alvarodazacruces.proyectoalvarofirebase.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alvarodazacruces.proyectoalvarofirebase.screen.DetallePokemonScreen
import com.alvarodazacruces.proyectoalvarofirebase.screen.PantallaDeInicio
import com.alvarodazacruces.proyectoalvarofirebase.screen.PokemonSearchViewModel

@Composable
fun Navegacion(
    navController: NavHostController,
    viewModel: PokemonSearchViewModel
) {
    NavHost(navController = navController, startDestination = "pantalla_inicio") {
        composable("pantalla_inicio") {
            PantallaDeInicio(
                viewModel = viewModel,
                onNavigateToDetail = { pokemonId ->
                    navController.navigate("detalle_pokemon/$pokemonId")
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
    }
}
