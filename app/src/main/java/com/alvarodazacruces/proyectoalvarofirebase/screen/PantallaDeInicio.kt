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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonSearchViewModel

@Composable
fun PantallaDeInicio(
    viewModel: PokemonSearchViewModel,
    navController: NavHostController,
    onNavigateToDetail: (String) -> Unit, // Función para navegar a los detalles del Pokémon
    onLogout: () -> Unit // Función para cerrar sesión
) {
    val pokemon by viewModel.pokemon.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    // Manejo del botón de retroceso
    BackHandler {
        navController.navigate("menu") {
            // Aseguramos que la pantalla de inicio sea la única en el stack
            popUpTo("menu") { inclusive = true }
        }
    }

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
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "Busca tu Pokémon",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Nombre o ID del Pokémon") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (showError) {
                Text(
                    text = "¡Debes ingresar un nombre!",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Button(
                onClick = {
                    if (inputText.isBlank()) {
                        showError = true
                    } else {
                        showError = false
                        viewModel.searchPokemon(inputText.lowercase().trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    "Buscar Pokémon",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            when {
                isLoading -> CircularProgressIndicator() // Muestra un indicador de carga mientras se busca
                error != null -> Text("Error: $error", color = Color.Red) // Muestra errores si ocurren
                pokemon != null -> {
                    LaunchedEffect(pokemon) {
                        pokemon?.name?.let { onNavigateToDetail(it) } // Navega a los detalles del Pokémon encontrado
                        viewModel.clearState() // Limpia el estado después de navegar
                    }
                }
            }
            // Botón para navegar a la lista de Pokémon
            Button(
                onClick = {
                    viewModel.clearState() // Limpia el estado antes de navegar
                    navController.navigate("pokemon_list") {
                        // Eliminar todas las rutas anteriores hasta llegar a "pantalla_inicio"
                        popUpTo("pantalla_inicio") { inclusive = false }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    "Ver Lista de Pokémon",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Botón para cerrar sesión
            Button(
                onClick = onLogout, // Llama al manejador de cierre de sesión
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    "Cerrar Sesión",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
