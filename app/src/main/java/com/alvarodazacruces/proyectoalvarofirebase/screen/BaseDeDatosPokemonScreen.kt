package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel

@Composable
fun BaseDeDatosPokemonScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    onNavigateToAddPokemon: () -> Unit,
    onNavigateToModifyPokemon: () -> Unit,
    onNavigateToListPokemon: () -> Unit,
    onNavigateToDeletePokemon: () -> Unit,
    viewModel: FirestorePokemonViewModel
) {
    // Estado de la UI
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Interceptar el evento de retroceso del dispositivo
    BackHandler {
        // Redirige al menú cuando el botón de retroceso es presionado
        navController.navigate("menu") {
            // Limpiar la pila de navegación para evitar que el usuario regrese a la pantalla de BaseDeDatosPokemonScreen
            popUpTo("menu") { inclusive = true }
        }
    }

    // Layout principal
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 195.dp, bottom = 10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de encabezado
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            // Título
            Text(
                text = "Base De Datos Pokémons",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Mostrar error si existe
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Botón de "Agregar Pokémon"
            Button(
                onClick = onNavigateToAddPokemon,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Agregar Pokémon")
            }

            // Botón de "Modificar Pokémon"
            Button(
                onClick = onNavigateToModifyPokemon,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Modificar Pokémon")
            }

            // Botón de "Ver Lista de Pokémon"
            Button(
                onClick = onNavigateToListPokemon,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Ver Lista de Pokémon")
            }

            // Botón de "Eliminar Pokémon"
            Button(
                onClick = onNavigateToDeletePokemon,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Eliminar Pokémon")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de "Cerrar Sesión"
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
