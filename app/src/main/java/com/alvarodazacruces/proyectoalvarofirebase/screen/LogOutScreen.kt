package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.AuthViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonSearchViewModel

@Composable
fun LogOutScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    pokemonSearchViewModel: PokemonSearchViewModel
) {
    LaunchedEffect(Unit) {
        authViewModel.logout()
        // Limpiar el estado del ViewModel
        pokemonSearchViewModel.clearState()
        // Navegar a la pantalla de inicio de sesión y limpiar la pila
        navController.navigate("login") {
            popUpTo(navController.graph.startDestinationRoute!!) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cerrando sesión...",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator()
    }
}