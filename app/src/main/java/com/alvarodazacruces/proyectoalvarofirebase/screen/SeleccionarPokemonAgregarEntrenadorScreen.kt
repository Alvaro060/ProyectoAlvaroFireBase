package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel

@Composable
fun SeleccionarPokemonAgregarEntrenadorScreen(
    pokemonViewModel: FirestorePokemonViewModel,
    navController: NavController
) {
    // Estado que almacenará la lista de Pokémon
    val pokemonList by pokemonViewModel.pokemonList.collectAsState(initial = emptyList())

    // Estado para el filtro de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtramos la lista de Pokémon según el nombre
    val filteredPokemonList = pokemonList.filter { pokemon ->
        pokemon.name.lowercase().contains(searchQuery.lowercase())
    }

    // Si la lista de Pokémon está vacía, mostramos un mensaje
    if (filteredPokemonList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay Pokémon disponibles o coinciden con la búsqueda.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // Campo de búsqueda
            item {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar Pokémon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )
            }

            // Lista filtrada de Pokémon
            items(filteredPokemonList) { pokemon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            // Pasamos el ID del Pokémon seleccionado al BackStack
                            navController.previousBackStackEntry?.savedStateHandle?.set("selectedPokemon", pokemon.id)
                            navController.popBackStack() // Volver a la pantalla anterior
                        },
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Fondo verde suave
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen del Pokémon
                        AsyncImage(
                            model = "https://img.pokemondb.net/artwork/large/${pokemon.name.lowercase()}.jpg",
                            contentDescription = "Imagen de ${pokemon.name}",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "#${pokemon.id} ${pokemon.name.capitalize()}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tipo: ${pokemon.type}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}
